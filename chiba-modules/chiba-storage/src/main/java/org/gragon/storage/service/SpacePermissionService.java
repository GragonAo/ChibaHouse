package org.gragon.storage.service;

import java.util.List;

public interface SpacePermissionService {
    List<Long> getAccessibleSpaceIds(Long userId);

    boolean hasSpacePermission(Long spaceId, Long userId);
}
