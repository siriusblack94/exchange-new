package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.TradeTopVolumeDTO;
import com.blockeng.admin.dto.TurnOverOrderTheCountDTO;
import com.blockeng.admin.dto.TurnOverOrderTheTotalCountDTOPage;
import com.blockeng.admin.dto.TurnoverOrderCountDTO;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.entity.TurnoverOrder;
import com.blockeng.admin.mapper.TurnoverOrderMapper;
import com.blockeng.admin.service.TurnoverOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 成交订单 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService {


    @Autowired
    private TurnoverOrderMapper turnoverOrderMapper;

    @Override
    public List<TurnoverOrder> selectListPage(int current, int size, Wrapper<TurnoverOrder> wrapper, Wrapper<TurnoverOrder> wrapper2) {
//        wrapper = (Wrapper<TurnoverOrder>) SqlHelper.fillWrapper(page, wrapper);
//        wrapper2 = (Wrapper<TurnoverOrder>) SqlHelper.fillWrapper(page, wrapper2);
        return this.turnoverOrderMapper.selectListPage(current, size, wrapper, wrapper2);
    }

    @Override
    public Page<TradeTopVolumeDTO> selectTradeTopVolumePage(Page<TradeTopVolumeDTO> page, Wrapper<TradeTopVolumeDTO> wrapper) {
        Page<TradeTopVolumeDTO> page1 = new Page<>();

        int size = page.getSize();
        int total = 0;
        int current = (page.getCurrent() - 1) * size;

        List<TradeTopVolumeDTO> list = new ArrayList<>();
        list = this.turnoverOrderMapper.
                selectTradeTopVolumePage(current, size, wrapper);
        total = this.turnoverOrderMapper.selectTradeTopVolumePageCount(wrapper);
        page1.setRecords(list).
                setCurrent(current + 1).
                setSize(size).
                setTotal(total);

        return page1;
    }

    @Override
    public Page<TurnoverOrderCountDTO> selectCountMain(Page<TurnoverOrderCountDTO> page, Map<String, Object> paramMap) {

        Page<TurnoverOrderCountDTO> page1 = new Page<>();

        page1.setRecords(this.turnoverOrderMapper.
                selectCountMain(page, paramMap)).
                setCurrent(page.getCurrent()).
                setSize(page.getSize()).
                setTotal(page.getTotal());

        return page1;
    }


    @Override
    public List<TurnoverOrderCountDTO> selectSellUserCount(String[] coins) {
        return baseMapper.selectSellUserCount(coins);
    }

    @Override
    public List<TurnoverOrderCountDTO> selectBuyUserCount(String[] coins) {
        return baseMapper.selectBuyUserCount(coins);
    }

    @Override
    public int selectListPageByUserCount(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow) {
        return baseMapper.selectListPageByUserCount(current, size, ew, ow);
    }

    @Override
    public int selectListPageCount(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow) {
        return baseMapper.selectListPageCount(current, size, ew, ow);
    }

    @Override
    public List<TurnoverOrder> selectListPageByOrder(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow) {
        return this.turnoverOrderMapper.selectListPageByOrder(current, size, ew, ow);
    }

    @Override
    public int selectListPageByOrderCount(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow) {
        return this.turnoverOrderMapper.selectListPageByOrderCount(current, size, ew, ow);
    }

    @Override
    public List<TurnOverOrderTheCountDTO> selectTheCountDTO(int c, int s, EntityWrapper<TurnOverOrderTheCountDTO> ew) {
        return baseMapper.selectTheCountDTO(c, s, ew);
    }

    @Override
    public int selectTheCountDTOCount(EntityWrapper<TurnOverOrderTheCountDTO> ew) {
        return baseMapper.selectTheCountDTOCount(ew);
    }

    @Override
    public TurnOverOrderTheTotalCountDTOPage selectTheTotalCountDTO(EntityWrapper<TurnOverOrderTheCountDTO> ew) {
        return this.turnoverOrderMapper.selectTheTotalCountDTO(ew);
    }

    @Override
    public List<TurnoverOrder> selectListPageByUser(int current, int size, Wrapper<TurnoverOrder> wrapper, Wrapper<TurnoverOrder> wrapper2) {
//        wrapper = (Wrapper<TurnoverOrder>) SqlHelper.fillWrapper(page, wrapper);
//        wrapper2 = (Wrapper<TurnoverOrder>) SqlHelper.fillWrapper(page, wrapper2);
//        page.setRecords(this.turnoverOrderMapper.selectListPageByUser(page, wrapper, wrapper2));
        return this.turnoverOrderMapper.selectListPageByUser(current, size, wrapper, wrapper2);
    }

    @Override
    public List<Map<String, Object>> selectBuyAndSellAndFeeGroupMarket(String userId) {

        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("status",1);
        return baseMapper.selectBuyAndSellOrder(paramMap);
    }
}
