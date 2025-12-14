package org.gragon.common.social.wechatmini;


import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

public enum AuthWechatMiniSource implements AuthSource {
    WECHAT_MINI {
        @Override
        public String authorize() {
            // 参见 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html 文档
            throw new UnsupportedOperationException("不支持获取授权 url，请使用小程序内置函数 wx.login() 登录获取 code");
        }

        @Override
        public String accessToken() {
            // 参见 https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html 文档
            // 获取 openid, unionId , session_key 等字段
            return "https://api.weixin.qq.com/sns/jscode2session";
        }

        @Override
        public String userInfo() {
            // 参见 https://developers.weixin.qq.com/miniprogram/dev/api/open-api/user-info/wx.getUserProfile.html 文档
            throw new UnsupportedOperationException("不支持获取用户信息 url，请使用小程序内置函数 wx.getUserProfile() 获取用户信息");
        }

        @Override
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthWechatMiniRequest.class;
        }
    }
}
