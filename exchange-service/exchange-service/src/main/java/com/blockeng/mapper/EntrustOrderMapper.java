package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.dto.DepthItemDTO;
import com.blockeng.entity.EntrustOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 委托订单信息 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface EntrustOrderMapper extends BaseMapper<EntrustOrder> {

}
