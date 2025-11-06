package org.gragon.storage.service;

import org.gragon.common.mybatis.core.page.PageQuery;
import org.gragon.common.mybatis.core.page.TableDataInfo;
import org.gragon.storage.domain.bo.ItemBo;
import org.gragon.storage.domain.vo.ItemVo;

public interface ItemService {
    ItemVo getItemById(Long id);

    TableDataInfo<ItemVo> getItemPageList(ItemBo itemBo, PageQuery pageQuery);

    int insertItem(ItemBo itemBo);

    int updateItem(ItemBo itemBo);

    int deleteItem(Long id);
}
