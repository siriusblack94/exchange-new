package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.MakeUpRechargeDTO;
import com.blockeng.admin.dto.UserWalletDTO;
import com.blockeng.admin.entity.UserWallet;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 用户钱包表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserWalletService extends IService<UserWallet> {

    Page<UserWalletDTO> selectUserWalletList(Page<UserWalletDTO> page, Long id);

    ResultMap updateRecharge(MakeUpRechargeDTO makeUpRecharge);
}
