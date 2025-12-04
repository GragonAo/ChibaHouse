package org.gragon.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.system.domain.SysTenant;
import org.gragon.system.domain.bo.SysTenantBo;
import org.gragon.system.mapper.SysTenantMapper;
import org.gragon.system.service.SysTenantService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SysTenantServiceImpl implements SysTenantService {
    private final SysTenantMapper baseMapper;

    public boolean insertTenant(SysTenantBo bo) {
        SysTenant add = MapstructUtils.convert(bo, SysTenant.class);
        return baseMapper.insert(add) > 0;
    }

    @Override
    public boolean updateTenantName(Long tenantId, String tenantName) {
        return baseMapper.update(new LambdaUpdateWrapper<SysTenant>()
                .eq(SysTenant::getId, tenantId)
                .set(SysTenant::getName, tenantName)) > 0;
    }
}
