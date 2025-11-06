package org.gragon.storage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.storage.domain.Item;
import org.gragon.storage.domain.vo.ItemVo;

@Mapper
public interface ItemMapper extends BaseMapperPlus<Item, ItemVo> {
}
