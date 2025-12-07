package org.gragon.storage.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gragon.common.core.validate.AddGroup;
import org.gragon.common.core.validate.EditGroup;
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
    @NotNull(groups = EditGroup.class, message = "{storage.item.id.not.null}")
    Long id;

    /**
     * 存放空间ID
     */
    @NotNull(groups = AddGroup.class, message = "{storage.item.space-id.not.null}")
    Long spaceId;
    /**
     * 物品名称
     */
    @NotNull(groups = AddGroup.class, message = "{storage.item.space-id.not.null}")
    String name;
    /**
     * 物品描述
     */
    String description;
    /**
     * 数量
     */
    @NotNull(groups = AddGroup.class, message = "{storage.item.quantity.not.null}")
    Float quantity;
    /**
     * 单位
     */
    @NotBlank(groups = AddGroup.class, message = "{storage.item.unit.not.blank}")
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
     * 购买日期
     */
    @NotNull(groups = AddGroup.class, message = "{storage.item.purchase-date.not.null}")
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
    LocalDateTime expiryDate;
    /**
     * 维护日期
     */
    LocalDateTime maintenanceDate;
}
