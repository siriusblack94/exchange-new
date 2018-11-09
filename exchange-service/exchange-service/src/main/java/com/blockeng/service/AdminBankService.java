package com.blockeng.service;

import com.blockeng.entity.AdminBank;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 人民币充值卡号管理 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface AdminBankService extends IService<AdminBank> {

    /**
     * 查询银行卡
     *
     * @return
     */
    AdminBank getFirstAdminBank();
}
