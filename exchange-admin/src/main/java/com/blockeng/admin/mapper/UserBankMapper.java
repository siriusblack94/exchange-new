package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.UserBankDTO;
import com.blockeng.admin.entity.UserBank;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户人民币提现地址 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserBankMapper extends BaseMapper<UserBank> {

    public List<UserBankDTO> selectMapPage(Pagination page, Map<String, Object> paramMap);


}
