package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.AdminBank;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.mapper.AdminBankMapper;
import com.blockeng.service.AdminBankService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人民币充值卡号管理 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class AdminBankServiceImpl extends ServiceImpl<AdminBankMapper, AdminBank> implements AdminBankService {

    /**
     * 查询银行卡
     *
     * @return
     */
    @Override
    public AdminBank getFirstAdminBank() {
        QueryWrapper<AdminBank> ew = new QueryWrapper<>();
        ew.eq("status", BaseStatus.EFFECTIVE.getCode());
        ew.orderByDesc("id");
        ew.last("limit 1");
        return super.selectOne(ew);
    }
}
