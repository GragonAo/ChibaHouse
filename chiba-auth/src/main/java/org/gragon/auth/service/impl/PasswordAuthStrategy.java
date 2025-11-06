package org.gragon.auth.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.gragon.auth.domain.vo.LoginVo;
import org.gragon.auth.form.PasswordLoginBody;
import org.gragon.auth.properties.CaptchaProperties;
import org.gragon.auth.service.AuthStrategy;
import org.gragon.auth.service.SysLoginService;
import org.gragon.common.core.constant.Constants;
import org.gragon.common.core.constant.GlobalConstants;
import org.gragon.common.core.enums.LoginType;
import org.gragon.common.core.exception.user.CaptchaException;
import org.gragon.common.core.exception.user.CaptchaExpireException;
import org.gragon.common.core.utils.MessageUtils;
import org.gragon.common.core.utils.StringUtils;
import org.gragon.common.core.utils.ValidatorUtils;
import org.gragon.common.json.utils.JsonUtils;
import org.gragon.common.redis.utils.RedisUtils;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.system.api.RemoteUserService;
import org.gragon.system.api.domain.vo.RemoteClientVo;
import org.gragon.system.api.model.LoginUser;
import org.springframework.stereotype.Service;

/**
 * 密码认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service("password" + AuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class PasswordAuthStrategy implements AuthStrategy {

    private final CaptchaProperties captchaProperties;

    private final SysLoginService loginService;

    @DubboReference
    private RemoteUserService remoteUserService;

    @Override
    public LoginVo login(String body, RemoteClientVo client) {
        PasswordLoginBody loginBody = JsonUtils.parseObject(body, PasswordLoginBody.class);
        ValidatorUtils.validate(loginBody);
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        String code = loginBody.getCode();
        String uuid = loginBody.getUuid();

        // 验证码开关
        if (captchaProperties.getEnabled()) {
            validateCaptcha(null, username, code, uuid);
        }
        LoginUser loginUser = remoteUserService.getUserInfo(username);
        loginService.checkLogin(LoginType.PASSWORD, username, () -> !BCrypt.checkpw(password, loginUser.getPassword()));

        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
        // 生成token
        LoginHelper.login(loginUser, model);

        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return loginVo;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private void validateCaptcha(String tenantId, String username, String code, String uuid) {
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.blankToDefault(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            loginService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            loginService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

}
