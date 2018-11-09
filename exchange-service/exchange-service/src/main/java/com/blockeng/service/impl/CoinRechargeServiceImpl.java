package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.CoinRecharge;
import com.blockeng.mapper.CoinRechargeMapper;
import com.blockeng.service.CoinRechargeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务实现类
 * </p>
 *
 * @author crow
 * @since 2018-05-16
 */
@Service
public class CoinRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinRechargeService {

}
