package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.CashWithdrawalsCountDTO;
import com.blockeng.admin.dto.CurbExchangeWithdrawStatisticsDTO;
import com.blockeng.admin.dto.UserWithDrawalsDTO;
import com.blockeng.admin.entity.CashWithdrawals;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.framework.exception.ExchangeException;
import com.blockeng.framework.exception.ExchangeException;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提现表 服务类
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
public interface CashWithdrawalsService extends IService<CashWithdrawals> {

    Page<UserWithDrawalsDTO> selectMapPage(Page<UserWithDrawalsDTO> page, Map<String, Object> paramMap);

    UserWithDrawalsDTO selectOneObj(Long id);

    /**
     * 法币提现审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    void cashWithdrawAudit(AuditDTO auditDTO, SysUser sysUser) throws ExchangeException;

    /**
     * 统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    Page<CashWithdrawalsCountDTO> selectCountMain(Page<CashWithdrawalsCountDTO> page, Map<String, Object> paramMap);


    /**
     * <!--成功笔数，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CashWithdrawalsCountDTO> selectValidCounts(Map<String, Object> paramMap);


    /**
     * <!--用户数，用户id，充值时间-->
     *
     * @param paramMap
     * @return
     */
    List<CashWithdrawalsCountDTO> selectUserCt(Map<String, Object> paramMap);


    /**
     *  场外交易充值统计
     * */
    CurbExchangeWithdrawStatisticsDTO selectCurbExchangeWithdrawStatistics(int current, int size, String startTime, String endTime, String userId);

    /**
     * 场外提现
     * */
    List<Map<String,Object>> selectCashWithdrawGroupCoin(String userId);


    /**
     * 查询某用户 场外提现冻结信息 按币种分类
     * */
    List<Map<String,Object>> selectCashWithdrawFreezeByUserGroupCoin(String userId);

}
