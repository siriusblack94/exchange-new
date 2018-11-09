package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.Market;
import com.blockeng.admin.mapper.MarketMapper;
import com.blockeng.admin.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易对配置信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Slf4j
@Service
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {

}
