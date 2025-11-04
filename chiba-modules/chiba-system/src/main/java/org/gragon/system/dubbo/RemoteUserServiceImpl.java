package org.gragon.system.dubbo;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.gragon.common.core.enums.UserStatus;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.exception.user.UserException;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.system.api.RemoteUserService;
import org.gragon.system.api.domain.bo.RemoteUserBo;
import org.gragon.system.api.domain.vo.RemoteUserVo;
import org.gragon.system.api.model.LoginUser;
import org.gragon.system.api.model.XcxLoginUser;
import org.gragon.system.domain.SysUser;
import org.gragon.system.domain.bo.SysUserBo;
import org.gragon.system.domain.vo.SysUserVo;
import org.gragon.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteUserServiceImpl implements RemoteUserService {

    private final ISysUserService userService;

    /**
     * 通过用户名查询用户信息
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public LoginUser getUserInfo(String userName) throws UserException {
        SysUserVo sysUser = userService.selectUserByUserName(userName);
        if (ObjectUtil.isNull(sysUser)) {
            throw new UserException("user.not.exists", userName);
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            throw new UserException("user.blocked", userName);
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        return buildLoginUser(sysUser);
    }

    /**
     * 通过用户id查询用户信息
     *
     * @param userId   用户id
     * @return 结果
     */
    @Override
    public LoginUser getUserInfo(Long userId) throws UserException {
        SysUserVo sysUser = userService.selectUserById(userId);
        if (ObjectUtil.isNull(sysUser)) {
            throw new UserException("user.not.exists", "");
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            throw new UserException("user.blocked", sysUser.getUserName());
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        return buildLoginUser(sysUser);
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param phoneNumber 手机号
     * @return 结果
     */
    @Override
    public LoginUser getUserInfoByPhonenumber(String phoneNumber) throws UserException {
        SysUserVo sysUser = userService.selectUserByPhoneNumber(phoneNumber);
        if (ObjectUtil.isNull(sysUser)) {
            throw new UserException("user.not.exists", phoneNumber);
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            throw new UserException("user.blocked", phoneNumber);
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        return buildLoginUser(sysUser);
    }

    /**
     * 通过邮箱查询用户信息
     *
     * @param email    邮箱
     * @return 结果
     */
    @Override
    public LoginUser getUserInfoByEmail(String email) throws UserException {
        SysUserVo user = userService.selectUserByEmail(email);
        if (ObjectUtil.isNull(user)) {
            throw new UserException("user.not.exists", email);
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new UserException("user.blocked", email);
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        return buildLoginUser(user);
    }

    /**
     * 通过openid查询用户信息
     *
     * @param openid openid
     * @return 结果
     */
    @Override
    public XcxLoginUser getUserInfoByOpenid(String openid) throws UserException {
        // todo 自行实现 userService.selectUserByOpenid(openid);
        SysUser sysUser = new SysUser();
        if (ObjectUtil.isNull(sysUser)) {
            // todo 用户不存在 业务逻辑自行实现
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            // todo 用户已被停用 业务逻辑自行实现
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setUsername(sysUser.getUserName());
        loginUser.setNickname(sysUser.getNickName());
        loginUser.setUserType(sysUser.getRole().toString());
        loginUser.setOpenid(openid);
        return loginUser;
    }

    /**
     * 注册用户信息
     *
     * @param remoteUserBo 用户信息
     * @return 结果
     */
    @Override
    public Boolean registerUserInfo(RemoteUserBo remoteUserBo) throws UserException, ServiceException {
        SysUserBo sysUserBo = MapstructUtils.convert(remoteUserBo, SysUserBo.class);
        String username = sysUserBo.getUserName();
        if (userService.existUserName(username)) {
            throw new UserException("user.register.save.error", username);
        }
        return userService.registerUser(sysUserBo);
    }

    /**
     * 通过用户ID查询用户账户
     *
     * @param userId 用户ID
     * @return 用户账户
     */
    @Override
    public String selectUserNameById(Long userId) {
        return userService.selectUserNameById(userId);
    }

    /**
     * 通过用户ID查询用户昵称
     *
     * @param userId 用户ID
     * @return 用户昵称
     */
    @Override
    public String selectNicknameById(Long userId) {
        return userService.selectNicknameById(userId);
    }

    /**
     * 通过用户ID查询用户手机号
     *
     * @param userId 用户id
     * @return 用户手机号
     */
    @Override
    public String selectPhoneNumberById(Long userId) {
        return userService.selectPhoneNumberById(userId);
    }

    /**
     * 通过用户ID查询用户邮箱
     *
     * @param userId 用户id
     * @return 用户邮箱
     */
    @Override
    public String selectEmailById(Long userId) {
        return userService.selectEmailById(userId);
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUserVo userVo) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userVo.getUserId());
        loginUser.setUsername(userVo.getUserName());
        loginUser.setNickname(userVo.getNickName());
        loginUser.setPassword(userVo.getPassword());
        loginUser.setUserType(userVo.getRole().toString());
//        loginUser.setMenuPermission(permissionService.getMenuPermission(userVo.getUserId()));
//        loginUser.setRolePermission(permissionService.getRolePermission(userVo.getUserId()));
        return loginUser;
    }

    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param ip     IP地址
     */
    @Override
    public void recordLoginInfo(Long userId, String ip) {
        // TODO 后续实现 记录在用户行为表中
    }

    /**
     * 通过用户ID查询用户列表
     *
     * @param userIds 用户ids
     * @return 用户列表
     */
    @Override
    public List<RemoteUserVo> selectListByIds(List<Long> userIds) {
        List<SysUserVo> sysUserVos = userService.selectUserByIds(userIds);
        return MapstructUtils.convert(sysUserVos, RemoteUserVo.class);
    }
}
