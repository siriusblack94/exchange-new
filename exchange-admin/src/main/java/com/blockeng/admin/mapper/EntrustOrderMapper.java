package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.EntrustOrder;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.entity.AccountFreeze;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托订单信息 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface EntrustOrderMapper extends BaseMapper<EntrustOrder> {

    List<EntrustOrder> selectListPage(Page<EntrustOrder> page, @Param("entrustEw") Wrapper<EntrustOrder> wrapper);


    List<EntrustOrder> selectListPageEmpty(@Param("current") int current, @Param("size") int size);


    List<EntrustOrder> selectListPageByOrder(@Param("entrustEw") Wrapper<EntrustOrder> wrapper, @Param("userEw") Wrapper<User> otherEw, @Param("current") int current, @Param("size") int size);


    List<EntrustOrder> selectListPageByUser(@Param("userEw") Wrapper<User> wrapper, @Param("entrustEw") Wrapper<EntrustOrder> otherEw, @Param("current") int current, @Param("size") int size);

    Integer selectListPageCount();


    Integer selectListPageByOrderCount(@Param("entrustEw") Wrapper<EntrustOrder> wrapper, @Param("userEw") Wrapper<User> otherEw);


    Integer selectListPageByUserCount(@Param("userEw") Wrapper<User> wrapper, @Param("entrustEw") Wrapper<EntrustOrder> entrustOrderWrapper);


    Integer selectListPageCountInDays(@Param("entrustEw") Wrapper<EntrustOrder> wrapper, @Param("current") int current, @Param("size") int size);


    List<AccountFreeze> selectTransactionFreezeByMarket(@Param("coinName") String coinName, @Param("userFlag") String userFlag);
//    List<AccountFreeze> selectTransactionFreezeByMarket();

    List<Map<String, Object>> selectExchangeFreezeGroupCoin(Map<String, Object> paramMap);


    void startCancel(@Param("orderId") Long orderId);
}
