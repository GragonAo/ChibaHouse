package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.storage.domain.enums.UserSpacePermissionType;

import java.time.LocalDateTime;

@Data
@TableName("user_space_permissions")
public class UserSpacePermission extends BaseEntity {
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    LocalDateTime expireTime;

    public UserSpacePermission(Long userid, Long spaceid, UserSpacePermissionType permissionLevel, LocalDateTime expireTime) {
        this.userId = userid;
        this.spaceId = spaceid;
        this.permissionLevel = permissionLevel;
        this.expireTime = expireTime;
    }
}
