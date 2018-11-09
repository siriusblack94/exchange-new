package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.CoinRechargeCountDTO;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.entity.WalletCoinRecharge;
import com.blockeng.admin.mapper.CoinRechargeMapper;
import com.blockeng.admin.mapper.WalletCoinRechargeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Service
public class WalletCoinRechargeServiceImpl extends ServiceImpl<WalletCoinRechargeMapper, WalletCoinRecharge> implements WalletCoinRechargeService {

}
