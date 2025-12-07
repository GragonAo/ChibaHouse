package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.util.List;

@Data
@TableName("storage_space_permission")
public class SpacePermission extends BaseEntity {
    /**
     * 存储空间权限ID
     */
    Long id;
    /**
     * 存储空间ID
     */
    Long spaceId;

    /**
     * 拒绝用户列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    List<Long> denyUidList;
}
