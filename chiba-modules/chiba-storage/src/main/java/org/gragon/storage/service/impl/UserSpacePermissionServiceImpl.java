package org.gragon.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.storage.domain.UserSpacePermission;
import org.gragon.storage.domain.bo.UserSpacePermissionBo;
import org.gragon.storage.domain.enums.UserSpacePermissionType;
import org.gragon.storage.mapper.UserSpacePermissionMapper;
import org.gragon.storage.service.UserSpacePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSpacePermissionServiceImpl implements UserSpacePermissionService {

    private final UserSpacePermissionMapper baseMapper;

    /**
     * 获取储物空间的所有权限记录
     *
     * @param spaceId 储物空间ID
     * @return
     */
    private List<UserSpacePermission> queryPermissionsBySpaceId(Long spaceId) {
        return baseMapper.selectList(new LambdaQueryWrapper<UserSpacePermission>()
                .eq(UserSpacePermission::getSpaceId, spaceId)
        );
    }

    /**
     * 为新创建的储物空间分配默认权限
     *
     * @param spaceId 储物空间ID
     */
    public void assignDefaultPermissions(UserSpacePermissionBo permissionBo) {
        // 如果有父空间，则继承父空间的权限
        if (permissionBo.getParentSpaceId() != null) {
            List<UserSpacePermission> parentPermissions = this.queryPermissionsBySpaceId(permissionBo.getParentSpaceId());
            for (UserSpacePermission parentPermission : parentPermissions) {
                this.addPermission(permissionBo);
            }
            return;
        }
        UserSpacePermission permission = new UserSpacePermission();
        permission.setSpaceId(spaceId);
        permission.setUserId(userId);
        permission.setPermissionLevel(UserSpacePermissionType.ADMIN);
        baseMapper.insert(permission);
    }

    /**
     * 为用户分配储物空间权限
     *
     * @param spaceId        存储空间ID
     * @param userId         用户ID
     * @param permissionType 权限类型
     * @return 分配结果
     */
    public int addPermission(UserSpacePermissionBo permissionBo) {
        if (!this.checkPermission(permissionBo.getParentSpaceId(), LoginHelper.getUserId(),
                List.of(UserSpacePermissionType.ADMIN))) {
            throw new ServiceException("没有权限分配该储物空间的权限");
        }
        UserSpacePermission permission = new UserSpacePermission();
        permission.setSpaceId(spaceId);
        permission.setUserId(userId);
        permission.setPermissionLevel(permissionType);
        return baseMapper.insert(permission);
    }

    /**
     * 检查用户对储物空间的权限
     *
     * @param spaceId      存储空间ID
     * @param userId       用户ID
     * @param requiredList 所需权限列表
     * @return 是否具有所需权限
     */
    public boolean checkPermission(Long spaceId, Long userId, List<UserSpacePermissionType> requiredList) {
        UserSpacePermission permission = baseMapper.selectOne(new LambdaQueryWrapper<UserSpacePermission>()
                .eq(UserSpacePermission::getSpaceId, spaceId)
                .eq(UserSpacePermission::getUserId, userId)
                .select(UserSpacePermission::getPermissionLevel)
        );
        return permission != null && requiredList.contains(permission.getPermissionLevel());
    }
}
