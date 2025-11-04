package org.gragon.system.domain.convert;

import io.github.linpeilie.BaseMapper;
import org.gragon.system.api.domain.bo.RemoteUserBo;
import org.gragon.system.domain.bo.SysUserBo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * 用户信息转换器
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserBoConvert extends BaseMapper<RemoteUserBo, SysUserBo> {

    /**
     * RemoteUserBoToSysUserBo
     * @param remoteUserBo 待转换对象
     * @return 转换后对象
     */
    SysUserBo convert(RemoteUserBo remoteUserBo);
}
