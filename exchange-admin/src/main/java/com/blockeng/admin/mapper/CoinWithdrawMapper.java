package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.CoinWithdrawalsCountDTO;
import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.AccountFreeze;
import com.blockeng.admin.entity.DigitalCoinWithdrawStatistics;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 当用户发起提币的时候,吧数据插入到该表 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
public interface CoinWithdrawMapper extends BaseMapper<CoinWithdraw> {

    List<CoinWithdraw> selectListPage(Page<CoinWithdraw> page, @Param("ew") Wrapper<CoinWithdraw> wrapper);

    /**
     * 根据日期和用户统计提币人数
     *
     * @param countDate
     * @param uidStrs   用户ID字符串('1,2,3')
     * @param status    充值状态
     * @return
     */
    Integer countByDateAndUidStrs(@Param("countDate") String countDate,
                                  @Param("uidStrs") String uidStrs,
                                  @Param("status") Integer status);

    /**
     * 提币金额，到账金额，提币币种，提币笔数，提币用户数 ，提币时间 统计统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    List<CoinWithdrawalsCountDTO> selectCountMain(Pagination page, Map<String, Object> paramMap);

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


    List<AccountFreeze> selectCoinWithdrawFreeze(@Param("coinId")String coinId, @Param("userFlag")String userFlag);


    List<DigitalCoinWithdrawStatistics> selectDigitalCoinWithdrawStatistics(Pagination page,Map<String,Object> paramMap);


    Map<String,Object> countDigitalCoinWithdrawCountsAndTimes(Map<String,Object> paramMap);


    BigDecimal selectAmountByCoinId(String id);


    List<Map<String,Object>> selectCoinWithdrawGroupCoin(Map<String,Object> paramMap);

    List<Map<String,Object>> selectCoinWithdrawFreezeGroupCoin(Map<String,Object> paramMap);
}
