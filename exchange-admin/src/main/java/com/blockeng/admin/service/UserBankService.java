package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.UserBankDTO;
import com.blockeng.admin.entity.UserBank;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户人民币提现地址 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserBankService extends IService<UserBank> {

    public Page<UserBankDTO> selectMapPage(Page<UserBankDTO> page, Map<String, Object> paramMap);

}
