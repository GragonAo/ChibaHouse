package org.gragon.auth.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.gragon.auth.domain.vo.LoginVo;
import org.gragon.auth.form.RegisterBody;
import org.gragon.auth.form.SocialLoginBody;
import org.gragon.auth.service.IAuthStrategy;
import org.gragon.auth.service.SysLoginService;
import org.gragon.common.core.constant.UserConstants;
import org.gragon.common.core.domain.R;
import org.gragon.common.core.domain.model.LoginBody;
import org.gragon.common.core.utils.*;
import org.gragon.common.encrypt.annotation.ApiEncrypt;
import org.gragon.common.json.utils.JsonUtils;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.common.social.config.properties.SocialLoginConfigProperties;
import org.gragon.common.social.config.properties.SocialProperties;
import org.gragon.common.social.utils.SocialUtils;
import org.gragon.system.api.RemoteClientService;
import org.gragon.system.api.domain.vo.RemoteClientVo;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * token 控制
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final SocialProperties socialProperties;
    private final SysLoginService sysLoginService;

    @DubboReference
    private final RemoteClientService remoteClientService;
    /**
     * 登录方法
     *
     * @param body 登录信息
     * @return 结果
     */
//    @ApiEncrypt
    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody String body) {
        LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
        ValidatorUtils.validate(loginBody);
        // 授权类型和客户端id
        String clientId = loginBody.getClientId();
        String grantType = loginBody.getGrantType();
        RemoteClientVo clientVo = remoteClientService.queryByClientId(clientId);

        // 查询不到 client 或 client 内不包含 grantType
        if (ObjectUtil.isNull(clientVo) || !StringUtils.contains(clientVo.getGrantType(), grantType)) {
            log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
            return R.fail(MessageUtils.message("auth.grant.type.error"));
        } else if (!UserConstants.NORMAL.equals(clientVo.getStatus())) {
            return R.fail(MessageUtils.message("auth.grant.type.blocked"));
        }
        // 登录
        LoginVo loginVo = IAuthStrategy.login(body, clientVo, grantType);

        Long userId = LoginHelper.getUserId();
        return R.ok(loginVo);
    }

    /**
     * 第三方登录请求
     *
     * @param source 登录来源
     * @return 结果
     */
//    @GetMapping("/binding/{source}")
//    public R<String> authBinding(@PathVariable("source") String source,
//                                 @RequestParam String tenantId, @RequestParam String domain) {
//        SocialLoginConfigProperties obj = socialProperties.getType().get(source);
//        if (ObjectUtil.isNull(obj)) {
//            return R.fail(source + "平台账号暂不支持");
//        }
//        AuthRequest authRequest = SocialUtils.getAuthRequest(source, socialProperties);
//        Map<String, String> map = new HashMap<>();
//        map.put("tenantId", tenantId);
//        map.put("domain", domain);
//        map.put("state", AuthStateUtils.createState());
//        String authorizeUrl = authRequest.authorize(Base64.encode(JsonUtils.toJsonString(map), StandardCharsets.UTF_8));
//        return R.ok("操作成功", authorizeUrl);
//    }

    /**
     * 第三方登录回调业务处理 绑定授权
     *
     * @param loginBody 请求体
     * @return 结果
     */
//    @PostMapping("/social/callback")
//    public R<Void> socialCallback(@RequestBody SocialLoginBody loginBody) {
//        // 获取第三方登录信息
//        AuthResponse<AuthUser> response = SocialUtils.loginAuth(
//            loginBody.getSource(), loginBody.getSocialCode(),
//            loginBody.getSocialState(), socialProperties);
//        AuthUser authUserData = response.getData();
//        // 判断授权响应是否成功
//        if (!response.ok()) {
//            return R.fail(response.getMsg());
//        }
//        sysLoginService.socialRegister(authUserData);
//        return R.ok();
//    }


    /**
     * 取消授权
     *
     * @param socialId socialId
     */
//    @DeleteMapping(value = "/unlock/{socialId}")
//    public R<Void> unlockSocial(@PathVariable Long socialId) {
//        Boolean rows = remoteSocialService.deleteWithValidById(socialId);
//        return rows ? R.ok() : R.fail("取消授权失败");
//    }

    /**
     * 登出方法
     */
    @PostMapping("logout")
    public R<Void> logout() {
        sysLoginService.logout();
        return R.ok();
    }

    /**
     * 用户注册
     */
//    @ApiEncrypt
    @PostMapping("register")
    public R<Void> register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok();
    }
}
