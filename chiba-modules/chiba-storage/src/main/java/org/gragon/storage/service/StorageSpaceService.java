package org.gragon.storage.service;

import org.gragon.common.mybatis.core.page.PageQuery;
import org.gragon.common.mybatis.core.page.TableDataInfo;
import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.vo.StorageSpaceVo;

import java.util.List;

public interface StorageSpaceService {

    TableDataInfo<StorageSpaceVo> getSpacePageList(StorageSpaceBo spaceBo, PageQuery pageQuery);

    List<Long> getAuthorizedSpaceList(Long userId);

    StorageSpaceVo getStorageSpaceById(Long id);

    long getRootSpace();

    int insertSpace(StorageSpaceBo spaceBo);

    int updateSpace(StorageSpaceBo spaceBo);

    int deleteSpace(Long spaceId);

    boolean isSpaceExist(Long spaceId);

    void updateSpaceUsedCapacity(Long spaceId, Integer capacityChange);

}
