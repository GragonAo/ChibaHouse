package org.gragon.storage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

@Data
@TableName("item_operation_logs")
public class ItemOperationLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 物品操作日志ID
     */
    Long id;
    /**
     * 物品ID
     */
    Long itemId;
    /**
     * 操作类型
     */
    String operationType;
    /**
     * 旧物品数据(JSON格式)
     */
    String oldItemData;
    /**
     * 新物品数据(JSON格式)
     */
    String newItemData;
}
