package com.blockeng.dto;

import com.blockeng.entity.CoinWithdraw;
import com.blockeng.framework.dto.CoinWithdrawDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * CoinRecharge to CoinRechargeDTO 转换器
 */
@Mapper
public interface CoinWithdrawDTOMapper {

    CoinWithdrawDTOMapper INSTANCE = Mappers.getMapper(CoinWithdrawDTOMapper.class);

    CoinWithdrawDTO from(CoinWithdraw coinWithdraw);

    List<CoinWithdrawDTO> from(List<CoinWithdraw> coinWithdraws);

    CoinWithdraw from(CoinWithdrawDTO coinWithdrawDTO);
}
