package org.gragon.storage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 物品对象信息
 */
@Data
@AllArgsConstructor
public class ItemInfoVo implements Serializable {
    ItemVo item;
    StorageSpaceVo storageSpace;
}
