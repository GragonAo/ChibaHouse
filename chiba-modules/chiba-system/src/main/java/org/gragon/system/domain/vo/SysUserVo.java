package org.gragon.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.gragon.common.core.enums.UserRoleType;
import org.gragon.common.core.enums.UserSexType;
import org.gragon.common.core.enums.UserStatus;
import org.gragon.system.domain.SysUser;

import java.io.Serial;
import java.io.Serializable;


/**
 * 用户信息视图对象 sys_user
 *
 * @author Michelle.Chung
 */
@Data
@AutoMapper(target = SysUser.class)
public class SysUserVo implements Serializable {

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
     * 密码Hash
     */
    @JsonIgnore
    @JsonProperty
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
