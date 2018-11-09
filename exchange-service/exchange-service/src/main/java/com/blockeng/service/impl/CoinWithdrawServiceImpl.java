package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.CoinWithdraw;
import com.blockeng.mapper.CoinWithdrawMapper;
import com.blockeng.service.CoinWithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数字货币提现 服务实现类
 * </p>
 *
 * @author crow
 * @since 2018-05-16
 */
@Service
@Slf4j
public class CoinWithdrawServiceImpl extends ServiceImpl<CoinWithdrawMapper, CoinWithdraw> implements CoinWithdrawService {

}
