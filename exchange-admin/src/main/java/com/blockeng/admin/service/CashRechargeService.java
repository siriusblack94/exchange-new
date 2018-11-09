package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.CashRechargeCountDTO;
import com.blockeng.admin.dto.CurbExchangeRechargeStatisticsDTO;
import com.blockeng.admin.dto.UserCashRechargeDTO;
import com.blockeng.admin.entity.CashRecharge;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.framework.exception.ExchangeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 法币充值 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface CashRechargeService extends IService<CashRecharge> {

    Page<UserCashRechargeDTO> selectMapPage(Page<UserCashRechargeDTO> page, Map<String, Object> paramMap);

    UserCashRechargeDTO selectOneObj(Long id);

    /**
     * 查询出规定的条数数据
     *
     * @param pageSize 为空返回查询所有，否则返回规定的条数集合
     * @return
     */
    List<UserCashRechargeDTO> selectUserCashRechargeDTOList(Integer pageSize);

    /**
     * 法币充值审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    void cashRechargeAudit(AuditDTO auditDTO, SysUser sysUser) throws ExchangeException;

    /**
     * 统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    Page<CashRechargeCountDTO> selectCountMain(Page<CashRechargeCountDTO> page, Map<String, Object> paramMap);


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
     *  场外交易充值统计
     * */
    CurbExchangeRechargeStatisticsDTO selectCurbExchangeRechargeStatistics(int current,int size,String startTime,String endTime,String userId);

    /**
     * 查询某用户 总充值信息 按币种分类
     * */
    List<Map<String,Object>> selectCashRechargeByUserGroupCoin(String userId);

}
