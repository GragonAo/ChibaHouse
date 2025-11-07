package org.gragon.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.gragon.storage.domain.UserSpacePermission;

@Mapper
public interface UserSpacePermissionMapper extends BaseMapper<UserSpacePermission> {
}
