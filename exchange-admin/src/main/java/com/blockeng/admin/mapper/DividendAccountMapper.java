package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.DividendAccountDTO;
import com.blockeng.admin.entity.DividendAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DividendAccountMapper extends BaseMapper<DividendAccount> {

    List<DividendAccountDTO> selectListPage(Page<DividendAccountDTO> page, @Param("ew") Wrapper<DividendAccountDTO> wrapper);
}
