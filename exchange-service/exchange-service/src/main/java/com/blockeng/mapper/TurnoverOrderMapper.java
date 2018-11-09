package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blockeng.dto.TurnoverData24HDTO;
import com.blockeng.entity.TurnoverOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 成交订单 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface TurnoverOrderMapper extends BaseMapper<TurnoverOrder> {

    /**
     * 获取最新成交价
     *
     * @param marketId 交易对ID
     * @return
     */
    BigDecimal queryCurrentPrice(@Param("marketId") Long marketId);


    /**
     * 个人用户订单管理-成交记录
     *
     * @param userId 用户ID
     * @param symbol 交易对标识符
     * @param type   类型
     * @return
     */
    long selectOrdersCount(@Param("userId") long userId,
                           @Param("symbol") String symbol,
                           @Param("type") int type);

    /**
     * 个人用户订单管理-成交记录
     *
     * @return
     */
    List<TurnoverOrder> selectListPage(IPage<TurnoverOrder> page, @Param("ew") Wrapper<TurnoverOrder> wrapper);
}
