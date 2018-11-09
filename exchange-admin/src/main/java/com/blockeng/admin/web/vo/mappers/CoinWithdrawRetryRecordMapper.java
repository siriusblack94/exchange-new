package com.blockeng.admin.web.vo.mappers;

import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.CoinWithdrawRetryRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoinWithdrawRetryRecordMapper {


    CoinWithdrawRetryRecordMapper INSTANCE = Mappers.getMapper(CoinWithdrawRetryRecordMapper.class);
    CoinWithdrawRetryRecord from(CoinWithdraw coinWithdraw);

}
