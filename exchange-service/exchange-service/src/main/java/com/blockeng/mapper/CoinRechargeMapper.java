package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.entity.CoinRecharge;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 Mapper 接口
 * </p>
 *
 * @author crow
 * @since 2018-05-16
 */
public interface CoinRechargeMapper extends BaseMapper<CoinRecharge> {

}
