package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.util.List;

@Data
@TableName(value = "storage_space", autoResultMap = true)
public class StorageSpace extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 存储空间ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    Long id;
    /**
     * 父存储空间ID
     */
    Long parentId;
    /**
     * 存储空间名称
     */
    String name;
    /**
     * 完整路径
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    List<Long> fullPaths;
    /**
     * 存储空间描述
     */
    String description;
    /**
     * 已用容量
     */
    Integer usedCapacity;
    /**
     * 颜色
     */
    String color;
    /**
     * 图标
     */
    String icon;
    /**
     * 排序号
     */
    Integer sortOrder;
    /**
     * 图片列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    List<String> images;

    @TableLogic
    Boolean deleted;
}
