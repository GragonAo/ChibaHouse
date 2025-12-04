package org.gragon.system.service;

import org.gragon.system.domain.bo.SysTenantBo;

public interface SysTenantService {
    boolean insertTenant(SysTenantBo bo);

    boolean updateTenantName(Long tenantId, String tenantName);
}
