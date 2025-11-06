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
import org.gragon.storage.service.ItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {
    private final ItemService itemService;

    @GetMapping("/list")
    public TableDataInfo<ItemInfoVo> list(ItemBo bo, PageQuery pageQuery) {
        TableDataInfo<ItemVo> itemPageList = itemService.getItemPageList(bo, pageQuery);
        TableDataInfo<ItemInfoVo> itemPageInfo = new TableDataInfo<>();
        for (ItemVo item : itemPageList.getRows()) {
            // TODO 获取Item所属空间信息
            itemPageInfo.getRows().add(new ItemInfoVo(item, null));
        }
        return itemPageInfo;
    }

    @GetMapping("/{itemId}")
    public R<ItemInfoVo> getItem(@PathVariable(value = "itemId", required = true) Long id) {
        ItemVo itemVo = itemService.getItemById(id);
        // TODO 获取Item所属空间信息
        ItemInfoVo itemInfoVo = new ItemInfoVo(itemVo, null);
        return R.ok(itemInfoVo);
    }

    @PostMapping()
    public R<Void> addItem(@Validated @RequestBody ItemBo itemBo) {
        Long userId = LoginHelper.getUserId();
        itemBo.setCreateBy(userId);
        itemBo.setUpdateBy(userId);
        itemBo.setOwnerId(userId);
        return toAjax(itemService.insertItem(itemBo));
    }

    @DeleteMapping("/{itemId}")
    public R<Void> deleteItem(@PathVariable(value = "itemId", required = true) Long id) {
        // TODO 权限校验
        return toAjax(itemService.deleteItem(id));
    }

    @PutMapping()
    public R<Void> updateItem(@Validated @RequestBody ItemBo itemBo) {
        Long userId = LoginHelper.getUserId();
        // TODO 权限校验
        itemBo.setUpdateBy(userId);
        return toAjax(itemService.updateItem(itemBo));
    }
}
