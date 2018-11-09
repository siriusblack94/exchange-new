package com.blockeng.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.trade.entity.TurnoverOrder;
import com.blockeng.trade.mapper.TurnoverOrderMapper;
import com.blockeng.trade.service.TurnoverOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/19 下午8:10
 * @Modified by: Chen Long
 */
@Service
@Transactional
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService {

}
