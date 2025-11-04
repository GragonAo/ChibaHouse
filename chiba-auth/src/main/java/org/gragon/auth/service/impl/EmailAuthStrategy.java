//package org.gragon.auth.service.impl;
//
//import cn.dev33.satoken.stp.SaLoginModel;
//import cn.dev33.satoken.stp.StpUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.gragon.auth.domain.vo.LoginVo;
//import org.gragon.auth.form.EmailLoginBody;
//import org.gragon.auth.service.IAuthStrategy;
//import org.gragon.auth.service.SysLoginService;
//import org.gragon.common.core.constant.Constants;
//import org.gragon.common.core.constant.GlobalConstants;
//import org.gragon.common.core.enums.LoginType;
//import org.gragon.common.core.exception.user.CaptchaExpireException;
//import org.gragon.common.core.utils.MessageUtils;
//import org.gragon.common.core.utils.StringUtils;
//import org.gragon.common.core.utils.ValidatorUtils;
//import org.gragon.common.json.utils.JsonUtils;
//import org.gragon.common.redis.utils.RedisUtils;
//import org.gragon.common.satoken.utils.LoginHelper;
//import org.gragon.common.tenant.helper.TenantHelper;
//import org.gragon.system.api.RemoteUserService;
//import org.gragon.system.api.domain.vo.RemoteClientVo;
//import org.gragon.system.api.model.LoginUser;
//import org.springframework.stereotype.Service;
//
///**
// * 邮件认证策略
// *
// * @author Michelle.Chung
// */
//@Slf4j
//@Service("email" + IAuthStrategy.BASE_NAME)
//@RequiredArgsConstructor
//public class EmailAuthStrategy implements IAuthStrategy {
//
//    private final SysLoginService loginService;
//
//    @DubboReference
//    private RemoteUserService remoteUserService;
//
//    @Override
//    public LoginVo login(String body, RemoteClientVo client) {
//        EmailLoginBody loginBody = JsonUtils.parseObject(body, EmailLoginBody.class);
//        ValidatorUtils.validate(loginBody);
//        String tenantId = loginBody.getTenantId();
//        String email = loginBody.getEmail();
//        String emailCode = loginBody.getEmailCode();
//        LoginUser loginUser = TenantHelper.dynamic(tenantId, () -> {
//            LoginUser user = remoteUserService.getUserInfoByEmail(email, tenantId);
//            loginService.checkLogin(LoginType.EMAIL, tenantId, user.getUsername(), () -> !validateEmailCode(tenantId, email, emailCode));
//            return user;
//        });
//        loginUser.setClientKey(client.getClientKey());
//        loginUser.setDeviceType(client.getDeviceType());
//        SaLoginModel model = new SaLoginModel();
//        model.setDevice(client.getDeviceType());
//        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
//        // 例如: 后台用户30分钟过期 app用户1天过期
//        model.setTimeout(client.getTimeout());
//        model.setActiveTimeout(client.getActiveTimeout());
//        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
//        // 生成token
//        LoginHelper.login(loginUser, model);
//
//        LoginVo loginVo = new LoginVo();
//        loginVo.setAccessToken(StpUtil.getTokenValue());
//        loginVo.setExpireIn(StpUtil.getTokenTimeout());
//        loginVo.setClientId(client.getClientId());
//        return loginVo;
//    }
//
//    /**
//     * 校验邮箱验证码
//     */
//    private boolean validateEmailCode(String tenantId, String email, String emailCode) {
//        String code = RedisUtils.getCacheObject(GlobalConstants.CAPTCHA_CODE_KEY + email);
//        if (StringUtils.isBlank(code)) {
//            loginService.recordLogininfor(tenantId, email, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
//            throw new CaptchaExpireException();
//        }
//        return code.equals(emailCode);
//    }
//
//}
