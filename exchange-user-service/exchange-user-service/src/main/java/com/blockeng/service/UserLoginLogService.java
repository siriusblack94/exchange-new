package com.blockeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.entity.AuthUser;
import com.blockeng.entity.User;
import com.blockeng.entity.UserLoginLog;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.web.vo.*;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface UserLoginLogService extends IService<UserLoginLog> {

}
