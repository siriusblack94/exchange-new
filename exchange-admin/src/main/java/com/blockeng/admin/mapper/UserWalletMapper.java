package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.UserWalletDTO;
import com.blockeng.admin.entity.UserWallet;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户钱包表 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface UserWalletMapper extends BaseMapper<UserWallet> {

    List<UserWalletDTO> selectUserWalletList(Pagination pagination, @Param("id") Long id);

}
