package org.gragon.storage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.storage.domain.StorageSpace;
import org.gragon.storage.domain.vo.StorageSpaceVo;

@Mapper
public interface StorageSpaceMapper extends BaseMapperPlus<StorageSpace, StorageSpaceVo> {
}
