package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.CoinRechargeCountDTO;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
public interface CoinRechargeMapper extends BaseMapper<CoinRecharge> {

    List<CoinRechargeDTO> selectMapPage(Pagination page, Map<String, Object> paramMap);

    /**
     * 根据日期和用户统计充币人数
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
     * 充值金额，到账金额，充值币种，充值笔数，充值用户数 ，充值时间 统计统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    List<CoinRechargeCountDTO> selectCountMain(Pagination page, Map<String, Object> paramMap);

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


    List<Map<String,Object>> selectCoinRechargeGroupCoin(Map<String,Object> paramMap);

}
