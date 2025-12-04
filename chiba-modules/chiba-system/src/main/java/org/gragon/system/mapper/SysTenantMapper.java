package org.gragon.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.system.domain.SysTenant;
import org.gragon.system.domain.vo.SysTenantVo;

@Mapper
public interface SysTenantMapper extends BaseMapperPlus<SysTenant, SysTenantVo> {
}
