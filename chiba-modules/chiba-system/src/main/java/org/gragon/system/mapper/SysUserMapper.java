package org.gragon.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gragon.common.mybatis.annotation.DataColumn;
import org.gragon.common.mybatis.annotation.DataPermission;
import org.gragon.common.mybatis.core.mapper.BaseMapperPlus;
import org.gragon.system.domain.SysUser;
import org.gragon.system.domain.vo.SysUserVo;

import java.util.List;

/**
 * 用户表 数据层
 */
@Mapper
public interface SysUserMapper extends BaseMapperPlus<SysUser, SysUserVo> {

    @DataPermission({
            @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<SysUserVo> selectPageUserList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    @DataPermission({
            @DataColumn(key = "userName", value = "user_id")
    })
    List<SysUserVo> selectUserList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    @Override
    @DataPermission({
            @DataColumn(key = "userName", value = "user_id")
    })
    int update(@Param(Constants.ENTITY) SysUser user, @Param(Constants.WRAPPER) Wrapper<SysUser> updateWrapper);

    @Override
    @DataPermission({
            @DataColumn(key = "userName", value = "user_id")
    })
    int updateById(@Param(Constants.ENTITY) SysUser user);

}
