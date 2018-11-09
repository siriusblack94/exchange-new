package com.blockeng.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.WalletCoinRecharge;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
public interface WalletCoinRechargeService extends IService<WalletCoinRecharge> {

}
