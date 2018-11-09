package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.NowWeekDividendDTO;
import com.blockeng.mining.entity.DividendRecord;

import java.math.BigDecimal;

/**
 * <p>
 * 邀请奖励
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface DividendRecordService extends IService<DividendRecord> {

    Integer saveRecord(BigDecimal dayTotalMining, String rate, Long refeId, Long userId, int enable, String mark);


    BigDecimal selectTotalPriWeek(Long userId);


    Object dividendAccountPriWeek(String unLockDate, Long id);


    void inviteRelation();

    NowWeekDividendDTO dividendAccountThisWeek(Long userId);

}
