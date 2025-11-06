package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.util.List;

@Data
@TableName("storage_spaces")
public class StorageSpace extends BaseEntity {

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
     * 存储空间编码
     */
    String spaceCode;
    /**
     * 存储空间名称
     */
    String name;
    /**
     * 完整路径
     */
    String fullPath;
    /**
     * 存储空间描述
     */
    String description;
    /**
     * 存储空间类型
     */
    String spaceType;
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
     * 二维码URL
     */
    String qrCodeUrl;
    /**
     * 图片列表
     */
    List<String> images;
    /**
     * 自定义字段
     */
    Object customFields;
    /**
     * 是否公开
     */
    String isPublic;
    /**
     * 状态
     */
    String status;
}
