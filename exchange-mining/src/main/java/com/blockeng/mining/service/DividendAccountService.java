package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.DividendAccountDTO;
import com.blockeng.mining.dto.NowWeekDividendDTO;
import com.blockeng.mining.entity.DividendAccount;
import com.blockeng.mining.entity.DividendReleaseRecord;

import java.math.BigDecimal;

/**
 * <p>
 * 邀请奖励解冻
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface DividendAccountService extends IService<DividendAccount> {

    Page<DividendAccountDTO> dividendAccountUnlockList(Page<DividendReleaseRecord> page, Long id);


    Object unlockdividendAccount(Long id, BigDecimal lockAmount);


    Object dividendAccountTotal(Long userId);

}
