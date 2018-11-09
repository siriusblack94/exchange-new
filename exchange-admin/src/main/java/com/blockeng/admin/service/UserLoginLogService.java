package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.UserCountLoginDTO;
import com.blockeng.admin.entity.UserLoginLog;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 用户登录日志 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserLoginLogService extends IService<UserLoginLog> {

    /**
     * 登陆统计
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<UserCountLoginDTO> selectLoginCountPage(Page<UserCountLoginDTO> page, Wrapper<UserCountLoginDTO> wrapper);

    org.springframework.data.domain.Page<com.blockeng.repository.UserLoginLog> selectListFromMongo(int current, int size, String startTime, String endTime);

   // int selectListCountFromMongo(String current, String size);
}
