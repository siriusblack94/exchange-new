package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.entity.CoinTransfer;

import java.util.List;
import java.util.Map;


/**
 * @Author: jakiro
 * @Date: 2018-10-30 14:31
 * @Description: 站内转帐成功记录mapper
 */
public interface CoinTransferMapper extends BaseMapper<CoinTransfer> {

     int saveCoinTransfer(CoinTransfer coinTransfer);

     List<CoinTransfer> selectCoinTransferDetail(Page<CoinTransfer> page, Map<String,Object> paramMap);
}
