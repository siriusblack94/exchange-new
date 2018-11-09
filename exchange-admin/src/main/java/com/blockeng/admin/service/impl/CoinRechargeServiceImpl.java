package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.CoinRechargeCountDTO;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import com.blockeng.admin.mapper.CoinRechargeMapper;
import com.blockeng.admin.service.CoinRechargeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Service
public class CoinRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinRechargeService {


    @Override
    public List<Map<String, Object>> selectRechargeByUserGroupCoin(String userId) {
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("status",3);
        return baseMapper.selectCoinRechargeGroupCoin(paramMap);
    }

    public Page<CoinRechargeDTO> selectMapPage(Page<CoinRechargeDTO> page, Map<String, Object> paramMap) {

        return page.setRecords(baseMapper.selectMapPage(page, paramMap));
    }

    public Page<CoinRechargeCountDTO> selectCountMain(Page<CoinRechargeCountDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectCountMain(page, paramMap));
    }

    @Override
    public List<CoinRechargeCountDTO> selectValidCounts(Map<String, Object> paramMap) {
        return baseMapper.selectValidCounts(paramMap);
    }

    @Override
    public List<CoinRechargeCountDTO> selectUserCt(Map<String, Object> paramMap) {
        return baseMapper.selectUserCt(paramMap);
    }

    @Override
    public BigDecimal selectAmountByCoinId(String id) {
        return baseMapper.selectAmountByCoinId(id);
    }

}
