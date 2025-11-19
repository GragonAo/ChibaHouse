package org.gragon.storage.service;

import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.vo.StorageSpaceVo;

public interface StorageSpaceService {
    StorageSpaceVo getStorageSpaceById(Long id);

    int insertSpace(StorageSpaceBo spaceBo);

    int updateSpace(StorageSpaceBo spaceBo);

    int deleteSpace(Long spaceId);

    boolean isSpaceExist(Long spaceId);
    
}
