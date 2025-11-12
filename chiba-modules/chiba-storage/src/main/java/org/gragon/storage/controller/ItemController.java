package org.gragon.storage.controller;

import lombok.RequiredArgsConstructor;
import org.gragon.common.core.domain.R;
import org.gragon.common.mybatis.core.page.PageQuery;
import org.gragon.common.mybatis.core.page.TableDataInfo;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.common.web.core.BaseController;
import org.gragon.storage.domain.bo.ItemBo;
import org.gragon.storage.domain.vo.ItemInfoVo;
import org.gragon.storage.domain.vo.ItemVo;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.service.ItemService;
import org.gragon.storage.service.StorageSpaceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {
    private final ItemService itemService;
    private final StorageSpaceService storageSpaceService;

    /**
     * 查询Item列表
     *
     * @param bo        Item查询对象
     * @param pageQuery 分页查询对象
     * @return Item分页数据
     */
    @GetMapping("/list")
    public TableDataInfo<ItemInfoVo> list(ItemBo bo, PageQuery pageQuery) {
        TableDataInfo<ItemVo> itemPageList = itemService.getItemPageList(bo, pageQuery);
        List<ItemInfoVo> itemInfoVoList = new ArrayList<>();
        for (ItemVo item : itemPageList.getRows()) {
            // TODO 获取Item所属空间信息
            itemInfoVoList.add(new ItemInfoVo(item, null));
        }
        return TableDataInfo.build(itemInfoVoList, itemPageList.getTotal());
    }

    /**
     * 获取Item详细信息
     *
     * @param id Item主键
     * @return Item详细信息
     */
    @GetMapping("/{itemId}")
    public R<ItemInfoVo> getItem(@PathVariable(value = "itemId", required = true) Long id) {
        ItemVo itemVo = itemService.getItemById(id);
        if (itemVo == null) {
            return R.fail("未找到对应物品信息");
        }
        StorageSpaceVo spaceVo = storageSpaceService.getStorageSpaceById(itemVo.getSpaceId());
        ItemInfoVo itemInfoVo = new ItemInfoVo(itemVo, spaceVo);
        return R.ok(itemInfoVo);
    }

    /**
     * 新增Item
     *
     * @param itemBo Item新增对象
     * @return 结果
     */
    @PostMapping()
    public R<Void> addItem(@Validated @RequestBody ItemBo itemBo) {
        Long userId = LoginHelper.getUserId();
        itemBo.setCreateBy(userId);
        itemBo.setUpdateBy(userId);
        itemBo.setOwnerId(userId);
        return toAjax(itemService.insertItem(itemBo));
    }

    /**
     * 删除Item
     *
     * @param id Item主键
     * @return 结果
     */
    @DeleteMapping("/{itemId}")
    public R<Void> deleteItem(@PathVariable(value = "itemId", required = true) Long id) {
        return toAjax(itemService.deleteItem(id));
    }

    /**
     * 修改Item
     *
     * @param itemBo Item修改对象
     * @return 结果
     */
    @PutMapping()
    public R<Void> updateItem(@Validated @RequestBody ItemBo itemBo) {
        Long userId = LoginHelper.getUserId();
        return toAjax(itemService.updateItem(itemBo));
    }
}
