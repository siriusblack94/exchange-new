package com.blockeng.admin.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.PoolDividendAccountDTO;
import com.blockeng.admin.entity.PoolDividendAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PoolDividendAccountMapper extends BaseMapper<PoolDividendAccount> {


    List<PoolDividendAccountDTO> selectPoolDividendAccountListPage(Page<PoolDividendAccountDTO> page,  @Param("ew")Wrapper<PoolDividendAccountDTO> wrapper);

    List<PoolDividendAccountDTO> selectPoolDividendAccountDetailList(Page<PoolDividendAccountDTO> page, @Param("ew")Wrapper<PoolDividendAccountDTO> wrapper);
}
