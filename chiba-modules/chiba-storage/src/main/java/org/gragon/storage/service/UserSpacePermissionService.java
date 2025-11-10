package org.gragon.storage.service;

import org.gragon.storage.domain.bo.UserSpacePermissionBo;
import org.gragon.storage.domain.enums.UserSpacePermissionType;

import java.util.List;

public interface UserSpacePermissionService {
    boolean checkPermission(Long spaceId, Long userId, List<UserSpacePermissionType> requiredList);

    void assignDefaultPermissions(UserSpacePermissionBo permissionBo);
}
