package org.gragon.system.api.domain.bo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gragon.common.core.constant.RegexConstants;
import org.gragon.common.core.xss.Xss;
import org.gragon.system.api.domain.enums.UserRoleType;
import org.gragon.system.api.domain.enums.UserSexType;
import org.gragon.system.api.domain.enums.UserStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息业务对象 sys_user
 *
 */
@Data
@NoArgsConstructor
public class RemoteUserBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 用户名
     */
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过{max}个字符")
    private String userName;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过{max}个字符")
    private String email;

    /**
     * 密码Hash
     */
    private String password_hash;

    /**
     * 用户昵称
     */
    @Xss(message = "用户昵称不能包含脚本字符")
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过{max}个字符")
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 手机号
     */
    @Pattern(regexp = RegexConstants.MOBILE, message = "手机号格式不正确")
    private String phoneNumber;

    /**
     * 用户角色
     */
    private UserRoleType role;

    /**
     * 用户状态
     */
    private UserStatus status;

    /**
     * 性别
     */
    private UserSexType sex;
}
