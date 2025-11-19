package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

@Data
@TableName("space_permissions")
public class SpacePermission extends BaseEntity {

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
}
