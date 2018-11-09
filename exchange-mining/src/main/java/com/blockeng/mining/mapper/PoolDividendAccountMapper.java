package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.dto.DividendTotalAccountDTO;
import com.blockeng.mining.dto.PoolDividendTotalAccountDTO;
import com.blockeng.mining.entity.PoolDividendAccount;
import com.blockeng.mining.entity.PoolDividendRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface PoolDividendAccountMapper extends BaseMapper<PoolDividendAccount> {


    PoolDividendTotalAccountDTO selectTotal(@Param("user_id") Long userId);


}
