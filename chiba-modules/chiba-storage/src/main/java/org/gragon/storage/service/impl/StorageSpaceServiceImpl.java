package org.gragon.storage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.common.core.utils.StringUtils;
import org.gragon.common.mybatis.core.page.PageQuery;
import org.gragon.common.mybatis.core.page.TableDataInfo;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.storage.domain.StorageSpace;
import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.mapper.StorageSpaceMapper;
import org.gragon.storage.service.SpacePermissionService;
import org.gragon.storage.service.StorageSpaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageSpaceServiceImpl implements StorageSpaceService {

    private final StorageSpaceMapper baseMapper;
    private final SpacePermissionService spacePermissionService;

    @Override
    public TableDataInfo<StorageSpaceVo> getSpacePageList(StorageSpaceBo spaceBo, PageQuery pageQuery) {
        Page<StorageSpaceVo> page = baseMapper.selectVoPage(pageQuery.build(), this.buildQueryWrapper(spaceBo));
        // 为每个空间设置完整路径名称
        page.getRecords().forEach(spaceVo -> {
            if (spaceVo != null) {
                spaceVo.setFullPathNames(this.getSpaceFullPathNameList(spaceVo.getFullPaths()));
            }
        });
        return TableDataInfo.build(page);
    }

    private Wrapper<StorageSpace> buildQueryWrapper(StorageSpaceBo spaceBo) {
        Map<String, Object> params = spaceBo.getParams();
        // 权限校验
        Long userId = LoginHelper.getUserId();
        List<Long> accessibleSpaceIds = this.getAuthorizedSpaceList(userId);
        // 如果用户没有任何空间权限，传递一个不存在的空间id
        if (accessibleSpaceIds.isEmpty()) accessibleSpaceIds.add(-1L);

        return Wrappers.<StorageSpace>lambdaQuery()
                .in(StorageSpace::getId, accessibleSpaceIds)
                .eq(spaceBo.getParentId() != null, StorageSpace::getParentId, spaceBo.getParentId())
                .like(StringUtils.isNotBlank(spaceBo.getName()), StorageSpace::getName, spaceBo.getName())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        StorageSpace::getCreateTime, params.get("beginTime"), params.get("endTime"))
                .orderByAsc(StorageSpace::getId);
    }

    /**
     * 查询用户有权限访问的储物空间ID列表
     *
     * @param userId 用户ID
     * @return 储物空间ID列表
     */
    @Override
    public List<Long> getAuthorizedSpaceList(Long userId) {
        List<Long> spaceAllDenyUidList = spacePermissionService.getSpaceAllDenyUidList(userId);
        List<StorageSpace> authorizedSpaces = baseMapper.selectList(new LambdaQueryWrapper<StorageSpace>()
                .notIn(CollectionUtil.isNotEmpty(spaceAllDenyUidList), StorageSpace::getId, spaceAllDenyUidList)
                .select(StorageSpace::getId)
        );
        if (CollectionUtil.isEmpty(authorizedSpaces))
            return new ArrayList<>();
        return authorizedSpaces.stream().map(StorageSpace::getId).toList();
    }

    /**
     * 通过储物空间ID获取储物空间信息
     *
     * @param id 储物空间ID
     * @return 储物空间信息
     */
    public StorageSpaceVo getStorageSpaceById(Long id) {
        StorageSpaceVo spaceVo = baseMapper.selectVoById(id);
        if (spaceVo != null) {
            spaceVo.setFullPathNames(this.getSpaceFullPathNameList(spaceVo.getFullPaths()));
        }
        return spaceVo;
    }

    /**
     * 获取根空间ID，如果不存在则初始化
     *
     * @return 根空间ID
     */
    @Override
    public long getRootSpace() {
        StorageSpace space = baseMapper.selectOne(new LambdaQueryWrapper<StorageSpace>()
                .eq(StorageSpace::getParentId, 0)
                .select(StorageSpace::getId)
        );
        if (space == null) {
            long rootId = this.initSpace();
            if (rootId == -1L)
                throw new ServiceException("初始化根空间错误");
            else return rootId;
        }
        return space.getId();
    }

    /**
     * 初始化根空间
     *
     * @return 根空间ID
     */
    private long initSpace() {
        StorageSpace space = new StorageSpace();
        space.setName("root");
        space.setSortOrder(0);
        if (baseMapper.insert(space) <= 0) {
            return -1L;
        }
        return space.getId();
    }

    /**
     * 获取储物空间完整路径名称列表
     *
     * @param spaceIdList 储物空间ID列表
     * @return 储物空间完整路径名称列表
     */
    private List<String> getSpaceFullPathNameList(List<Long> spaceIdList) {
        if (spaceIdList == null || spaceIdList.isEmpty()) {
            return new ArrayList<>();
        }
        List<StorageSpace> spaceList = baseMapper.selectList(new LambdaQueryWrapper<StorageSpace>()
                .in(StorageSpace::getId, spaceIdList)
                .orderByAsc(StorageSpace::getSortOrder)
        );
        return spaceList.stream().map(StorageSpace::getName).collect(Collectors.toList());
    }

    /**
     * 创建新的储物空间
     *
     * @param spaceBo 储物空间业务对象
     * @return 创建结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSpace(StorageSpaceBo spaceBo) {
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);

        // 构建路径信息
        SpacePathInfo pathInfo = checkParentAndBuildPath(
                spaceBo.getParentId(),
                LoginHelper.getUserId()
        );

        // 设置完整路径和排序
        space.setFullPaths(pathInfo.getFullPaths());
        space.setSortOrder(pathInfo.getSortOrder());

        return baseMapper.insert(space);
    }

    /**
     * 更新储物空间已用容量 (只记录物品的增加或删除)
     *
     * @param spaceId        储物空间ID
     * @param capacityChange 容量变更量（正数表示增加，负数表示减少）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSpaceUsedCapacity(Long spaceId, Integer capacityChange) {
        StorageSpace space = baseMapper.selectById(spaceId);
        if (space == null || capacityChange == 0) {
            return;
        }
        // 计算新的容量
        Integer newUsedCapacity = space.getUsedCapacity() + capacityChange;
        // 校验容量不能为负数
        if (newUsedCapacity < 0) {
            throw new ServiceException(
                    String.format("容量不足，当前容量：%d，变更量：%d",
                            space.getUsedCapacity(), capacityChange)
            );
        }
        // 更新容量
        int result = baseMapper.update(new LambdaUpdateWrapper<StorageSpace>()
                .eq(StorageSpace::getId, space.getId())
                .set(StorageSpace::getUsedCapacity, newUsedCapacity)
        );
        if (result == 0) {
            throw new ServiceException("更新存储空间容量失败");
        }
        // 可以添加日志记录
        log.info("存储空间容量更新成功，空间ID：{}，原容量：{}，变更量：{}，新容量：{}",
                space.getId(), space.getUsedCapacity(), capacityChange, newUsedCapacity);
    }

    /**
     * 更新储物空间信息
     *
     * @param spaceBo 储物空间业务对象
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSpace(StorageSpaceBo spaceBo) {
        StorageSpace space = baseMapper.selectById(spaceBo.getId());
        if (space == null) {
            throw new ServiceException("储物空间不存在，无法更新");
        }

        StorageSpace newSpace = MapstructUtils.convert(spaceBo, StorageSpace.class);

        // 如果父空间变更，重新构建路径
        if (spaceBo.getParentId() != null && !spaceBo.getParentId().equals(space.getParentId())) {
            SpacePathInfo pathInfo = checkParentAndBuildPath(
                    spaceBo.getParentId(),
                    LoginHelper.getUserId()
            );
            newSpace.setFullPaths(pathInfo.getFullPaths());
            newSpace.setSortOrder(pathInfo.getSortOrder());
        }

        return baseMapper.updateById(newSpace);
    }

    /**
     * 删除储物空间
     *
     * @param spaceId 储物空间ID
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSpace(Long spaceId) {
        StorageSpace space = baseMapper.selectById(spaceId);
        if (space == null || !spacePermissionService.hasSpacePermission(space.getFullPaths(), LoginHelper.getUserId())) {
            throw new ServiceException("没有储物空间的访问权限，无法删除");
        }
        return baseMapper.deleteById(spaceId);
    }


    @Override
    public boolean isSpaceExist(Long spaceId) {
        return baseMapper.exists(new LambdaQueryWrapper<StorageSpace>()
                .eq(StorageSpace::getId, spaceId));
    }

    /**
     * 检查父空间权限并构建完整路径
     */
    private SpacePathInfo checkParentAndBuildPath(Long parentId, Long currentUserId) {
        if (parentId == null) {
            return new SpacePathInfo(new ArrayList<>(), 0);
        }

        StorageSpace parentSpace = baseMapper.selectById(parentId);
        if (parentSpace == null) {
            throw new ServiceException("父空间不存在，无法创建子空间");
        }

        // 检查权限
        if (!spacePermissionService.hasSpacePermission(parentSpace.getFullPaths(), currentUserId)) {
            throw new ServiceException("没有父空间的访问权限，无法创建子空间");
        }

        // 构建完整路径
        List<Long> fullPaths = new ArrayList<>();
        if (!CollectionUtil.isEmpty(parentSpace.getFullPaths())) {
            fullPaths.addAll(parentSpace.getFullPaths());
        }
        fullPaths.add(parentId);

        return new SpacePathInfo(fullPaths, parentSpace.getSortOrder() + 1);
    }

    // 路径信息封装类
    @Getter
    @AllArgsConstructor
    private static class SpacePathInfo {
        private List<Long> fullPaths;
        private Integer sortOrder;
    }
}
