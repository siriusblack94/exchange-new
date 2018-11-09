package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.DigitalCoinRechargeStatisticsDTO;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.mapper.AccountDetailMapper;
import com.blockeng.admin.service.AccountDetailService;
import com.blockeng.admin.service.CoinService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金账户流水 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-16
 */
@Service
public class AccountDetailServiceImpl extends ServiceImpl<AccountDetailMapper, AccountDetail> implements AccountDetailService {

    @Autowired
    private AccountDetailMapper accountDetailMapper;

    @Autowired
    @Lazy
    private CoinService coinService;


    @Override
    public Page<AccountDetail> selectListPage(Page<AccountDetail> page, Wrapper<AccountDetail> wrapper) {
        wrapper = (Wrapper<AccountDetail>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(this.accountDetailMapper.selectListPage(page, wrapper));
        return page;
    }

    @Override
    public List<AccountDetail> selectListPageFromAccount(int current, int size, Wrapper<AccountDetail> ew, Wrapper<User> ewOther) {
        System.out.println(ew.getSqlSegment());
        System.out.println(ew.getParamAlias());
        System.out.println(ew.getParamNameValuePairs());
        System.out.println(ew.getSqlSelect());
        return this.baseMapper.selectListPageFromAccount(current, size, ew, ewOther);
    }

    @Override
    public List<Map<String, Object>> selectAllRechargeByUser(String userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("status", 3);
        paramMap.put("direction", 1);
        return baseMapper.selectAllRecharge(paramMap);
    }

    @Override
    public Integer selectListPageCount() {
        return this.baseMapper.selectListPageCount();
    }

    @Override
    public Integer selectListPageCountFromAccount(Wrapper<AccountDetail> ew, Wrapper<User> ewOther) {
        return this.baseMapper.selectListPageCountFromAccount(ew, ewOther);
    }

    @Override
    public Integer selectListPageCountFromUser(Wrapper<User> ew, Wrapper<AccountDetail> ewOther) {
        return this.baseMapper.selectListPageCountFromUser(ew, ewOther);
    }

    @Override
    public Integer selectListPageEmptyInDaysCount(EntityWrapper<AccountDetail> accountEw, EntityWrapper<User> userEw) {
        return this.baseMapper.selectListPageEmptyInDaysCount(accountEw, userEw);
    }


    @Override
    public List<AccountDetail> selectListPageEmpty(int current, int size) {
        return this.baseMapper.selectListPageEmpty(current, size);
    }

    @Override
    public List<AccountDetail> selectListPageFromUser(int current, int size, Wrapper<User> ew, Wrapper<AccountDetail> ewOther) {
        return this.baseMapper.selectListPageFromUser(current, size, ew, ewOther);
    }

    @Override
    public DigitalCoinRechargeStatisticsDTO selectDigitalCoinRechargeStatistics(int current, int size, String startTime, String endTime, String coinId, String userId, int rechargeMethod) {

        BigDecimal rechargeCount = new BigDecimal(0);
        Long rechargeTimes = Long.valueOf(0);
        Long total = Long.valueOf(0);

        Coin coin = coinService.selectById(coinId);
        DigitalCoinRechargeStatisticsDTO digitalCoinRechargeStatisticsDTO = new DigitalCoinRechargeStatisticsDTO();
        Page<DigitalCoinRechargeStatistics> pager = new Page<>(current, size);

        Map<String, Object> paramMap = new HashMap<>();
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("coinId", coinId);
        paramMap.put("userId", userId);

        String rechargeMethod_str = "";

        switch (rechargeMethod) {
            case 0://三种都要查 比较恶心 表里还有其他的 这个位置给一个特殊变量进去 判断
                rechargeMethod_str = "不限";
                paramMap.put("condition", "all");
                break;
            case 1://account_detail remark
                rechargeMethod_str = "充值";
                paramMap.put("remark", "充值");
                break;
            case 2://account_detail remark="后台充值"
                rechargeMethod_str = "后台充值";
                paramMap.put("remark", "后台充值");
                break;
            case 3://account_detail business_type=sys
                rechargeMethod_str = "数据库操作";
                paramMap.put("businessType", "sys");
                break;
            case 4://补扣
                paramMap.put("remark", "资金补扣");
                break;
            case 5://数据迁移;数据转移;数据迁移step2 remark like 数据%
                paramMap.put("remark", "数据");
                break;
            default:
                break;
        }

        //用户明细
        List<DigitalCoinRechargeStatistics> digitalCoinRechargeStatisticsList = accountDetailMapper.selectDigitalCoinRechargeStatistics(pager, paramMap);
        //总体统计
        Map<String, Object> resultMap = accountDetailMapper.countDigitalCoinRechargeCountsAndTimes(paramMap);

        if (digitalCoinRechargeStatisticsList != null && digitalCoinRechargeStatisticsList.size() > 0) {
//            String str_method=rechargeMethod_str;
            digitalCoinRechargeStatisticsList.forEach(item -> {
                item.setCoinName(coin.getName());
//                item.setRechargeMethod(str_method);
            });
            rechargeCount = (BigDecimal) resultMap.get("rechargeCounts");
            rechargeTimes = (Long) resultMap.get("rechargeTimes");
            total = (Long) resultMap.get("total");
            digitalCoinRechargeStatisticsDTO.setRecords(digitalCoinRechargeStatisticsList);
        }
        digitalCoinRechargeStatisticsDTO.setCurrent(current).setSize(size).setTotal(total.intValue());
        digitalCoinRechargeStatisticsDTO.setRechargeCount(rechargeCount);
        digitalCoinRechargeStatisticsDTO.setRechargeTimes(rechargeTimes.intValue());
        digitalCoinRechargeStatisticsDTO.setRechargeMethod(rechargeMethod_str);
        digitalCoinRechargeStatisticsDTO.setCoinName(coin.getName());

        return digitalCoinRechargeStatisticsDTO;
    }
}
