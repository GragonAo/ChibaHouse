package org.gragon.storage.service.impl;

import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.storage.domain.StorageSpace;
import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.mapper.StorageSpaceMapper;
import org.gragon.storage.service.StorageSpaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageSpaceServiceImpl implements StorageSpaceService {

    private final StorageSpaceMapper baseMapper;

    /**
     * 通过储物空间ID获取储物空间信息
     *
     * @param id 储物空间ID
     * @return 储物空间信息
     */
    public StorageSpaceVo getStorageSpaceById(Long id) {
        StorageSpaceVo spaceVo = baseMapper.selectVoById(id);
        if (spaceVo != null) {
            spaceVo.setFullPathNames(this.getSpaceFullPathNameList(spaceVo.getFullPaths()));
        }
        return spaceVo;
    }
    

    private List<String> getSpaceFullPathNameList(List<Long> spaceIdList) {
        if (spaceIdList == null || spaceIdList.isEmpty()) {
            return new ArrayList<>();
        }
        List<StorageSpace> spaceList = baseMapper.selectList(new LambdaQueryWrapper<StorageSpace>()
                .in(StorageSpace::getId, spaceIdList)
                .orderByAsc(StorageSpace::getSortOrder)
        );
        return spaceList.stream().map(StorageSpace::getName).toList();
    }

    /**
     * 创建新的储物空间
     *
     * @param spaceBo 储物空间业务对象
     * @return 创建结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertSpace(StorageSpaceBo spaceBo) {
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);
        if (spaceBo.getParentId() != null) {
            StorageSpace parentSpace = baseMapper.selectById(spaceBo.getParentId());
            List<Long> parentIdList = new ArrayList<>(parentSpace.getFullPaths());
            parentIdList.add(spaceBo.getParentId());
            space.setFullPaths(parentIdList);
            space.setSortOrder(parentSpace.getSortOrder() + 1);
        }
        int rows = baseMapper.insert(space);
        this.updateSpaceUsedCapacity(space.getParentId(), rows);
        return rows;
    }

    @Lock4j(keys = "#spaceId")
    public void updateSpaceUsedCapacity(Long spaceId, Integer capacityChange) {
        StorageSpace space = baseMapper.selectById(spaceId);
        if (space == null || capacityChange == 0) {
            return;
        }

        // 计算新的容量
        Integer newUsedCapacity = space.getUsedCapacity() + capacityChange;

        // 校验容量不能为负数
        if (newUsedCapacity < 0) {
            throw new ServiceException(
                    String.format("容量不足，当前容量：%d，变更量：%d",
                            space.getUsedCapacity(), capacityChange)
            );
        }

        // 更新容量
        StorageSpace updateEntity = new StorageSpace();
        updateEntity.setId(spaceId);
        updateEntity.setUsedCapacity(newUsedCapacity);

        int result = baseMapper.updateById(updateEntity);
        if (result == 0) {
            throw new ServiceException("更新存储空间容量失败");
        }

        // 可以添加日志记录
        log.info("存储空间容量更新成功，空间ID：{}，原容量：{}，变更量：{}，新容量：{}",
                spaceId, space.getUsedCapacity(), capacityChange, newUsedCapacity);
    }


    /**
     * 更新储物空间信息
     *
     * @param spaceBo 储物空间业务对象
     * @return 更新结果
     */
    public int updateSpace(StorageSpaceBo spaceBo) {
        StorageSpace space = MapstructUtils.convert(spaceBo, StorageSpace.class);
        return baseMapper.updateById(space);
    }

    public int deleteSpace(Long spaceId) {
        return baseMapper.deleteById(spaceId);
    }

    @Override
    public boolean isSpaceExist(Long spaceId) {
        return baseMapper.exists(new LambdaQueryWrapper<StorageSpace>()
                .eq(StorageSpace::getId, spaceId));
    }
}
