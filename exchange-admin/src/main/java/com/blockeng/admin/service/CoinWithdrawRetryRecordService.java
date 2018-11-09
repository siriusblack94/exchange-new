package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.CoinWithdrawRetryRecordDTO;
import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.CoinWithdrawRetryRecord;

public interface CoinWithdrawRetryRecordService extends IService<CoinWithdrawRetryRecord> {


    /**
     * 分页查询
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<CoinWithdrawRetryRecordDTO> selectListPage(Page<CoinWithdrawRetryRecordDTO> page, Wrapper<CoinWithdrawRetryRecord> wrapper);
}
