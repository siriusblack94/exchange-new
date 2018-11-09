package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.dto.BuckleAccountCountDTO;
import com.blockeng.admin.dto.CoinBuckleDTO;
import com.blockeng.admin.entity.AccountFreeze;
import com.blockeng.admin.entity.CoinBuckle;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CoinBuckleMapper extends BaseMapper<CoinBuckle> {

    List<CoinBuckleDTO> selectDTOList(@Param("size") int size, @Param("current") int current, @Param("ew") EntityWrapper<CoinBuckleDTO> ew);

    BigDecimal selectSumTotal( @Param("ew")EntityWrapper<BuckleAccountCountDTO> ew);

    BigDecimal selectSubTotal( @Param("ew")EntityWrapper<BuckleAccountCountDTO> ew);

    List<BuckleAccountCountDTO> selectBuckleAccountCounts(@Param("current") Integer current, @Param("size") Integer size, @Param("ew") EntityWrapper<BuckleAccountCountDTO> ew);

    int selectListPageCount(@Param("ew") EntityWrapper<CoinBuckleDTO> ew);

    int selectBuckleAccountCountsTotal(@Param("ew")EntityWrapper<BuckleAccountCountDTO> ew);

    List<AccountFreeze> selectBuckleFreeze(@Param("coinId")String coinId, @Param("userFlag")String userFlag);

    List<Map<String,Object>> selectCoinBuckleGroupCoin(Map<String,Object> paramMap);

    List<Map<String,Object>> selectCoinBuckleFreezeGroupCoin(Map<String,Object> paramMap);
}
