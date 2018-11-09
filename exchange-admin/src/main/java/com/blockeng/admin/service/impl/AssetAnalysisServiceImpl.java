package com.blockeng.admin.service.impl;

import com.blockeng.admin.dto.AssetAnalysisDTO;
import com.blockeng.admin.entity.AssetAnalysisWithoutMining;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jakiro
 * @Date: 2018-10-14 11:22
 * @Description:资产分析实现类,分别实现是否带挖矿两种情况下的资产异常分析
 */
@Service
@Slf4j
public class AssetAnalysisServiceImpl implements AssetAnalysisService {


    @Autowired
    CoinService coinService;

    @Autowired
    AccountBalanceCountService accountBalanceCountService;

    @Autowired
    CoinRechargeService coinRechargeService;

    @Autowired
    CoinWithdrawService coinWithdrawService;

    @Autowired
    CashRechargeService cashRechargeService;

    @Autowired
    CashWithdrawalsService cashWithdrawalsService;

    @Autowired
    TurnoverOrderService turnoverOrderService;

    @Autowired
    CoinBuckleService coinBuckleService;

    @Autowired
    UserService userService;

    @Autowired
    EntrustOrderService entrustOrderService;

    @Autowired
    AccountDetailService accountDetailService;
    /**
     * @param userId
     * @return AssetAnalysisDTO
     * @description 非挖矿资产分析
     *
     *  计算公式:充值+交易-补扣-提现=总资产
     *         =>(资产转移+充值+后台充值)+(交易收入-交易支出-买入手续费-卖出手续费)-(数字币提取+场外提现)+补-扣=(总余额+冻结)
     *         其中,冻结=交易冻结+数字币提现冻结+场外提现冻结+补扣冻结;充值=币充值+法币充值(场外充值) 总充值=资金转移+充值+后台充值+资金补扣的补 一个交易 例如 一个买单 BTC/USDT 交易收入->BTC 交易支出->USDT 手续费->USDT*0.001 支出不包括手续费 单独算
     *         数字币、场外交易提出的手续费 都是从提出金额里边扣除 所以 不用计算到公式之中
     *  其中所有值都要查询出
     */
    @Override
    public AssetAnalysisDTO assetAnalysisWithoutMining(String userId) {

        //最终返回结果实体
        AssetAnalysisDTO assetAnalysisDTO=new AssetAnalysisDTO();
        //设置用户名
        assetAnalysisDTO.setUserName(userService.selectById(userId).getUsername());
        //最终返回结果的明细
        var resultMap=new HashMap<String,AssetAnalysisWithoutMining>();
        //coinId对应coinName的缓存 双向存储
        var coinIdToNameTwoWayCache=new HashMap<String,String>();
        //查询所有有效币种信息
        var coinList=coinService.getAllValidCoin();
        //缓存对应币种ID和名称
        coinList.forEach(coin -> {
            AssetAnalysisWithoutMining assetAnalysisWithoutMining=new AssetAnalysisWithoutMining();
            assetAnalysisWithoutMining.setCoinName(coin.getName());
            resultMap.put(coin.getName(),assetAnalysisWithoutMining);
            coinIdToNameTwoWayCache.put(String.valueOf(coin.getId()),coin.getName());
            coinIdToNameTwoWayCache.put(coin.getName(),String.valueOf(coin.getId()));
        });

        //查询用户
        User user=userService.selectById(userId);
        if(user!=null){
            assetAnalysisDTO.setUserName(user.getUsername());
        }else {
            return assetAnalysisDTO;
        }
        //数字币充值
        //结果 coinId(币种ID) recharge(充值总数)
        var coinRechageMap=coinRechargeService.selectRechargeByUserGroupCoin(userId);
        //法币充值
        var cashRechargeMap=cashRechargeService.selectCashRechargeByUserGroupCoin(userId);
        //总充值 （资产转移+充值+后台充值+补) recharge 币种ID coinId
        var totalRechargeMap=accountDetailService.selectAllRechargeByUser(userId);
        //查询成交订单表 volumn 买入数量,卖出数量 marketName 市场名称 fee 买入手续费,卖出手续费 作为买家的订单信息
        var orderMap=turnoverOrderService.selectBuyAndSellAndFeeGroupMarket(userId);
        //数字币提取
        var coinWithdraw=coinWithdrawService.selectCoinWithdrawGroupCoin(userId);
        //场外提取
        var cashWithdraw=cashWithdrawalsService.selectCashWithdrawGroupCoin(userId);
        //补扣中的扣
        var buckle=coinBuckleService.selectCoinBuckleGroupCoin(userId,2);
        //当前账户余额表  返回 币种ID
        var balance=accountBalanceCountService.selectBalanceByUser(userId);
        //交易冻结
        var exchangeFreeze=entrustOrderService.selectExchangeFreezeByCoin(userId);
        //数字币提现冻结
        var coinWithdrawFreeze=coinWithdrawService.selectCoinWithdrawFreezeGroupCoin(userId);
        //场外提现冻结
        var cashWithdrawFreeze=cashWithdrawalsService.selectCashWithdrawFreezeByUserGroupCoin(userId);
        //补扣冻结
        var buckleFreeze=coinBuckleService.selectBuckleFreezeByCoin(userId);

        //数字币充值
        coinRechageMap.forEach(map->{
            //币种ID
            String coinId=String.valueOf(map.get("coinId"));
            //过滤
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            //实际到账
            BigDecimal recharge=(BigDecimal)map.get("amount");
            //过滤脏数据
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            String coinName=coinIdToNameTwoWayCache.get(coinId);
            resultMap.get(coinName).setCoinRecharge(resultMap.get(coinName).getCoinRecharge().add(recharge));
        });

        //法币充值
        cashRechargeMap.forEach(map->{
            //币种ID
            String coinId=String.valueOf(map.get("coinId"));
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            //成交量
            BigDecimal mum=(BigDecimal)map.get("mum");

            String coinName=coinIdToNameTwoWayCache.get(coinId);

            if(!coinIdToNameTwoWayCache.containsKey(coinName)){
                return;
            }
            resultMap.get(coinName).setCashRecharge(resultMap.get(coinName).getCashRecharge().add(mum));
        });

        //总充值
        totalRechargeMap.forEach(map->{
            //币种
            String coinId=String.valueOf(map.get("coinId"));
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            //成交量
            BigDecimal recharge=(BigDecimal)map.get("amount");
            String remark=(String)map.get("remark");
            String coinName=coinIdToNameTwoWayCache.get(coinId);

            if(!coinIdToNameTwoWayCache.containsKey(coinName)){
                return;
            }
            resultMap.get(coinName).setTotalRecharge(resultMap.get(coinName).getTotalRecharge().add(recharge));
            if(remark.equals("充值")){//无奈的枚举判断 充值=币充值成功+法币充值成功
                resultMap.get(coinName).setRecharge(resultMap.get(coinName).getRecharge().add(recharge));
            }else if(remark.equals("后台充值")){
                resultMap.get(coinName).setBackstageRecharge(resultMap.get(coinName).getBackstageRecharge().add(recharge));
            }else if(remark.equals("资金补扣")){
                resultMap.get(coinName).setSupply(resultMap.get(coinName).getSupply().add(recharge));
            }else if(remark.startsWith("数据")){
                resultMap.get(coinName).setAssetTransfer(resultMap.get(coinName).getAssetTransfer().add(recharge));
            }
        });
        //成交单 这个位置 比较特殊 没有直接拿到coinId 拿到的为marketId 如:BTC/USDT 买入是扣USDT 加BTC 卖出是扣BTC得USDT 特殊处理
        orderMap.forEach(map->{//用户的所有买单
            String marketName=(String)map.get("marketName");
            BigDecimal volume=(BigDecimal)map.get("volume");
            BigDecimal amount=(BigDecimal)map.get("amount");
            BigDecimal fee=(BigDecimal)map.get("fee");
            Integer flag=((Long)map.get("flag")).intValue();
            // BTC/USDT -> ['BTC','USDT']
            String[] marketResults=marketName.split("/");
            if(flag==1){

                if(!coinIdToNameTwoWayCache.containsKey(marketResults[1])){
                    return;
                }
                //计算支出的/后边的币种
                resultMap.get(marketResults[1]).setExchangeExpend(resultMap.get(marketResults[1]).getExchangeExpend().add(amount));
                //买入手续费
                resultMap.get(marketResults[1]).setBuyFee(resultMap.get(marketResults[1]).getBuyFee().add(fee));
                //买到手了 要把/前边的币种加入收入
                resultMap.get(marketResults[0]).setExchangeIncome(resultMap.get(marketResults[0]).getExchangeIncome().add(volume));

            }else if(flag==2){//是卖家 总归不可能两个都不是

                if(!coinIdToNameTwoWayCache.containsKey(marketResults[0])){
                    return;
                }
                //扣掉 /前边的币种对应值
                resultMap.get(marketResults[0]).setExchangeExpend(resultMap.get(marketResults[0]).getExchangeExpend().add(volume));
                //卖出手续费 和买入一样的
                resultMap.get(marketResults[0]).setSellFee(resultMap.get(marketResults[0]).getSellFee().add(fee));
                //收入 /后边的币种的对应值
                resultMap.get(marketResults[1]).setExchangeIncome(resultMap.get(marketResults[1]).getExchangeIncome().add(amount));
            }
        });

        //数字币提现 手续费
        coinWithdraw.forEach(map->{
            //币种名称
            String coinId=String.valueOf(map.get("coinId"));
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            String coinName=coinIdToNameTwoWayCache.get(coinId);
            //提现量
            BigDecimal withdraw=(BigDecimal)map.get("num");
            //手续费 在体现里边扣的 所以计算的时候 可以不加了 不是额外的
            BigDecimal fee=(BigDecimal)map.get("fee");
            //增加数字币提现数
            resultMap.get(coinName).setCoinWithdraw(resultMap.get(coinName).getCoinWithdraw().add(withdraw));

        });

        //场外提现 手续费
        cashWithdraw.forEach(map->{
            //币种名称
            String coinId=String.valueOf(map.get("coinId"));

            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            //提现量
            BigDecimal withdraw=(BigDecimal)map.get("num");
            //手续费 在体现里边扣的 所以计算的时候 可以不加了 不是额外的
            BigDecimal fee=(BigDecimal)map.get("fee");
            //通过缓存获取币名称
            String coinName=coinIdToNameTwoWayCache.get(coinId);
            //增加数字币提现数
            resultMap.get(coinName).setCashWithdraw(resultMap.get(coinName).getCashWithdraw().add(withdraw));
        });

        //因为之前补的 在account_detail里边捎带查出来了 补扣表里 查询扣的就可以了 补扣
        buckle.forEach(map->{
           //币种ID
            String coinId=String.valueOf(map.get("coinId"));

            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            //扣数量
            BigDecimal buckleVolume=(BigDecimal)map.get("amount");

            String coinName=coinIdToNameTwoWayCache.get(coinId);
            //扣的数量加一下
            resultMap.get(coinName).setDeduct(resultMap.get(coinName).getDeduct().add(buckleVolume));
        });

        //account_detail 账户余额 冻结
        balance.forEach(map->{
            //币种ID
            String coinId=String.valueOf(map.get("coinId"));

            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            //余额
            BigDecimal balanceAmount=(BigDecimal)map.get("balance");
            //冻结
            BigDecimal freeze=(BigDecimal)map.get("freeze");
            //币名称
            String coinName=coinIdToNameTwoWayCache.get(coinId);
            resultMap.get(coinName).setBalance(resultMap.get(coinName).getBalance().add(balanceAmount));
            resultMap.get(coinName).setFreeze(resultMap.get(coinName).getFreeze().add(freeze));
        });

        //交易冻结 查出未成交的委托单
        exchangeFreeze.forEach(map->{
            //交易市场名称
            String marketName=(String)map.get("marketName");
            //冻结金额 交易冻结
            BigDecimal freeze=(BigDecimal)map.get("freeze");
            Integer type=((Long)map.get("type")).intValue();
            String[] marketNames=marketName.split("/");
            switch (type){
                case 1://买

                    if(!coinIdToNameTwoWayCache.containsKey(marketNames[1])){
                        return;
                    }
                    resultMap.get(marketNames[1]).setExchangeFreeze(resultMap.get(marketNames[1]).getExchangeFreeze().add(freeze));
                    break;
                case 2://卖

                    if(!coinIdToNameTwoWayCache.containsKey(marketNames[0])){
                        return;
                    }
                    resultMap.get(marketNames[0]).setExchangeFreeze(resultMap.get(marketNames[0]).getExchangeFreeze().add(freeze));
                    break;
                default:
                    break;
            }
        });

        //查询出 虚拟币提现待审核,审核通过,打币中状态的单子
        coinWithdrawFreeze.forEach(map->{
             String coinId=String.valueOf(map.get("coinId"));
             if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
             }
             BigDecimal num=(BigDecimal)map.get("num");
             String coinName=coinIdToNameTwoWayCache.get(coinId);
             resultMap.get(coinName).setCoinWithdrawFreeze(resultMap.get(coinName).getCoinWithdrawFreeze().add(num));
        });

