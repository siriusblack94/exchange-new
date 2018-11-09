package com.blockeng.wallet.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.blockeng.wallet.entity.CoinRecharge;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinRechargeMapper extends BaseMapper<CoinRecharge> {

}
