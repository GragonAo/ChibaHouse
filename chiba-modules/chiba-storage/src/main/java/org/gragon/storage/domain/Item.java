package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物品对象 item
 */

@Data
@TableName(value = "items", autoResultMap = true)
public class Item extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 物品ID
     */
    Long id;
    /**
     * 拥有者ID
     */
    Long ownerId;
    /**
     * 存放空间ID
     */
    Long spaceId;
    /**
     * 分类ID
     */
    Long categoryId;
    /**
     * 物品名称
     */
    String name;
    /**
     * 物品描述
     */
    String description;
    /**
     * 数量
     */
    Float quantity;
    /**
     * 最小数量
     */
    Float minQuantity;
    /**
     * 单位
     */
    String unit;
    /**
     * 状态
     */
    String status;
    /**
     * 购买价格
     */
    Float purchasePrice;
    /**
     * 当前价值
     */
    Float currentValue;
    /**
     * 购买日期
     */
    LocalDateTime purchaseDate;
    /**
     * 序列号
     */
    Long serialNumber;
    /**
     * 型号
     */
    Long modelNumber;
    /**
     * 图片列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    List<String> images;
    /**
     * 文档列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    List<String> documents;
    /**
     * 重要等级
     */
    String importanceLevel;
    /**
     * 到期日期
     */
    LocalDateTime expiryDate;
    /**
     * 维护日期
     */
    LocalDateTime maintenanceDate;

    @TableLogic
    Integer deleted;
}
