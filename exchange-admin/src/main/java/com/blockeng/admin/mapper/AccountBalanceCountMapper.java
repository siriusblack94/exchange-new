package com.blockeng.admin.mapper;

import com.blockeng.admin.entity.AccountBalance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountBalanceCountMapper {

    List<AccountBalance> accountBalanceCount(@Param("coinId")String coinId, @Param("userFlag")String userFlag);

}
