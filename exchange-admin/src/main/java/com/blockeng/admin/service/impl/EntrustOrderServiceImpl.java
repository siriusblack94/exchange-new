package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.EntrustOrder;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.mapper.EntrustOrderMapper;
import com.blockeng.admin.service.EntrustOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托订单信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService {

    @Autowired
    private EntrustOrderMapper entrustOrderMapper;


    @Override
    public Page<EntrustOrder> selectListPage(Page<EntrustOrder> page, Wrapper<EntrustOrder> wrapper) {
        wrapper = (Wrapper<EntrustOrder>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(this.entrustOrderMapper.selectListPage(page, wrapper));
        return page;
    }

    @Override
    public List<EntrustOrder> selectListPageEmpty(int current, int size) {
        return this.entrustOrderMapper.selectListPageEmpty(current, size);
    }


    @Override
    public List<EntrustOrder> selectListPageByOrder(Wrapper<EntrustOrder> wrapper, Wrapper<User> otherEw, int current, int size) {
        return this.entrustOrderMapper.selectListPageByOrder(wrapper, otherEw, current, size);
    }

    @Override
    public List<EntrustOrder> selectListPageByUser(Wrapper<User> wrapper, Wrapper<EntrustOrder> otherEw, int current, int size) {
        return this.entrustOrderMapper.selectListPageByUser(wrapper, otherEw, current, size);
    }

    @Override
    public Integer selectListPageCount() {
        return this.entrustOrderMapper.selectListPageCount();
    }


    @Override
    public Integer selectListPageByOrderCount(Wrapper<EntrustOrder> wrapper, Wrapper<User> otherEw) {
        return this.entrustOrderMapper.selectListPageByOrderCount(wrapper, otherEw);
    }

    @Override
    public Integer selectListPageByUserCount(Wrapper<EntrustOrder> wrapper, Wrapper<User> otherEw) {
        return this.entrustOrderMapper.selectListPageByUserCount(otherEw, wrapper);
    }

    @Override
    public Integer selectListPageCountInDays(Wrapper<EntrustOrder> wrapper, int current, int size) {
        return this.entrustOrderMapper.selectListPageCountInDays(wrapper,current,size);
    }

    @Override
    public List<Map<String, Object>> selectExchangeFreezeByCoin(String userId) {
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("status",0);
        return baseMapper.selectExchangeFreezeGroupCoin(paramMap);
    }

    public void startCancel(Long orderId){
        this.baseMapper.startCancel(orderId);
    }
}
