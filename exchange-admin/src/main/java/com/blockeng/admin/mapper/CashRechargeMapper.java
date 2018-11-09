package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.CashRechargeCountDTO;
import com.blockeng.admin.dto.UserCashRechargeDTO;
import com.blockeng.admin.entity.CashRecharge;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.blockeng.admin.entity.CurbExchangeRechargeStatistics;
import com.blockeng.admin.entity.DigitalCoinWithdrawStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 充值表 Mapper 接口
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
public interface CashRechargeMapper extends BaseMapper<CashRecharge> {

    List<UserCashRechargeDTO> selectMapPage(Pagination page, Map<String, Object> paramMap);


    UserCashRechargeDTO selectOneObj(Long id);

    /**
     * 查询出规定的条数数据
     *
     * @param pageSize 为空返回查询所有，否则返回规定的条数集合
     * @return
     */
    List<UserCashRechargeDTO> selectUserCashRechargeDTOList(@Param("pageSize") Integer pageSize);

    /**
     * 充值金额，到账金额，充值币种，充值笔数，充值用户数 ，充值时间 统计统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    List<CashRechargeCountDTO> selectCountMain(Pagination page, Map<String, Object> paramMap);

    /**
     * <!--成功笔数，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CashRechargeCountDTO> selectValidCounts(Map<String, Object> paramMap);


    /**
     * <!--用户数，用户id，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CashRechargeCountDTO> selectUserCt(Map<String, Object> paramMap);

    /**
     * 根据日期和用户统计充值人数
     *
     * @param countDate
     * @param uidStrs   用户ID字符串('1,2,3')
     * @param status    充值状态
     * @return
     */
    Integer countByDateAndUidStrs(@Param("countDate") String countDate,
                                  @Param("uidStrs") String uidStrs,
                                  @Param("status") Integer status);


    List<CurbExchangeRechargeStatistics> selectCurbExchangeRechargeStatistics(Pagination page, Map<String,Object> paramMap);


    Map<String,Object> countCurbExchangeRecharge(Map<String,Object> paramMap);

    List<Map<String,Object>> selectCashRecharge(Map<String,Object> paramMap);



}
