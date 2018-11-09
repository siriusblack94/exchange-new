package com.blockeng.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.trade.entity.EntrustOrder;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 委托订单信息 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface EntrustOrderMapper extends BaseMapper<EntrustOrder> {

    /**
     * 撤销委托订单
     *
     * @param orderId 委托订单号
     */
    int cancelEntrustOrder(@Param("orderId") long orderId);

    EntrustOrder getCancelEntrustOrder(Long id);

    /**
     * 开始撤单
     *
     * @param orderId
     */
    void startCancel(@Param("orderId") Long orderId);
}
