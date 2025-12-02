package org.gragon.storage.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.storage.domain.StorageSpace;

import java.io.Serial;
import java.util.List;

@Data
@AutoMapper(target = StorageSpace.class)
public class StorageSpaceVo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 存储空间ID
     */
    Long id;
    /**
     * 父存储空间ID
     */
    Long parentId;
    /**
     * 拥有者ID
     */
    Long ownerId;
    /**
     * 存储空间名称
     */
    String name;
    /**
     * 完整路径
     */
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
     * 图片列表
     */
    List<String> images;
    /**
     * 是否公开
     */
    String isPublic;

    List<String> fullPathNames;
}
