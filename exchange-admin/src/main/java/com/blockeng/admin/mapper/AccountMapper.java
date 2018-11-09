package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.User;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户财产记录 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface AccountMapper extends BaseMapper<Account> {

    List<Account> selectListPage(Page<Account> page, @Param("ew") Wrapper<Account> wrapper);


    List<Account> selectListPageEmpty(@Param("current") int current, @Param("size") int size);

    List<Account> selectListPageFromUser(@Param("current") int current, @Param("size") int size, @Param("userEw") Wrapper<User> ew, @Param("accountEw") Wrapper<Account> accountEw);

    List<Account> selectListPageFromAccount(@Param("current") int current, @Param("size") int size, @Param("accountEw") Wrapper<Account> ew, @Param("userEw") Wrapper<User> userEw);

    Integer selectListPageCount();

    Integer selectListPageCountFromUser(@Param("userEw") Wrapper<User> ew, @Param("accountEw") Wrapper<Account> accountEw);

    Integer selectListPageCountFromAccount(@Param("accountEw") Wrapper<Account> ew, @Param("userEw") Wrapper<User> userEw);


    /**
     * 冻结账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    冻结金额
     * @return
     */
    int lockAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    /**
     * 解冻账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    解冻金额
     * @return
     */
    int unlockAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    /**
     * 提现-解冻账户资金
     * @param amount
     * @return
     */
    int unlockCashAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);
    /**
     * 增加账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    增加金额
     * @return
     */
    int addAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    /**
     * 扣减账户资金
     *
     * @param accountId 资金账户ID
     * @param amount    扣减金额
     * @return
     */
    int subtractAmount(@Param("accountId") long accountId, @Param("amount") BigDecimal amount);

    int selectBalanceCountByCoinId(String id);

    List<Map<String,Object>> countUserNumberAndBalanceByFlag(@Param("coinId")String coinId, @Param("userFlag")String userFlag);

    BigDecimal selectAmount(String id);

    List<Map<String,Object>> selectBalanceByUser(Map<String,Object> paramMap);

    Account queryByUserIdAndCoinId(Map<String,Object> paramMap);
}
