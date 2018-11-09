package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.*;
import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.framework.exception.ExchangeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 虚拟币提现 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
public interface CoinWithdrawService extends IService<CoinWithdraw> {

    /**
     * 分页查询
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<CoinWithdraw> selectListPage(Page<CoinWithdraw> page, Wrapper<CoinWithdraw> wrapper);

    /**
     * 提币审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     */
    void coinWithdrawAudit(AuditDTO auditDTO, SysUser sysUser) throws ExchangeException;

    /**
     * 统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    Page<CoinWithdrawalsCountDTO> selectCountMain(Page<CoinWithdrawalsCountDTO> page, Map<String, Object> paramMap);


    /**
     * <!--成功笔数，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CoinWithdrawalsCountDTO> selectValidCounts(Map<String, Object> paramMap);
    /**
     * <!--用户数，用户id，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CoinWithdrawalsCountDTO> selectUserCt(Map<String, Object> paramMap);

    Object withDrawSuccess(CoinWithDrawDTO coinWithDrawDTO);

    Integer reTryWithdraw(AuditDTO retryDTO);

    BigDecimal selectAmountByCoinId(String id);

    DigitalCoinWithdrawStatisticsDTO selectDigitalCoinWithdrawStatistics(int current,int size,String startTime,String endTime,String coinId,String userId);

    List<Map<String,Object>> selectCoinWithdrawGroupCoin(String userId);

    List<Map<String,Object>> selectCoinWithdrawFreezeGroupCoin(String userId);
}
