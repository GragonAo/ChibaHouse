package org.gragon.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.storage.domain.SpacePermission;
import org.gragon.storage.mapper.SpacePermissionMapper;
import org.gragon.storage.service.SpacePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SpacePermissionServiceImpl implements SpacePermissionService {
    private final SpacePermissionMapper baseMapper;

    @Override
    public List<Long> getAccessibleSpaceIds(Long userId) {
        return baseMapper.selectList(new LambdaQueryWrapper<SpacePermission>()
                .eq(SpacePermission::getUserId, userId)
        ).stream().map(SpacePermission::getSpaceId).collect(Collectors.toList());
    }

    @Override
    public boolean hasSpacePermission(Long spaceId, Long userId) {
        return baseMapper.exists(new LambdaQueryWrapper<SpacePermission>()
                .eq(SpacePermission::getSpaceId, spaceId)
                .eq(SpacePermission::getUserId, userId)
        );
    }
}
