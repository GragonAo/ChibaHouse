package org.gragon.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gragon.common.core.enums.UserStatus;
import org.gragon.common.mybatis.core.domain.BaseEntity;
import org.gragon.common.core.enums.UserRoleType;
import org.gragon.common.core.enums.UserSexType;

import java.io.Serial;

/**
 * 用户对象 user
 *
 */

@Data
@NoArgsConstructor
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id")
    private Long userId;


    /**
     * 用户名
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 手机号
     */
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
