package com.blockeng.mining.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.mining.entity.User;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询所有实名认证并且邀请码不为空的
     *
     * @return
     */
    List<User> inviteList();

    /**
     * 所有已经高级实名认证的
     *
     * @return
     */
    List<User> authStatusList();


}
