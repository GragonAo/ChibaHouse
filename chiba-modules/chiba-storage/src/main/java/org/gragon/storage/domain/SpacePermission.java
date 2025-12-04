package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.storage.domain.enums.UserSpacePermissionType;

import java.time.LocalDateTime;

@Data
@TableName("storage_space_permission")
public class SpacePermission extends BaseEntity {
    /**
     * 用户存储空间权限ID
     */
    Long id;
    /**
     * 用户ID
     */
    Long userId;
    /**
     * 存储空间ID
     */
    Long spaceId;
    /**
     * 权限级别
     */
    UserSpacePermissionType permissionLevel;
    /**
     * 过期时间
     */
    LocalDateTime expireTime;

    Long tenantId;

    public SpacePermission(Long userid, Long spaceid, UserSpacePermissionType permissionLevel, LocalDateTime expireTime) {
        this.userId = userid;
        this.spaceId = spaceid;
        this.permissionLevel = permissionLevel;
        this.expireTime = expireTime;
    }
}
