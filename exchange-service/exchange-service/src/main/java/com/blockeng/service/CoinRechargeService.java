package com.blockeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.entity.CoinRecharge;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务类
 * </p>
 *
 * @author crow
 * @since 2018-05-16
 */
public interface CoinRechargeService extends IService<CoinRecharge> {

}
