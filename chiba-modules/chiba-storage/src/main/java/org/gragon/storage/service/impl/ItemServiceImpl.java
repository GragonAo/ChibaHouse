package org.gragon.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.common.core.utils.ObjectDiffUtils;
import org.gragon.common.core.utils.StringUtils;
import org.gragon.common.json.utils.JsonUtils;
import org.gragon.common.mybatis.core.page.PageQuery;
import org.gragon.common.mybatis.core.page.TableDataInfo;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.storage.domain.Item;
import org.gragon.storage.domain.ItemOperationLog;
import org.gragon.storage.domain.bo.ItemBo;
import org.gragon.storage.domain.enums.UserSpacePermissionType;
import org.gragon.storage.domain.vo.ItemVo;
import org.gragon.storage.mapper.ItemMapper;
import org.gragon.storage.mapper.ItemOperationLogMapper;
import org.gragon.storage.service.ItemService;
import org.gragon.storage.service.StorageSpaceService;
import org.gragon.storage.service.UserSpacePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper baseMapper;
    private final ItemOperationLogMapper logMapper;

    private final StorageSpaceService storageSpaceService;
    private final UserSpacePermissionService permissionService;


    /**
     * 通过物品ID查询物品信息
     *
     * @param id 物品ID
     * @return 物品信息
     */
    public ItemVo getItemById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 获取物品分页列表
     *
     * @param itemBo    物品业务对象
     * @param pageQuery 分页查询对象
     * @return 物品分页列表
     */
    public TableDataInfo<ItemVo> getItemPageList(ItemBo itemBo, PageQuery pageQuery) {
        Page<ItemVo> page = baseMapper.selectVoPage(pageQuery.build(), this.buildQueryWrapper(itemBo));
        return TableDataInfo.build(page);
    }

    private Wrapper<Item> buildQueryWrapper(ItemBo item) {
        Map<String, Object> params = item.getParams();
        return Wrappers.<Item>lambdaQuery()
                .like(StringUtils.isNotBlank(item.getName()), Item::getName, item.getName())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        Item::getCreateTime, params.get("beginTime"), params.get("endTime"))
                .orderByAsc(Item::getId);
    }

    /**
     * 插入物品信息
     *
     * @param itemBo 物品业务对象
     * @return 插入结果
     */
    public int insertItem(ItemBo itemBo) {
        // 检查用户是否有权限在该储物空间添加物品
        if (!permissionService.checkPermission(itemBo.getSpaceId(), LoginHelper.getUserId(),
                List.of(UserSpacePermissionType.READ_WRITE, UserSpacePermissionType.ADMIN))) {
            throw new ServiceException("没有权限在该储物空间添加物品");
        }

        Item item = MapstructUtils.convert(itemBo, Item.class);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        // TODO 生成 (条形码、二维码) 编码
        return baseMapper.insert(item);
    }

    /**
     * 更新物品信息
     *
     * @param itemBo 物品业务对象
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateItem(ItemBo itemBo) {
        Item item = baseMapper.selectById(itemBo.getId());
        if (item == null) return 0;
        // 检查用户是否有权限修改该物品信息
        if (!permissionService.checkPermission(item.getSpaceId(), LoginHelper.getUserId(),
                List.of(UserSpacePermissionType.READ_WRITE, UserSpacePermissionType.ADMIN))) {
            throw new ServiceException("没有权限修改该物品信息");
        }

        Item newItem = MapstructUtils.convert(itemBo, Item.class);

        ItemOperationLog log = new ItemOperationLog();
        ObjectDiffUtils.DiffNode<Item> node = ObjectDiffUtils.getDiffObjects(item, newItem, Item.class);

        Long newStorageSpaceId = node.getNewObj().getSpaceId();
        if (newStorageSpaceId != null) {
            // 检查用户是否有权限将物品移动到该储物空间
            if (!permissionService.checkPermission(newStorageSpaceId, LoginHelper.getUserId(),
                    List.of(UserSpacePermissionType.READ_WRITE, UserSpacePermissionType.ADMIN))) {
                throw new ServiceException("没有权限将物品移动到该储物空间");
            }
        }

        log.setItemId(itemBo.getId());
        log.setNewItemData(JsonUtils.toJsonString(node.getNewObj(), true, true));
        log.setOldItemData(JsonUtils.toJsonString(node.getOldObj(), true, true));
        logMapper.insert(log);

        newItem.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(newItem);
    }

    /**
     * 删除物品信息
     *
     * @param id 物品ID
     * @return 删除结果
     */
    public int deleteItem(Long id) {
        // 逻辑删除
        return baseMapper.deleteById(id);
    }
}
