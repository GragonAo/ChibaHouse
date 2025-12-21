package org.gragon.storage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.common.core.utils.StringUtils;
import org.gragon.common.mybatis.core.page.PageQuery;
import org.gragon.common.mybatis.core.page.TableDataInfo;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.storage.domain.Item;
import org.gragon.storage.domain.bo.ItemBo;
import org.gragon.storage.domain.vo.ItemVo;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.mapper.ItemMapper;
import org.gragon.storage.service.ItemService;
import org.gragon.storage.service.SpacePermissionService;
import org.gragon.storage.service.StorageSpaceService;
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

    private final StorageSpaceService storageSpaceService;
    private final SpacePermissionService spacePermissionService;


    /**
     * 通过物品ID查询物品信息
     *
     * @param id 物品ID
     * @return 物品信息
     */
    public ItemVo getItemById(Long id) {
        ItemVo itemVo = baseMapper.selectVoById(id);
        if (itemVo == null) throw new ServiceException("物品不存在");
        StorageSpaceVo storage = storageSpaceService.getStorageSpaceById(itemVo.getSpaceId());
        if (storage == null || !spacePermissionService.hasSpacePermission(storage.getFullPaths(), LoginHelper.getUserId())) {
            throw new ServiceException("用户没有空间权限");
        }
        return itemVo;
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
        // 权限校验
        Long userId = LoginHelper.getUserId();
        List<Long> accessibleSpaceIds = storageSpaceService.getAuthorizedSpaceList(userId);
        // 如果用户没有任何空间权限，传递一个不存在的空间id
        if (CollectionUtil.isEmpty(accessibleSpaceIds)) accessibleSpaceIds.add(-1L);

        return Wrappers.<Item>lambdaQuery()
                .in(Item::getSpaceId, accessibleSpaceIds)
                .eq(item.getSpaceId() != null, Item::getSpaceId, item.getSpaceId())
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
        StorageSpaceVo storage = storageSpaceService.getStorageSpaceById(itemBo.getSpaceId());
        if (storage == null || !spacePermissionService.hasSpacePermission(storage.getFullPaths(), LoginHelper.getUserId())) {
            throw new ServiceException("用户没有空间权限");
        }
        Item item = MapstructUtils.convert(itemBo, Item.class);
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
        if (item == null) throw new ServiceException("物品不存在");
        if (itemBo.getSpaceId() != null) {
            StorageSpaceVo storage = storageSpaceService.getStorageSpaceById(itemBo.getSpaceId());
            if (storage == null || !spacePermissionService.hasSpacePermission(storage.getFullPaths(), LoginHelper.getUserId())) {
                throw new ServiceException("用户没有空间权限");
            }
        }
        Item newItem = MapstructUtils.convert(itemBo, Item.class);
        newItem.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(newItem);
    }

    /**
     * 删除物品信息 (进行逻辑删除)
     *
     * @param id 物品ID
     * @return 删除结果
     */
    public int deleteItem(Long id) {
        Item item = baseMapper.selectById(id);
        if (item == null) throw new ServiceException("物品不存在");
        StorageSpaceVo storage = storageSpaceService.getStorageSpaceById(item.getSpaceId());
        if (storage == null || !spacePermissionService.hasSpacePermission(storage.getFullPaths(), LoginHelper.getUserId())) {
            throw new ServiceException("用户没有空间权限");
        }
        return baseMapper.deleteById(id);
    }
}
