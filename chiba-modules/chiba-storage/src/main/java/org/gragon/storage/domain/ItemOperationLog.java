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
    Long item_id;
    /**
     * 操作类型
     */
    String operation_type;
    /**
     * 旧物品数据(JSON格式)
     */
    String old_item_data;
    /**
     * 新物品数据(JSON格式)
     */
    String new_item_data;
}
