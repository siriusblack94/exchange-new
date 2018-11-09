package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import com.blockeng.admin.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金账户流水 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
public interface AccountDetailMapper extends BaseMapper<AccountDetail> {

    List<AccountDetail> selectListPage(Page<AccountDetail> page, @Param("accountEx") Wrapper<AccountDetail> wrapper);

    List<AccountDetail> selectListPageEmpty(@Param("current") int current, @Param("size") int size);

    List<AccountDetail> selectListPageFromUser(@Param("current") int current, @Param("size") int size, @Param("userEx") Wrapper<User> ew, @Param("accountEx") Wrapper<AccountDetail> ewOther);

    List<AccountDetail> selectListPageFromAccount(@Param("current") int current, @Param("size") int size, @Param("accountEx") Wrapper<AccountDetail> ew, @Param("userEx") Wrapper<User> ewOther);

    Integer selectListPageCount();

    Integer selectListPageCountFromUser(@Param("userEx") Wrapper<User> ew, @Param("accountEx") Wrapper<AccountDetail> ewOther);

    Integer selectListPageCountFromAccount(@Param("accountEx") Wrapper<AccountDetail> ew, @Param("userEx") Wrapper<User> ewOther);

    Integer selectListPageEmptyInDaysCount(@Param("accountEx") EntityWrapper<AccountDetail> accountEw, @Param("userEx") EntityWrapper<User> userEw);

    List<DigitalCoinRechargeStatistics> selectDigitalCoinRechargeStatistics(Page<DigitalCoinRechargeStatistics> page, Map<String, Object> paramMap);

    Map<String, Object> countDigitalCoinRechargeCountsAndTimes(Map<String, Object> paramMap);

    List<Map<String,Object>> selectAllRecharge(Map<String,Object> paramMap);
}
