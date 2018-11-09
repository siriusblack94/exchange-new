package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.dto.FeeDTO;
import com.blockeng.mining.entity.MiningDetail;
import com.blockeng.mining.entity.TurnoverOrder;
import com.blockeng.mining.mapper.MiningDetailMapper;
import com.blockeng.mining.service.MiningDetailService;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 挖矿统计
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

@Service
@Slf4j
@Transactional
public class MiningDetailServiceImpl extends ServiceImpl<MiningDetailMapper, MiningDetail> implements MiningDetailService {


    /**
     * 当有资金变动的时候,按照日期累计增加个人挖矿信息
     *
     * @param turnoverOrder
     */
    @Transactional
    @Override
    public void calcTxInfo(TurnoverOrder turnoverOrder) {

        String nowDay = TimeUtils.getNowDay();//获取需要存储的挖矿日期
        String areaName = turnoverOrder.getMarketName().split("\\/")[1]; //交易市场名称
        BigDecimal sellTotalFee = BigDecimal.ZERO;
        BigDecimal buyTotalFee = BigDecimal.ZERO;
        //买方id和卖方id一样的时候记录一份一样的数据即可
        if (turnoverOrder.getBuyUserId().equals(turnoverOrder.getSellUserId())) {

            sellTotalFee = turnoverOrder.getAmount()
                    .multiply(turnoverOrder.getSellFeeRate())
                    .multiply(new BigDecimal("2"));
            this.baseMapper.updateMining(sellTotalFee, areaName, nowDay, turnoverOrder.getBuyUserId());

        } else {
            buyTotalFee = turnoverOrder.getAmount()
                    .multiply(turnoverOrder.getBuyFeeRate());
            this.baseMapper.updateMining(buyTotalFee, areaName, nowDay, turnoverOrder.getBuyUserId());

            sellTotalFee = turnoverOrder.getAmount()
                    .multiply(turnoverOrder.getSellFeeRate());
            this.baseMapper.updateMining(sellTotalFee, areaName, nowDay, turnoverOrder.getSellUserId());

            }
        }


    /**
     * 按照指定日期计算计算当天挖矿手续费总和
     */
    @Transactional
    @Override
    public List<FeeDTO> dayTotalFee(String day) {
        return super.baseMapper.dayTotalFee(day);
    }
}