        //场外提现冻结
        cashWithdrawFreeze.forEach(map->{
            String coinId=String.valueOf(map.get("coinId"));
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            BigDecimal num=(BigDecimal)map.get("num");
            String coinName=coinIdToNameTwoWayCache.get(coinId);
            resultMap.get(coinName).setCashWithdrawFreeze(resultMap.get(coinName).getCashWithdrawFreeze().add(num));
        });

        //补扣冻结
        buckleFreeze.forEach(map->{
            String coinId=String.valueOf(map.get("coinId"));
            if(!coinIdToNameTwoWayCache.containsKey(coinId)){
                return;
            }
            BigDecimal amount=(BigDecimal)map.get("amount");
            String coinName=coinIdToNameTwoWayCache.get(coinId);
            resultMap.get(coinName).setBuckleFreeze(resultMap.get(coinName).getBuckleFreeze().add(amount));
        });
        assetAnalysisDTO.setResultMap(resultMap);
        return doAnalysisWithoutMining(assetAnalysisDTO);
    }

    
    /**
     * @param userId
     * @return Map<String,Map<String,BigDecimal>>
     * @description 挖矿资产分析
     */
    @Override
    public AssetAnalysisDTO assetAnalysisWithMining(String userId) {

        return new AssetAnalysisDTO();
    }


    /**
     * @param assetAnalysisDTO
     * @return assetAnalysisDTO
     * @description (资产转移+充值+后台充值)+(交易收入-交易支出-买入手续费-卖出手续费)-(数字币提取+场外提现)+补-扣=(总余额+冻结)
     * 其中,交易冻结+数字币提现冻结+场外提现冻结+补扣冻结=冻结,充值=币充值+法币充值(场外充值) 总充值=资金转移+充值+后台充值+资金补扣的补 先按公式计算
     * 验证一:资产等式是否成立
     * 验证二:冻结等式是否成立
     */
    public AssetAnalysisDTO doAnalysisWithoutMining(AssetAnalysisDTO assetAnalysisDTO){
        //用于原因分析计数
        AtomicInteger index=new AtomicInteger(1);

        StringBuffer sb=new StringBuffer("");
        var resultMap=assetAnalysisDTO.getResultMap();
        resultMap.forEach((k,v)->{
            //数字币充值
            BigDecimal coinRecharge=v.getCoinRecharge();
            //场外充值
            BigDecimal totalRecharge=v.getCashRecharge();
            //资产转移
            BigDecimal assetTransfer=v.getAssetTransfer();
            //充值
            BigDecimal recharge=v.getRecharge();
            //后台充值
            BigDecimal backstageRecharge=v.getBackstageRecharge();
            //交易收入
            BigDecimal exchangeIncome=v.getExchangeIncome();
            //交易支出
            BigDecimal exchangeExpend=v.getExchangeExpend();
            //买入手续费
            BigDecimal buyFee=v.getBuyFee();
            //卖出手续费
            BigDecimal sellFee=v.getSellFee();
            //数字比提取成功
            BigDecimal coinWithdraw=v.getCoinWithdraw();
            //场外提现成功
            BigDecimal cashWithdraw=v.getCashWithdraw();
            //补扣中的补款
            BigDecimal supply=v.getSupply();
            //补扣中的扣款
            BigDecimal deduct=v.getDeduct();
            //当前总余额
            BigDecimal balance=v.getBalance();
            //总冻结
            BigDecimal freeze=v.getFreeze();
            //交易冻结
            BigDecimal exchangeFreeze=v.getFreeze();
            //数字币提现冻结
            BigDecimal coinWithdrawFreeze=v.getCoinWithdrawFreeze();
            //场外提现冻结
            BigDecimal cashWithdrawFreeze=v.getCashWithdrawFreeze();
            //补扣冻结
            BigDecimal buckleFreeze=v.getBuckleFreeze();

            /*---------------资产校验-------------------*/
            //(充值+资产转移+后台充值)+(交易收入-交易支出-买入手续费-卖出手续费)-(数字币提取+场外提现)+补-扣=(总余额+冻结)
            //等式左边 流动资产
            BigDecimal equationLeftAsset=recharge.add(assetTransfer).add(backstageRecharge)
                    .add(exchangeIncome).subtract(exchangeExpend).subtract(buyFee).subtract(sellFee).subtract(coinWithdraw)
                    .subtract(cashWithdraw).add(supply).subtract(deduct);
            //等式右边 剩余资产
            BigDecimal equationRightAsset=balance.add(freeze);
            //左-右
            BigDecimal resultAsset=equationLeftAsset.subtract(equationRightAsset);
            Double differAsset=resultAsset.doubleValue();

            if(differAsset==0){
                //没问题
            }else if(differAsset>0){
                //流动资产 大于余额 最后加分号 利于前台排版
                assetAnalysisDTO.setUnusual(true);
                sb.append(index.getAndIncrement()+"."+"币种"+k+"账户余额少"+differAsset).append(";");
            }else if(differAsset<0){
                //流动资产 小于余额
                assetAnalysisDTO.setUnusual(true);
                sb.append(index.getAndIncrement()+"."+"币种"+k+"账户余额多"+Math.abs(differAsset)).append(";");
            }
            /*-----------------------------------------*/

            /*---------------冻结校验-------------------*/
            //交易冻结+数字币提现冻结+场外提现冻结+补扣冻结=冻结
            //等式左边 流动冻结
            BigDecimal equationLeftFreeze=exchangeFreeze.add(coinWithdrawFreeze).add(cashWithdrawFreeze).add(buckleFreeze);
            //等式右边 总冻结
            BigDecimal equationRightFreeze=freeze;
            //左-右
            BigDecimal resultFreeze=equationLeftFreeze.subtract(equationRightFreeze);
            Double differFreeze=resultFreeze.doubleValue();

            if(differFreeze==0){
                //没问题
            }else if(differFreeze>0){
                //流动冻结>总冻结 最后加分号 利于前台排版
                assetAnalysisDTO.setUnusual(true);
                sb.append(index.getAndIncrement()+"."+"币种"+k+"流动冻结比总冻结多"+differFreeze).append(";");
            }else if(differFreeze<0){
                //流动冻结<总冻结
                assetAnalysisDTO.setUnusual(true);
                sb.append(index.getAndIncrement()+"."+"币种"+k+"流动冻结比总冻结少"+Math.abs(differFreeze)).append(";");
            }
            /*-----------------------------------------*/

            //分析结论
            assetAnalysisDTO.setAnalyticResult(sb.toString());

        });
        return assetAnalysisDTO;
    }





}
