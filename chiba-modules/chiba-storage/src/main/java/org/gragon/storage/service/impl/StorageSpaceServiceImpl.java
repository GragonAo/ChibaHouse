package org.gragon.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.storage.domain.StorageSpace;
import org.gragon.storage.domain.UserSpacePermission;
import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.enums.UserSpacePermissionType;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.mapper.StorageSpaceMapper;
import org.gragon.storage.mapper.UserSpacePermissionMapper;
import org.gragon.storage.service.StorageSpaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageSpaceServiceImpl implements StorageSpaceService {
    private final StorageSpaceMapper baseMapper;
    private final UserSpacePermissionMapper permissionMapper;

    /**
     * 通过ID查询存储空间
     *
     * @param id 存储空间ID
     * @return 存储空间
     */
    @Override
    public StorageSpaceVo getSpaceById(Long id) {
        UserSpacePermissionType permissionType = getSpacePermissionType(id, LoginHelper.getUserId());
        if (permissionType != UserSpacePermissionType.READ_WRITE || permissionType != UserSpacePermissionType.ADMIN) {
            log.error("用户没有权限访问该存储空间，spaceId: {}, userId: {}, permissionType: {}", id, LoginHelper.getUserId(), permissionType);
            throw new ServiceException("用户没有权限访问该存储空间");
        }
        return baseMapper.selectVoById(id);
    }

    /**
     * 插入存储空间
     *
     * @param spaceBo 存储空间业务对象
     * @return 插入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSpace(StorageSpaceBo spaceBo) {
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);
        // TODO 生成 (条形码、二维码) 编码
        int rows = baseMapper.insert(space);
        permissionMapper.insert(new UserSpacePermission(space.getOwnerId(), space.getId(), UserSpacePermissionType.ADMIN, null));
        return rows;
    }

    /**
     * 更新存储空间
     *
     * @param spaceBo 存储空间业务对象
     * @return 更新结果
     */
    @Override
    public int updateSpace(StorageSpaceBo spaceBo) {
        UserSpacePermissionType permissionType = getSpacePermissionType(spaceBo.getId(), spaceBo.getOwnerId());
        if (permissionType != UserSpacePermissionType.READ_WRITE || permissionType != UserSpacePermissionType.ADMIN) {
            log.error("用户没有权限修改该存储空间，spaceId: {}, userId: {}, permissionType: {}", spaceBo.getId(), spaceBo.getOwnerId(), permissionType);
            throw new ServiceException("用户没有权限修改该存储空间");
        }
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);
        return baseMapper.updateById(space);
    }

    /**
     * 删除存储空间
     *
     * @param spaceId 存储空间ID
     * @return 删除结果
     */
    @Override
    public int deleteSpace(Long spaceId) {
        UserSpacePermissionType permissionType = getSpacePermissionType(spaceId, LoginHelper.getUserId());
        if (permissionType != UserSpacePermissionType.ADMIN) {
            log.error("用户没有权限删除该存储空间，spaceId: {}, userId: {}, permissionType: {}", spaceId, LoginHelper.getUserId(), permissionType);
            throw new ServiceException("用户没有权限删除该存储空间");
        }
        return baseMapper.deleteById(spaceId);
    }

    private UserSpacePermissionType getSpacePermissionType(long spaceId, long userId) {
        UserSpacePermission userSpacePermission = permissionMapper.selectOne(new LambdaQueryWrapper<UserSpacePermission>()
                .eq(UserSpacePermission::getSpaceId, spaceId)
                .eq(UserSpacePermission::getUserId, userId)
                .select(UserSpacePermission::getPermissionLevel));
        return userSpacePermission == null ? UserSpacePermissionType.NONE : userSpacePermission.getPermissionLevel();
    }


}
