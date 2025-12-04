package org.gragon.system.service;

import org.gragon.system.domain.bo.SysTenantBo;
import org.gragon.system.domain.vo.SysTenantVo;

public interface SysTenantService {
    SysTenantVo getTenantByUserId(Long userId);

    boolean insertTenant(SysTenantBo bo);

    boolean updateTenantName(Long tenantId, String tenantName);
}
