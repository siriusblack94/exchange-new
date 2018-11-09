package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blockeng.mining.entity.TurnoverOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
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
    List<TurnoverOrder> selectListPage(IPage<TurnoverOrder> page, @Param("ew") Wrapper<TurnoverOrder> wrapper);
    List<TurnoverOrder> getOrders(@Param("startDate")String startDate, @Param("endDate")String endDate);
    List<String> getSellUser();
    List<String> getBuyUser();
    List<TurnoverOrder> getOrderByUseridByDay(@Param("startDate")String startDate, @Param("endDate")String endDate,@Param("userid")String userid);
}
