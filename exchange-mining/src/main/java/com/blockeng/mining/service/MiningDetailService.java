package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.FeeDTO;
import com.blockeng.mining.entity.MiningDetail;
import com.blockeng.mining.entity.TurnoverOrder;

import java.util.List;

/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface MiningDetailService extends IService<MiningDetail> {

    void calcTxInfo(TurnoverOrder turnoverOrder);

    List<FeeDTO> dayTotalFee(String date);
}
