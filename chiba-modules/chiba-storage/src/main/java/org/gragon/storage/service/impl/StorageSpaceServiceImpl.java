package org.gragon.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.storage.domain.StorageSpace;
import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.bo.UserSpacePermissionBo;
import org.gragon.storage.domain.enums.UserSpacePermissionType;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.mapper.StorageSpaceMapper;
import org.gragon.storage.service.StorageSpaceService;
import org.gragon.storage.service.UserSpacePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageSpaceServiceImpl implements StorageSpaceService {

    private final StorageSpaceMapper baseMapper;

    private final UserSpacePermissionService permissionService;

    /**
     * 通过储物空间ID获取储物空间信息
     *
     * @param id 储物空间ID
     * @return 储物空间信息
     */
    public StorageSpaceVo getStorageSpaceById(Long id) {
        if (!permissionService.checkPermission(id, LoginHelper.getUserId(),
                List.of(UserSpacePermissionType.REDE_ONLY, UserSpacePermissionType.READ_WRITE,
                        UserSpacePermissionType.ADMIN))) {
            throw new ServiceException("没有权限访问该储物空间");
        }
        return baseMapper.selectVoById(id);
    }

    /**
     * 创建新的储物空间
     *
     * @param spaceBo 储物空间业务对象
     * @return 创建结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertSpace(StorageSpaceBo spaceBo) {
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);
        if (spaceBo.getParentId() != null) {
            StorageSpace parentSpace = baseMapper.selectById(spaceBo.getParentId());
            if (parentSpace == null) {
                throw new ServiceException("父储物空间不存在");
            }
            List<Long> fullPaths = Optional.ofNullable(parentSpace.getFullPaths())
                    .map(ArrayList::new)
                    .orElseGet(ArrayList::new);
            fullPaths.add(parentSpace.getId());
            space.setFullPaths(fullPaths);
        }
        int rows = baseMapper.insert(space);
        permissionService.assignDefaultPermissions(new UserSpacePermissionBo());
        return rows;
    }


    /**
     * 更新储物空间信息
     *
     * @param spaceBo 储物空间业务对象
     * @return 更新结果
     */
    public int updateSpace(StorageSpaceBo spaceBo) {
        // 检查用户是否有权限修改该储物空间
        if (!permissionService.checkPermission(spaceBo.getId(), LoginHelper.getUserId(),
                List.of(UserSpacePermissionType.ADMIN, UserSpacePermissionType.READ_WRITE))) {
            throw new ServiceException("没有权限修改该储物空间");
        }
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);
        return baseMapper.updateById(space);
    }

    public int deleteSpace(Long spaceId) {
        // 检查用户是否有权限删除该储物空间
        if (!permissionService.checkPermission(spaceId, LoginHelper.getUserId(),
                List.of(UserSpacePermissionType.ADMIN))) {
            throw new ServiceException("没有权限删除该储物空间");
        }
        return baseMapper.deleteById(spaceId);
    }

    @Override
    public boolean isSpaceExist(Long spaceId) {
        return baseMapper.exists(new LambdaQueryWrapper<StorageSpace>()
                .eq(StorageSpace::getId, spaceId));
    }
}
