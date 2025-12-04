package org.gragon.system.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.system.domain.SysTenant;

import java.io.Serial;

@Data
@NoArgsConstructor
@AutoMapper(target = SysTenant.class)
public class SysTenantVo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
}
