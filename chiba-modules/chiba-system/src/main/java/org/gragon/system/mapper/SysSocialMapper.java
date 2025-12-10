package org.gragon.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.system.domain.SysSocial;
import org.gragon.system.domain.vo.SysSocialVo;

/**
 * 社会化关系Mapper接口
 *
 * @author thiszhc
 */
@Mapper
public interface SysSocialMapper extends BaseMapperPlus<SysSocial, SysSocialVo> {

}
