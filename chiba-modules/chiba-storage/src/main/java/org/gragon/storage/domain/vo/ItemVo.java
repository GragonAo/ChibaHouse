package org.gragon.storage.domain.vo;

import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

@Data
/**
 * 物品对象 item
 */
public class ItemVo extends BaseEntity {

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
     * 物品编码
     */
    String itemCode;
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
    Float purchaseDate;
    /**
     * 条形码
     */
    String barcode;
    /**
     * 二维码链接
     */
    String qrCodeUrl;
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
    List<String> images;
    /**
     * 文档列表
     */
    List<String> documents;
    /**
     * 自定义字段
     */
    Object customFields;
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
}
