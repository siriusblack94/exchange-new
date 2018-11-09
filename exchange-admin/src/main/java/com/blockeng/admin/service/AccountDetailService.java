package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.DigitalCoinRechargeStatisticsDTO;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import com.blockeng.admin.entity.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金账户流水 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
public interface AccountDetailService extends IService<AccountDetail> {

    /**
     * 分页查询
     *
     * @param var1
     * @param var2
     * @return
     */
    Page<AccountDetail> selectListPage(Page<AccountDetail> var1, Wrapper<AccountDetail> var2);


    /**
     * 分页查询
     *
     * @return
     */
    List<AccountDetail> selectListPageEmpty(int current, int size);


    /**
     * 分页查询
     *
     * @param ew      account
     * @param ewOther User
     * @return
     */
    List<AccountDetail> selectListPageFromUser(int current, int size, Wrapper<User> ew, Wrapper<AccountDetail> ewOther);


    /**
     * 空查询
     *
     * @param ew      account
     * @param ewOther User
     * @return 空查询
     */
    List<AccountDetail> selectListPageFromAccount(int current, int size, Wrapper<AccountDetail> ew, Wrapper<User> ewOther);

    /**
     * count
     *
     * @return 查询总是,
     */
    Integer selectListPageCount();

    /**
     * count
     *
     * @param ew account
     * @return 更具ew查询总数
     */
    Integer selectListPageCountFromAccount(Wrapper<AccountDetail> ew, Wrapper<User> ewOther);

    /**
     * count
     *
     * @param ew account
     * @return 更具user查询总数
     */
    Integer selectListPageCountFromUser(Wrapper<User> ew, Wrapper<AccountDetail> ewOther);

    Integer selectListPageEmptyInDaysCount(EntityWrapper<AccountDetail> accountEw, EntityWrapper<User> userEw);


    /**
     * 数字币充值统计新增需求
     * */
    DigitalCoinRechargeStatisticsDTO selectDigitalCoinRechargeStatistics(int current, int size, String startTime, String endTime, String coinId, String userId, int rechargeMethod);


    /**
     * 查询所有充值信息 (充值、后台充值、资产转移、补的资金)
     * */
    List<Map<String,Object>> selectAllRechargeByUser(String userId);
}
