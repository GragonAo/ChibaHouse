package org.gragon.storage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.storage.domain.ItemOperationLog;
import org.gragon.storage.domain.vo.ItemOperationLogVo;

@Mapper
public interface ItemOperationLogMapper extends BaseMapperPlus<ItemOperationLog, ItemOperationLogVo> {
}
