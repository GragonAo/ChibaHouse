package org.gragon.storage.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gragon.common.core.validate.AddGroup;
import org.gragon.common.core.validate.EditGroup;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.storage.domain.StorageSpace;

import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = StorageSpace.class, reverseConvertGenerate = false)
public class StorageSpaceBo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 存储空间ID
     */
    @NotNull(groups = EditGroup.class, message = "{storage.space.id.not.null}")
    Long id;
    /**
     * 父存储空间ID
     */
    @NotNull(groups = AddGroup.class, message = "{storage.space.parent-id.not.null}")
    Long parentId;
    /**
     * 存储空间名称
     */
    @NotBlank(groups = AddGroup.class, message = "{storage.space.name.not.blank}")
    String name;
    /**
     * 存储空间描述
     */
    String description;
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
}
