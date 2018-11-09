package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.PlantCoinDividendRecordDTO;
import com.blockeng.mining.entity.DividendAccount;
import com.blockeng.mining.entity.DividendRecord;
import com.blockeng.mining.entity.PlantCoinDividendRecord;

import java.math.BigDecimal;

/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface PlantCoinDividendRecordService extends IService<PlantCoinDividendRecord> {

    void bxxDividend();

    /**
     * 平台的总流通量
     *
     * @return
     */
    BigDecimal totalDividend();

    Object plantCoinDividendRecord(Page<PlantCoinDividendRecord> page, Long id);

    Object plantCoinDividendTotal(Long id);
}
