package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.entity.CashWithdrawals;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 提现表 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CashWithdrawalsMapper extends BaseMapper<CashWithdrawals> {

    BigDecimal getTotalByUserId(@Param("userId") long userId, @Param("dateTime") String dateTime);

}
