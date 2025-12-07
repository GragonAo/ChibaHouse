package org.gragon.storage.service;

import java.util.List;

public interface SpacePermissionService {
    boolean hasSpacePermission(List<Long> spaceTreeNodeList, Long userId);

    List<Long> getSpaceAllDenyUidList(Long userId);
}
