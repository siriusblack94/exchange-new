package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.Market;
import com.blockeng.admin.entity.TradeArea;
import com.blockeng.admin.mapper.TradeAreaMapper;
import com.blockeng.admin.service.MarketService;
import com.blockeng.admin.service.TradeAreaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.framework.enums.TradeAreaType;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 交易区 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class TradeAreaServiceImpl extends ServiceImpl<TradeAreaMapper, TradeArea> implements TradeAreaService {

    @Autowired
    private MarketService marketService;

    /**
     * 分页查询
     *
     * @param page   分页参数
     * @param type   交易区域类型
     * @param name   交易区域名称
     * @param status 状态
     */
    @Override
    public Page<TradeArea> queryPage(Page<TradeArea> page, TradeAreaType type, String name, Integer status) {
        EntityWrapper<TradeArea> wrapper = new EntityWrapper<>();
        wrapper.eq("type", type.getCode());
        if (!Strings.isNullOrEmpty(name)) {
            wrapper.eq("name", name);
        }
        if (status != null) {
            wrapper.eq("status", status.intValue());
        }
        //wrapper.orderBy("sort", true);
        wrapper.orderBy("created", false);
        return super.selectPage(page, wrapper);
    }

    /**
     * 按类型查询交易区域
     *
     * @param type 交易区域类型
     * @return
     */
    @Override
    public List<TradeArea> queryByType(TradeAreaType type, String status) {
        EntityWrapper<TradeArea> wrapper = new EntityWrapper<>();
        wrapper.eq("type", type.getCode());
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", status);
        }
        return baseMapper.selectList(wrapper);
    }

    /**
     * 批量删除交易区域
     *
     * @param idList 交易区域ID集合
     */
    @Override
    @Transactional
    public void batchDelete(List<String> idList) {
        for (String id : idList) {
            Long areaId = Long.parseLong(id);
            EntityWrapper<Market> wrapper = new EntityWrapper<>();
            wrapper.eq("trade_area_id", areaId);
            int count = marketService.selectCount(wrapper);
            if (count > 0) {
                throw new ExchangeException("删除失败，选择要删除的交易区域已被引用");
            }
            baseMapper.deleteById(areaId);
        }
    }
}
