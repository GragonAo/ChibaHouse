package org.gragon.system.controller.system;

import lombok.RequiredArgsConstructor;
import org.gragon.common.core.validate.EditGroup;
import org.gragon.system.domain.bo.SysTenantBo;
import org.gragon.system.service.SysTenantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
