package com.blockeng.boss.mapper;

import com.blockeng.boss.dto.Symbol;
import com.blockeng.boss.entity.Account;
import com.blockeng.boss.entity.AccountDetail;
import com.blockeng.boss.entity.TurnoverOrder;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.OrderType;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午1:04
 * @Modified by: Chen Long
 */
public interface BossMapper {

    /**
     * 根据委托卖单和委托买单查询成交记录数量
     *
     * @param sellOrderId 委托卖单ID
     * @param buyOrderId  委托买单ID
     * @return
     */
    int queryDealOrderCount(@Param("sellOrderId") Long sellOrderId,
                            @Param("buyOrderId") Long buyOrderId);

    /**
     * 查询交易对币种信息
     *
     * @param marketId 交易对ID
     * @return
     */
    Symbol querySymbol(@Param("marketId") Long marketId);

    /**
     * 保存成交订单
     *
     * @param turnoverOrder
     */
    int saveDealOrder(TurnoverOrder turnoverOrder);

    /**
     * 修改委托买单
     *
     * @param orderId    订单编号
     * @param dealVolume 成交数量
     * @return
     */
    int modifyEntrustBuyOrder(@Param("orderId") Long orderId, @Param("dealVolume") BigDecimal dealVolume);

    /**
     * 修改委托卖单
     *
     * @param orderId    订单编号
     * @param dealVolume 成交数量
     * @return
     */
    int modifyEntrustSellOrder(@Param("orderId") Long orderId, @Param("dealVolume") BigDecimal dealVolume);

    /**
     * 查询用户资金账户
     *
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    Account queryAccount(@Param("userId") Long userId, @Param("coinId") Long coinId);

    /**
     * 扣减资金
     *
     * @param accountId    资金账户ID
     * @param amount       扣减资金额度
     * @param returnAmount 返还多冻结部分
     * @return
     */
    int subtractAmount(@Param("accountId") Long accountId,
                       @Param("amount") BigDecimal amount,
                       @Param("returnAmount") BigDecimal returnAmount);

    /**
     * 增加账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    增加资金额度
     * @return
     */
    int addAmount(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    /**
     * 批量保存交易流水
     *
     * @param toAccountDetails
     */
    int batchAddAccountDetail(List<AccountDetail> toAccountDetails);

    int unlockAmount(@Param("accountId") Long id, @Param("amount") BigDecimal amount);
}
