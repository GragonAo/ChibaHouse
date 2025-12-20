package org.gragon.auth.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.gragon.auth.domain.vo.LoginVo;
import org.gragon.auth.form.SocialLoginBody;
import org.gragon.auth.service.AuthStrategy;
import org.gragon.auth.service.SysLoginService;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.ValidatorUtils;
import org.gragon.common.json.utils.JsonUtils;
import org.gragon.common.satoken.utils.LoginHelper;
import org.gragon.common.social.config.properties.SocialProperties;
import org.gragon.common.social.utils.SocialUtils;
import org.gragon.common.tenant.helper.TenantHelper;
import org.gragon.system.api.RemoteSocialService;
import org.gragon.system.api.RemoteUserService;
import org.gragon.system.api.domain.bo.RemoteUserBo;
import org.gragon.system.api.domain.vo.RemoteClientVo;
import org.gragon.system.api.domain.vo.RemoteSocialVo;
import org.gragon.system.api.model.LoginUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 第三方授权策略
 *
 * @author thiszhc is 三三
 */
@Slf4j
@Service("social" + AuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SocialAuthStrategy implements AuthStrategy {

    private final SocialProperties socialProperties;
    private final SysLoginService sysLoginService;

    @DubboReference
    private RemoteSocialService remoteSocialService;
    @DubboReference
    private RemoteUserService remoteUserService;

    /**
     * 登录-第三方授权登录
     *
     * @param body   登录信息
     * @param client 客户端信息
     */
    @Override
    public LoginVo login(String body, RemoteClientVo client) {
        SocialLoginBody loginBody = JsonUtils.parseObject(body, SocialLoginBody.class);
        ValidatorUtils.validate(loginBody);
        AuthResponse<AuthUser> response = SocialUtils.loginAuth(
                loginBody.getSource(), loginBody.getSocialCode(),
                loginBody.getSocialState(), socialProperties);
        if (!response.ok()) {
            throw new ServiceException(response.getMsg());
        }
        AuthUser authUserData = response.getData();
        List<RemoteSocialVo> list = remoteSocialService.selectByAuthId(authUserData.getSource() + authUserData.getUuid());
        Long userId;
        if (CollUtil.isEmpty(list)) {
            // 直接注册登录
            RemoteUserBo userBo = new RemoteUserBo();
            userBo.setUserName(StrUtil.isBlank(authUserData.getUsername()) ? "用户" + System.currentTimeMillis() : authUserData.getUsername());
            userBo.setNickName(StrUtil.isBlank(authUserData.getNickname()) ? userBo.getUserName() : authUserData.getNickname());
            userBo.setPassword(UUID.randomUUID().toString().replace("-", ""));
            userBo.setEmail(authUserData.getEmail());
            userBo.setAvatarUrl(authUserData.getAvatar());
            userId = remoteUserService.registerUserInfo(userBo);
            if (userId == null) {
                throw new ServiceException("第三方登录失败");
            }
            sysLoginService.socialRegister(authUserData, userId);
        } else {
            RemoteSocialVo socialVo = list.get(0);
            userId = socialVo.getUserId();
        }
        LoginUser loginUser = remoteUserService.getUserInfo(userId);
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
        TenantHelper.setDynamic(loginUser.getTenantId().toString(), true);
        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return loginVo;
    }

}
