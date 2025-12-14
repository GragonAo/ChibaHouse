package org.gragon.system.controller.system;

import lombok.RequiredArgsConstructor;
import org.gragon.common.core.domain.R;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.common.web.core.BaseController;
import org.gragon.system.domain.vo.SysSocialVo;
import org.gragon.system.service.ISysSocialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 社会化关系
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/social")
public class SysSocialController extends BaseController {

    private final ISysSocialService socialUserService;

    /**
     * 查询社会化关系列表
     */
    @GetMapping("/list")
    public R<List<SysSocialVo>> list() {
        return R.ok(socialUserService.queryListByUserId(LoginHelper.getUserId()));
    }

}
