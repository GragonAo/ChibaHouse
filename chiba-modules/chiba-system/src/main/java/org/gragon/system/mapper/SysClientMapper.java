package org.gragon.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.system.domain.SysClient;
import org.gragon.system.domain.vo.SysClientVo;

/**
 * 授权管理Mapper接口
 */
@Mapper
public interface SysClientMapper extends BaseMapperPlus<SysClient, SysClientVo> {

}
