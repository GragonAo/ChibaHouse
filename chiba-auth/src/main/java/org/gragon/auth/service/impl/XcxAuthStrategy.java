//package org.gragon.auth.service.impl;
//
//import cn.dev33.satoken.stp.SaLoginModel;
//import cn.dev33.satoken.stp.StpUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.gragon.auth.domain.vo.LoginVo;
//import org.gragon.auth.form.XcxLoginBody;
//import org.gragon.auth.service.AuthStrategy;
//import org.gragon.common.core.utils.StringUtils;
//import org.gragon.common.core.utils.ValidatorUtils;
//import org.gragon.common.json.utils.JsonUtils;
//import org.gragon.common.satoken.utils.LoginHelper;
//import org.gragon.common.social.config.properties.SocialProperties;
//import org.gragon.common.social.domain.XcxAuth;
//import org.gragon.system.api.RemoteUserService;
//import org.gragon.system.api.domain.vo.RemoteClientVo;
//import org.gragon.system.api.model.XcxLoginUser;
//import org.springframework.stereotype.Service;
//
/// **
// * 邮件认证策略
// */
//@Service("xcx" + AuthStrategy.BASE_NAME)
//@RequiredArgsConstructor
//@Slf4j
//public class XcxAuthStrategy implements AuthStrategy {
//
//    private final SocialProperties socialProperties;
//
//    @DubboReference
//    private RemoteUserService remoteUserService;
//
//    @Override
//    public LoginVo login(String body, RemoteClientVo client) {
//        XcxLoginBody loginBody = JsonUtils.parseObject(body, XcxLoginBody.class);
//        ValidatorUtils.validate(loginBody);
//
//        String code = loginBody.getXcxCode();
//        String appid = loginBody.getAppid();
//        String phoneCode = loginBody.getPhoneCode();
//
//        XcxAuth xcxAuth = XcxUtils.loginAuth("wx_mini_app", code, appid, socialProperties);
//
//        // 3) 查用户，无则注册（需手机号）
//        XcxLoginUser loginUser = remoteUserService.getUserInfoByOpenid(openid);
//        if (loginUser == null) {
//            if (StringUtils.isBlank(phoneCode)) {
//                throw new IllegalArgumentException("首次登录需提供手机号或 phoneCode");
//            }
//            String mobile = decryptPhone(appid, appSecret, sessionKey, phoneCode);
//            if (StringUtils.isBlank(mobile)) throw new IllegalStateException("获取手机号失败");
//            loginUser = registerByPhoneAndOpenid(mobile, openid, unionid, appid);
//            if (loginUser == null) throw new IllegalStateException("注册用户失败");
//        }
//
//        // 4) 附加客户端信息
//        loginUser.setClientKey(client.getClientKey());
//        loginUser.setDeviceType(client.getDeviceType());
//
//        // 5) 发 token
//        SaLoginModel model = new SaLoginModel()
//                .setDevice(client.getDeviceType())
//                .setTimeout(client.getTimeout())
//                .setActiveTimeout(client.getActiveTimeout())
//                .setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
//        LoginHelper.login(loginUser, model);
//
//        LoginVo vo = new LoginVo();
//        vo.setAccessToken(StpUtil.getTokenValue());
//        vo.setExpireIn(StpUtil.getTokenTimeout());
//        vo.setClientId(client.getClientId());
//        vo.setOpenid(openid);
//        return vo;
//    }
//}
