package org.gragon.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.gragon.common.core.validate.EditGroup;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.system.domain.SysTenant;

import java.io.Serial;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysTenant.class, reverseConvertGenerate = false)
public class SysTenantBo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotNull(groups = EditGroup.class, message = "租户ID不能为空")
    Long id;
    String name;
}
