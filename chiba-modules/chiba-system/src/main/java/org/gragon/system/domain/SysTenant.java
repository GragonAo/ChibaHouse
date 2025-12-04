package org.gragon.system.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gragon.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

@Data
@NoArgsConstructor
@TableName("sys_tenant")
public class SysTenant extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
}
