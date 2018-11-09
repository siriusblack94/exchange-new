package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.CoinRechargeCountDTO;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.entity.WalletCoinRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
public interface WalletCoinRechargeMapper extends BaseMapper<WalletCoinRecharge> {

}
