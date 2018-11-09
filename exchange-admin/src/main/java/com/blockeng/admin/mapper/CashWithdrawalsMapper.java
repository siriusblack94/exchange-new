package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.CashWithdrawalsCountDTO;
import com.blockeng.admin.dto.UserWithDrawalsDTO;
import com.blockeng.admin.entity.CashWithdrawals;
import com.blockeng.admin.entity.AccountFreeze;
import com.blockeng.admin.entity.CurbExchangeWithdrawStatistics;
import com.blockeng.admin.entity.DigitalCoinWithdrawStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提现表 Mapper 接口
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
public interface CashWithdrawalsMapper extends BaseMapper<CashWithdrawals> {


    List<UserWithDrawalsDTO> selectMapPage(Page<UserWithDrawalsDTO> page, Map<String, Object> paramMap);

    UserWithDrawalsDTO selectOneObj(Long id);

    /**
     * 查询出规定的条数数据
     *
     * @param pageSize 为空返回查询所有，否则返回规定的条数集合
     * @return
     */
    List<UserWithDrawalsDTO> selectCashWithdrawalsDTOList(@Param("pageSize") Integer pageSize);

    /**
     * 根据日期和用户统计提现人数
     *
     * @param countDate
     * @param uidStrs   用户ID字符串('1,2,3')
     * @param status    状态
     * @return
     */
    Integer countByDateAndUidStrs(@Param("countDate") String countDate,
                                  @Param("uidStrs") String uidStrs,
                                  @Param("status") Integer status);

    /**
     * 提现金额，到账金额，充值币种，提现笔数，提现用户数 ，提现时间 统计统计
     *
     * @param page
     * @param paramMap
     * @return
     */
    List<CashWithdrawalsCountDTO> selectCountMain(Pagination page, Map<String, Object> paramMap);

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
     * 按用户类型 币种 查询冻结金额
     * */
    List<AccountFreeze> selectCashRechargeFreeze(@Param("coinId")String coinId, @Param("userFlag")String userFlag);


    List<CurbExchangeWithdrawStatistics> selectCurbExchangeWithdrawStatistics(Pagination page, Map<String,Object> paramMap);


    Map<String,Object> countCurbExchangeWithdraw(Map<String,Object> paramMap);


    List<Map<String,Object>> selectCashWithdrawGroupCoin(Map<String,Object> paramMap);

    List<Map<String,Object>> selectCashWithdrawFreezeGroupByCoin(Map<String,Object> paramMap);
}
