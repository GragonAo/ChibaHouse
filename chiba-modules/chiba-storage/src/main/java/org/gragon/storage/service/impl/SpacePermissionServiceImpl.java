package org.gragon.storage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.storage.domain.SpacePermission;
import org.gragon.storage.mapper.SpacePermissionMapper;
import org.gragon.storage.service.SpacePermissionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SpacePermissionServiceImpl implements SpacePermissionService {
    private final SpacePermissionMapper baseMapper;

    /**
     * 判断用户是否有空间访问权限
     *
     * @param spaceTreeNodeList 空间路径节点列表（从根空间到目标空间的路径）
     * @param userId            用户ID
     * @return true 有权限，false 无权限
     */
    @Override
    public boolean hasSpacePermission(List<Long> spaceTreeNodeList, Long userId) {
        if (userId == null) {
            return false;
        }
        if (CollectionUtil.isEmpty(spaceTreeNodeList)) {
            return true; // 没有空间路径，默认有权限,比如访问根空间
        }
        Map<Long, List<Long>> spaceAllDenyUidList = getSpaceAllDenyUidList();
        // 从下往上检查路径（从子空间到父空间）
        for (int i = spaceTreeNodeList.size() - 1; i >= 0; i--) {
            Long spaceId = spaceTreeNodeList.get(i);
            List<Long> denyUidList = spaceAllDenyUidList.get(spaceId);

            // 如果这个空间有拒绝列表，并且包含当前用户，则没有权限
            if (denyUidList != null && denyUidList.contains(userId)) {
                return false; // 被拒绝，没有权限
            }
        }

        return true; // 没有被任何空间拒绝，有权限
    }

    /**
     * 获取用户被拒绝访问的所有空间ID列表
     *
     * @param userId 用户ID
     * @return 被拒绝访问的空间ID列表
     */
    @Override
    public List<Long> getSpaceAllDenyUidList(Long userId) {
        Map<Long, List<Long>> spaceAllDenyUidList = getSpaceAllDenyUidList();
        return spaceAllDenyUidList.entrySet().stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().contains(userId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有空间的拒绝访问用户列表
     *
     * @return Map<空间ID, 拒绝访问用户ID列表>
     */
    private Map<Long, List<Long>> getSpaceAllDenyUidList() {
        List<SpacePermission> spacePermissions = baseMapper.selectList(
                new LambdaQueryWrapper<SpacePermission>()
                        .select(SpacePermission::getSpaceId, SpacePermission::getDenyUidList)
        );

        if (CollectionUtil.isEmpty(spacePermissions)) {
            return new HashMap<>();
        }

        return spacePermissions.stream()
                .filter(permission -> permission.getId() != null && permission.getDenyUidList() != null)
                .collect(Collectors.toMap(
                        SpacePermission::getSpaceId,
                        SpacePermission::getDenyUidList
                ));
    }
}
