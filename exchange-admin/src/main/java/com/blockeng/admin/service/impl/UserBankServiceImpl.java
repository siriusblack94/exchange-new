package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.UserBankDTO;
import com.blockeng.admin.entity.UserBank;
import com.blockeng.admin.mapper.UserBankMapper;
import com.blockeng.admin.service.SysUserService;
import com.blockeng.admin.service.UserBankService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 用户人民币提现地址 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService {

    public Page<UserBankDTO> selectMapPage(Page<UserBankDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectMapPage(page, paramMap));
    }

}
