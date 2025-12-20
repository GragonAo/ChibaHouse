package org.gragon.system.domain.convert;

import io.github.linpeilie.BaseMapper;
import org.gragon.system.api.domain.vo.RemoteSocialVo;
import org.gragon.system.domain.vo.SysSocialVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * 社交数据转换器
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysSocialVoConvert extends BaseMapper<SysSocialVo, RemoteSocialVo> {
}
