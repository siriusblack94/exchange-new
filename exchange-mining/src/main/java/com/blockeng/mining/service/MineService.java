package com.blockeng.mining.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.dto.MineTotalDTO;
import com.blockeng.mining.entity.Mine;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 矿池 数据查询
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface MineService extends IService<Mine> {

    void calcTotalDayMining();

    BigDecimal totalMine();

    BigDecimal dayTotalMine(String timeMining);

    MineTotalDTO mineTotal(Long id);


    BigDecimal priMonthTotalMine(String day, Long userId);

    BigDecimal priWeekTotalMine(String nowDay, Long id);

    Object mineCoinInfo(Long id);

     Map<String, BigDecimal> getAveragePrice();
}
