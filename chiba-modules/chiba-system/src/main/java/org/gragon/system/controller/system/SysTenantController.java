package org.gragon.system.controller.system;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.gragon.common.core.domain.R;
import org.gragon.common.core.validate.EditGroup;
import org.gragon.common.tenant.helper.TenantHelper;
import org.gragon.system.domain.bo.SysTenantBo;
import org.gragon.system.service.SysTenantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant")
public class SysTenantController {

    private final SysTenantService sysTenantService;

    @PutMapping("/name")
    public boolean updateTenantName(@Validated(EditGroup.class) @RequestBody SysTenantBo bo) {
        return sysTenantService.updateTenantName(bo.getId(), bo.getName());
    }

    @GetMapping("/dynamic/{tenantId}")
    public R<Void> dynamicTenant(@NotBlank(message = "租户ID不能为空") @PathVariable String tenantId) {
        TenantHelper.setDynamic(tenantId, true);
        return R.ok();
    }
}
