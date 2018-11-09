package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.CoinRechargeCountDTO;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.dto.DigitalCoinRechargeStatisticsDTO;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;

import java.math.BigDecimal;
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
public interface CoinRechargeService extends IService<CoinRecharge> {
    public Page<CoinRechargeDTO> selectMapPage(Page<CoinRechargeDTO> page, Map<String, Object> paramMap);

    /**
     * 统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    Page<CoinRechargeCountDTO> selectCountMain(Page<CoinRechargeCountDTO> page, Map<String, Object> paramMap);


    /**
     * <!--成功笔数，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CoinRechargeCountDTO> selectValidCounts(Map<String, Object> paramMap);


    /**
     * <!--用户数，用户id，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CoinRechargeCountDTO> selectUserCt(Map<String, Object> paramMap);

    BigDecimal selectAmountByCoinId(String id);

    /**
     *  条件userID 返回 按币种ID 分组 返回结果 币种ID coinId 充值总数 recharge
     * */
    List<Map<String,Object>> selectRechargeByUserGroupCoin(String userId);

}
