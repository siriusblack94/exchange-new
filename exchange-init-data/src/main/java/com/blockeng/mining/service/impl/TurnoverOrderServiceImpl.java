package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.TurnoverOrder;
import com.blockeng.mining.mapper.TurnoverOrderMapper;
import com.blockeng.mining.service.TurnoverOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * 成交订单 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService {


    @Autowired
    private TurnoverOrderMapper turnoverOrderMapper;



    @Override
    public IPage<TurnoverOrder> selectOrders(IPage<TurnoverOrder> page, Wrapper<TurnoverOrder> wrapper) {
        wrapper = (Wrapper<TurnoverOrder>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(this.turnoverOrderMapper.selectListPage(page, wrapper));
        return page;
    }

    @Override
    public List<TurnoverOrder> getOrders(String startDate, String endDate) {
        return turnoverOrderMapper.getOrders( startDate,  endDate);
    }

    @Override
    public List<String> getSellUser() {
        return turnoverOrderMapper.getSellUser();
    }
    @Override
    public List<String> getBuyUser() {
        return turnoverOrderMapper.getBuyUser();
    }
    @Override
    public List<TurnoverOrder> getOrderByUseridByDay(String startDate, String endDate,String userid) {
        return turnoverOrderMapper.getOrderByUseridByDay(startDate,endDate,userid);
    }
}
