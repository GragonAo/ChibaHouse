package org.gragon.storage.controller;

import lombok.RequiredArgsConstructor;
import org.gragon.common.core.domain.R;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.common.web.core.BaseController;
import org.gragon.storage.domain.bo.StorageSpaceBo;
import org.gragon.storage.domain.vo.StorageSpaceVo;
import org.gragon.storage.service.StorageSpaceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
public class StorageSpaceController extends BaseController {
    private final StorageSpaceService storageSpaceService;

    @GetMapping("/{spaceId}")
    public R<StorageSpaceVo> getSpace(@RequestParam("spaceId") final String spaceId) {
        StorageSpaceVo spaceVo = storageSpaceService.getStorageSpaceById(Long.valueOf(spaceId));
        return R.ok(spaceVo);
    }

    /**
     * 添加储物空间
     *
     * @param spaceBo 储物空间业务对象
     * @return 操作结果
     */
    @PostMapping
    public R<Void> addSpace(@Validated @RequestBody StorageSpaceBo spaceBo) {
        Long userId = LoginHelper.getLoginUser().getUserId();
        spaceBo.setOwnerId(userId);
        return toAjax(storageSpaceService.insertSpace(spaceBo));
    }

    @PutMapping
    public R<Void> updateSpace(@Validated @RequestBody StorageSpaceBo spaceBo) {
        Long userId = LoginHelper.getLoginUser().getUserId();
        spaceBo.setOwnerId(userId);
        return toAjax(storageSpaceService.updateSpace(spaceBo));
    }

    @DeleteMapping({"/{spaceId}"})
    public R<Void> deleteSpace(@PathVariable("spaceId") Long spaceId) {
        return toAjax(storageSpaceService.deleteSpace(spaceId));
    }

}
