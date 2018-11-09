package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.CoinWithdrawRetryRecordDTO;
import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.CoinWithdrawRetryRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CoinWithdrawRetryRecordMapper extends BaseMapper<CoinWithdrawRetryRecord> {


    List<CoinWithdrawRetryRecordDTO> selectListPage(Page<CoinWithdrawRetryRecordDTO> page, @Param("ew") Wrapper<CoinWithdrawRetryRecord> wrapper);
}
