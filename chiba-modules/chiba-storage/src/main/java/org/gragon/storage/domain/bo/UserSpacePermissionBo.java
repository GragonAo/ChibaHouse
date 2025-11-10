package org.gragon.storage.domain.bo;


import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.storage.domain.UserSpacePermission;
import org.gragon.storage.domain.enums.UserSpacePermissionType;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = UserSpacePermission.class, reverseConvertGenerate = false)
public class UserSpacePermissionBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户空间权限ID
     */
    Long id;
    /**
     * 用户ID
     */
    Long userId;
    /**
     * 空间ID
     */
    Long spaceId;

    /**
     * 父空间ID
     */
    Long parentSpaceId;

    /**
     * 权限级别
     */
    UserSpacePermissionType permissionLevel;
}
