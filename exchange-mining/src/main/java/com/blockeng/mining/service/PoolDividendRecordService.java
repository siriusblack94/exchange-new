package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.PoolUserHoldCoinDTO;
import com.blockeng.mining.entity.PoolDividendAccount;
import com.blockeng.mining.entity.PoolDividendRecord;
import com.blockeng.mining.entity.User;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 矿池 分红数据
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface PoolDividendRecordService extends IService<PoolDividendRecord> {

    void dividend();


    /**
     * 获取当前矿池所有持币量
     */
    BigDecimal getTotalUserHold(Long createUser, List<User> allPoolMember);


    /**
     * 根据矿池用户获取当前用户持币量
     */
    List<PoolUserHoldCoinDTO> getPoolUserHold(Long createUser, List<User> allPoolMember);


    /**
     * 获取当前矿池所有合格的用户
     *
     * @param createUser
     * @return
     */


    List<User> getPoolAllUser(Long createUser);

    Object poolRecordListDetail(Page<PoolDividendRecord> page, String startTime, String endTime, Long id);

    Object poolUnAccount(Long id);


    Object dividendAccountThisMonth(Long id);
}
