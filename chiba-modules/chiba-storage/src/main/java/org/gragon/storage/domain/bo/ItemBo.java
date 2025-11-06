package org.gragon.storage.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.storage.domain.Item;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Item.class, reverseConvertGenerate = false)
/**
 * 物品对象 item
 */
public class ItemBo extends BaseEntity {

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    LocalDateTime purchaseDate;
    /**
     * 图片列表
     */
    List<String> images;
    /**
     * 文档列表
     */
    List<String> documents;
    /**
     * 重要等级
     */
    String importanceLevel;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    LocalDateTime  expiryDate;
    /**
     * 维护日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    LocalDateTime  maintenanceDate;
}
