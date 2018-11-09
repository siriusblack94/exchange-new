package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.dto.DividendTotalAccountDTO;
import com.blockeng.mining.entity.DividendAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;


public interface DividendAccountMapper extends BaseMapper<DividendAccount> {

    DividendTotalAccountDTO selectTotal(@Param("user_id") Long userId);


    void insertOrUpdate(@Param("userId") Long key, @Param("dayTotalMining") BigDecimal dayTotalMining,@Param("date")Date date,@Param("unlockDate")String unlockDate);

    Integer updateByUserID(DividendAccount dividendAccount);
}
