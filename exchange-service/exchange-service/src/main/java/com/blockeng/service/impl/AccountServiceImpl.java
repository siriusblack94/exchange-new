package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.*;
import com.blockeng.entity.*;
import com.blockeng.enums.MessageChannel;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.ApplyWithdrawDTO;
import com.blockeng.framework.dto.WalletResultCode;
import com.blockeng.framework.dto.WalletResultDTO;
import com.blockeng.framework.enums.*;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.DateUtil;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mapper.AccountMapper;
import com.blockeng.service.*;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.UserServiceClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户财产记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService, Constant {

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private RandCodeService randCodeService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RewardRecordService rewardRecordService;

    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId   用户ID
     * @param coinName 币种名称
     * @return
     */
    @Override
    public Account selectByUserAndCoinName(long userId, String coinName) {
        return baseMapper.selectByUserAndCoinName(userId, coinName);
    }

    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    @Override
    public Account queryByUserIdAndCoinId(long userId, long coinId) {
        return queryByUserIdAndCoinId(userId, coinId, false);
    }


    /**
     * 根据用户ID和币种名称查询资金账户
     *
     * @param userId 用户ID
     * @param coinId 币种ID
     * @return
     */
    @Override
    public Account queryByUserIdAndCoinId(long userId, long coinId, boolean containEnable) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("coin_id", coinId);
        if (!containEnable) {
            wrapper.eq("status", BaseStatus.EFFECTIVE.getCode());
        }
        wrapper.last("LIMIT 1");
        return baseMapper.selectOne(wrapper);
/*        List<Account> accountList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        }
        return accountList.get(0);*/
    }

    /**
     * 冻结资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean lockAmount(long userId,
                              long coinId,
                              BigDecimal amount,
                              BusinessType businessType,
                              long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("冻结资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new AccountException("资金账户异常");
        }
        if (baseMapper.lockAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    "冻结");
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("LockAmount Error.冻结资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new AccountException("可用资金不足");
    }

    /**
     * 解冻资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean unlockAmount(long userId,
                                long coinId,
                                BigDecimal amount,
                                BusinessType businessType,
                                long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("解冻资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new AccountException("资金账户异常");
        }
        if (baseMapper.unlockAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.INCOME.getType(),
                    businessType.getCode(),
                    amount,
                    "解冻");
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("UnlockAmount Error.解冻资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new AccountException("解冻资金失败");
    }

    /**
     * 增加资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean addAmount(long userId,
                             long coinId,
                             BigDecimal amount,
                             BusinessType businessType,
                             String remark,
                             long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("增加资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new com.blockeng.framework.exception.AccountException("资金账户异常");
        }
        if (baseMapper.addAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.INCOME.getType(),
                    businessType.getCode(),
                    amount,
                    remark);
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("增加资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new com.blockeng.framework.exception.AccountException("增加资金失败");
    }

    /**
     * 增加资金(注册奖励，推荐奖励，首次充值)
     */
    @Override
    public boolean addAmountReward(long userId,
                                long coinId,
                                BigDecimal amount,
                                BusinessType businessType,
                                String remark) throws AccountException {

        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("增加奖励资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new com.blockeng.framework.exception.AccountException("资金账户异常");
        }
        if (baseMapper.addAmount(account.getId(), amount) > 0) {

            RewardRecord rewardRecord = new RewardRecord(userId,
                    coinId,
                    account.getId(),
                    businessType.getCode(),
                    amount,
                    remark);

            rewardRecordService.insert(rewardRecord);
            return true;
        }

        log.error("增加奖励资金， userId:{}, coinId:{}, amount:{}, businessType:{}",
                userId, coinId, amount, businessType.getCode());
        throw new com.blockeng.framework.exception.AccountException("增加奖励资金失败");
    }

    /**
     * 扣减资金 -> 提现审核通过 解冻 资金变化方法
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean subtractAmount(long userId,
                                  long coinId,
                                  BigDecimal amount,
                                  BusinessType businessType,
                                  String remark,
                                  long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("扣减资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new com.blockeng.framework.exception.AccountException("资金账户异常");
        }
        if (baseMapper.withdrawAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    remark);
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("扣减资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new com.blockeng.framework.exception.AccountException("扣减资金失败");
    }


    /**
     * 扣减资金
     *
     * @param userId       用户ID
     * @param coinId       币种ID
     * @param amount       冻结金额
     * @param businessType 业务类型
     * @param remark       备注
     * @param orderId      关联订单号
     * @return
     */
    @Override
    public boolean subtractBalanceAmount(long userId, long coinId, BigDecimal amount, BusinessType businessType, String remark, long orderId) throws AccountException {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("扣减资金-资金账户异常，userId:{}, coinId:{}", userId, coinId);
            throw new com.blockeng.framework.exception.AccountException("资金账户异常");
        }

        if(account.getBalanceAmount().compareTo(amount)==-1){
            //证明 余额比要扣除的金额小
            log.error("扣减资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                    orderId, userId, coinId, amount, businessType.getCode());
            throw new com.blockeng.framework.exception.AccountException("账户余额不足");

        }
        if (baseMapper.subtractBalanceAmount(account.getId(), amount) > 0) {
            // 保存流水
            AccountDetail accountDetail = new AccountDetail(userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    AmountDirection.OUT.getType(),
                    businessType.getCode(),
                    amount,
                    remark);
            accountDetailService.insert(accountDetail);
            return true;
        }
        log.error("扣减资金失败，orderId:{}, userId:{}, coinId:{}, amount:{}, businessType:{}",
                orderId, userId, coinId, amount, businessType.getCode());
        throw new com.blockeng.framework.exception.AccountException("扣减资金失败");
    }

    /**
     * 币币交易用户交易对账户资产
     *
     * @param symbol 交易对标识符
     * @param userId 用户ID
     */
    @Override
    public SymbolAssetDTO queryAccount(String symbol, long userId) {
        Market market = marketService.queryBySymbol(symbol);
        long priceCoinId = market.getBuyCoinId();
        long baseCoinId = market.getSellCoinId();
        Coin priceCoin = coinService.queryById(priceCoinId);
        Coin baseCoin = coinService.queryById(baseCoinId);
        Account priceCoinAccount = this.queryByUserIdAndCoinId(userId, priceCoinId);
        Account baseCoinAccount = this.queryByUserIdAndCoinId(userId, baseCoinId);
        SymbolAssetDTO symbolAsset = new SymbolAssetDTO();
        symbolAsset.setBuyAmount(priceCoinAccount.getBalanceAmount())
                .setBuyLockAmount(priceCoinAccount.getFreezeAmount().add(priceCoinAccount.getLockMargin()))
                .setBuyFeeRate(market.getFeeBuy())
                .setBuyUnit(priceCoin.getName())
                .setSellAmount(baseCoinAccount.getBalanceAmount())
                .setSellLockAmount(baseCoinAccount.getFreezeAmount().add(baseCoinAccount.getLockMargin()))
                .setSellFeeRate(market.getFeeSell())
                .setSellUnit(baseCoin.getName());
        return symbolAsset;
    }

    /**
     * 获取钱包充值地址
     *
     * @param coinId 币种ID
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional
    public String queryRechargeAddress(Long coinId, Long userId) {
        Account account = this.queryByUserIdAndCoinId(userId, coinId);
        if (account == null) {
            log.error("请求参数错误");
            throw new GlobalDefaultException(10001);
        }
        if (!StringUtils.isEmpty(account.getRecAddr())) {
            return account.getRecAddr();
        }
        GetAddressDTO getAddressDTO = new GetAddressDTO(userId, coinId);
        Object result = rabbitTemplate.convertSendAndReceive(MessageChannel.RECHARGE_ADDRESS.getChannel(), GsonUtil.toJson(getAddressDTO));
        WalletResultDTO walletResultDTO = GsonUtil.convertObj(result.toString(), WalletResultDTO.class);
        // 调用钱包服务获取充值地址
        if (null == walletResultDTO || walletResultDTO.getStatusCode() != WalletResultCode.SUCCESS.getCode() || StringUtils.isEmpty(walletResultDTO.getResult())) {
            log.error("获取钱包充值地址失败");
            throw new GlobalDefaultException(walletResultDTO.getResultDesc());
        }
        baseMapper.setRechargeAddress(account.getId(), walletResultDTO.getResult().toString());
        return walletResultDTO.getResult().toString();
    }

    /**
     * 申请提币
     *
     * @param applyWithdraw 申请提币参数
     * @param userId        用户ID
     */
    @Override
    public Response applyWithdraw(ApplyWithdrawDTO applyWithdraw, Long userId) {
        DecimalFormat format = new DecimalFormat("#.########");
        // 验证短信验证码
        UserDTO user = userServiceClient.selectById(userId);
        if (user.getAuthStatus() == null || user.getAuthStatus() != 2) {
            log.error("用户尚未完成高级实名认证");
            throw new GlobalDefaultException(50029);
        }
        // 校验资金密码
        if (!new BCryptPasswordEncoder().matches(applyWithdraw.getPayPassword(), user.getPaypassword())) {
            log.error("资金密码错误");
            throw new GlobalDefaultException(2012);
        }
        Coin coin = coinService.queryById(applyWithdraw.getCoinId());
        if (coin == null) {
            log.error("提现币种错误");
            throw new GlobalDefaultException(-2);
        }
        BigDecimal applyAmount = applyWithdraw.getAmount();
        // day_max_amount 当日最大提现数量
        String drawKey = "APPLY_WITHDRAW:" + userId;
        BigDecimal dayMaxAmount = applyAmount;
        String dayMaxAmountString = stringRedisTemplate.opsForValue().get(drawKey);
        if (!StringUtils.isEmpty(dayMaxAmountString)) {
            dayMaxAmount = new BigDecimal(dayMaxAmountString).add(applyAmount);
        }
        if (dayMaxAmount.compareTo(coin.getDayMaxAmount()) > 0) {
            log.error("当天提现总额不能大于当天最大提现数量");
            //throw new GlobalDefaultException(20012);
            return Response.err(20012, "超出单日提现上限：" + format.format(coin.getDayMaxAmount()) + coin.getName());
        }
        // max_amount 单笔最大提现数量
        if (applyAmount.compareTo(coin.getMaxAmount()) > 0) {
            log.error("提现数量不能大于单笔最大提现数量");
            //throw new GlobalDefaultException(20013);
            return Response.err(20013, "超出单笔提现上限" + format.format(coin.getMaxAmount()) + coin.getName());
        }
        // min_amount 单笔最小提现数量
        if (applyAmount.compareTo(coin.getMinAmount()) < 0) {
            log.error("提现数量不能小于单笔最小提现数量");
            //throw new GlobalDefaultException(20015);
            return Response.err(20015, "超出单笔提现下限" + format.format(coin.getMinAmount()) + coin.getName());
        }
        // base_amount 最小提现单位
        BigDecimal base[] = applyAmount.divideAndRemainder(coin.getBaseAmount());
        if (base[1].compareTo(BigDecimal.ZERO) > 0) {
            log.error("提现数量应为最小提现单位整数倍");
            //throw new GlobalDefaultException(20014);
            return Response.err(20014, "提现数量应为最小提现单位" + coin.getBaseAmount() + "整数倍");
        }
        //校验短信验证码
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(user.getCountryCode())
                .setTemplateCode(SmsTemplate.WITHDRAW_APPLY.getCode())
                .setPhone(user.getMobile())
                .setEmail(user.getEmail())
                .setCode(applyWithdraw.getVerifyCode());
        boolean verifyResult = randCodeService.verify(randCodeVerifyDTO);
        if (!verifyResult) {
            log.error("短信验证码错误");
            throw new GlobalDefaultException(50021);
        }
        CoinWithdraw coinWithdraw = new CoinWithdraw();
        UserWallet userWallet;
        String address;
        // 如果选择了地址
        if (applyWithdraw.getAddressId() != null && applyWithdraw.getAddressId() > 0L) {
            userWallet = userWalletService.selectById(applyWithdraw.getAddressId());
            if (userWallet == null || StringUtils.isEmpty(userWallet.getAddr())) {
                throw new GlobalDefaultException(50022);
            }
            address = userWallet.getAddr();
        } else {
            address = applyWithdraw.getAddress();
            // 自动保存
            userWallet = new UserWallet();
            userWallet.setUserId(userId)
                    .setCoinId(applyWithdraw.getCoinId())
                    .setCoinName(coin.getName())
                    .setName("")
                    .setAddr(address)
                    .setSort(0)
                    .setStatus(BaseStatus.EFFECTIVE.getCode());
            userWalletService.insert(userWallet);
        }
        // 提现实际转出金额
        if (applyWithdraw.getAmount().compareTo(coin.getMinFeeNum()) < 0) {
            log.error("提现数量需大于提现手续费");
            throw new GlobalDefaultException(50057);
        }
        BigDecimal rate = new BigDecimal(coin.getRate());
        BigDecimal ratefee = applyWithdraw.getAmount().multiply(rate);
        //最小提现额度，修改为根据手续费率提现
        if (ratefee.compareTo(coin.getMinFeeNum()) <= 0) {
            ratefee = coin.getMinFeeNum();
        }
        BigDecimal amount = applyWithdraw.getAmount().subtract(ratefee);
        coinWithdraw.setUserId(userId)
                .setCoinId(coin.getId())
                .setCoinName(coin.getName())
                .setCoinType(coin.getType())
                .setAddress(address)
                .setStep(1)
                .setStatus(CoinWithdrawStatus.PENDING.getCode())
                .setNum(applyWithdraw.getAmount())
                .setFee(ratefee)
                .setMum(amount);
        coinWithdrawService.insert(coinWithdraw);
        try {
            //设置24提现总额 刷新KEY
            stringRedisTemplate.opsForValue().set(drawKey, dayMaxAmount.toString());
            long expireTime = DateUtil.getCurrentLeaveTime();
            stringRedisTemplate.expire(drawKey, expireTime, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            log.error("获取当天过期时间异常");
        }
        // 冻结资金时应该带上手续费
        this.lockAmount(userId, coin.getId(), applyWithdraw.getAmount(), BusinessType.WITHDRAW, coinWithdraw.getId());
        return Response.ok();
    }

    /**
     * 初始化资金账户
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional
    public boolean syncAccount(long userId) {
        boolean returnFlag = false;
        List<Coin> coinList = coinService.selectList(new QueryWrapper<>());
        List<Account> tradeAccounts = new ArrayList<>();
        for (Coin coin : coinList) {
            Account account = new Account(userId, coin.getId(), BaseStatus.EFFECTIVE.getCode(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", 0L, new Date(), new Date());
            tradeAccounts.add(account);
        }
        if (!CollectionUtils.isEmpty(tradeAccounts)) {
            returnFlag = this.insertBatch(tradeAccounts);
        }
        return returnFlag;
    }

    @Override
    public boolean unfreezeInviteRewards(long userId) {
        Config config = configService.queryByTypeAndCode(MiningConfig.TYPE.getValue(), MiningConfig.CODE_COIN_ID.getValue());
        Long coinId = Long.parseLong(config.getValue());
        MongoCollection inviteRewardsAccountCollection = mongoTemplate.getCollection("invite_rewards_account");
        Document query = new Document("user_id", userId);
        Document document = mongoTemplate.getCollection("invite_rewards_account").find(query).first();
        BigDecimal canDefrost = document.get("can_defrost", Decimal128.class).bigDecimalValue();
        addAmount(userId, coinId, canDefrost, BusinessType.BONUS_UNLOCK, "邀请奖励解冻", 0L);
        Document update = new Document();
        update.put("$set", new Document("can_defrost", BigDecimal.ZERO));
        inviteRewardsAccountCollection.updateOne(query, update);
        return false;
    }

    /**
     * 统计账户资产
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public Map<String, Object> countAssets(Long userId) {
        Map<String, Object> map = new HashMap<>();
        // 总资产
        BigDecimal total = BigDecimal.ZERO;
        // 获取平台币
        QueryWrapper<Config> wrapper = new QueryWrapper<>();
        wrapper.eq("type", CONFIG_TYPE_SYSTEM).eq("code", "PLATFORM_COIN_ID");
        Config config = configService.selectOne(wrapper);
        if (config == null) {
            log.error("没有配置平台资产统计币种");
            return map;
        }
        // 获取资产统计币种的ID
        long baseCoinId = Long.parseLong(config.getValue());
        List<List<Market>> marketList = this.getAllMarket(baseCoinId);
        if (CollectionUtils.isEmpty(marketList)) {
            return map;
        }
        List<Market> usdtMarketList = marketList.get(0);
        List<Market> otherMarketList = new ArrayList<>();
        if (marketList.size() == 2) {
            otherMarketList = marketList.get(1);
        }
        // 资金账户列表
        List<AccountDTO> accounts = new ArrayList<>();
        QueryWrapper<Account> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        List<Account> accountList = baseMapper.selectList(ew);
        for (Account account : accountList) {
            AccountDTO accountDTO = AccountDTOMapper.INSTANCE.from(account);
            Long coinId = account.getCoinId();
            BigDecimal volume = account.getBalanceAmount().add(account.getFreezeAmount());
            // 币种信息
            Coin coin = coinService.queryById(account.getCoinId());
            if (coin == null || coin.getStatus() != BaseStatus.EFFECTIVE.getCode()) {
                // 过滤无效币种
                continue;
            }
            accountDTO.setCoinImgUrl(coin.getImg())             // 币种图片路径
                    .setCoinName(coin.getName())                // 币种名称
                    .setFeeRate(new BigDecimal(coin.getRate())
                            .setScale(4, RoundingMode.HALF_UP))    // 提现手续费
                    .setRechargeFlag(coin.getRechargeFlag())    // 充值开关
                    .setWithdrawFlag(coin.getWithdrawFlag())    // 提现开关
                    .setCoinType(coin.getType())                // 币种类型
                    .setMinFeeNum(coin.getMinFeeNum())          // 提币手续费
                    .setCarryingAmount(accountDTO.getBalanceAmount()
                            .add(accountDTO.getFreezeAmount())); //账面余额
            accounts.add(accountDTO);
            // 计算总资产
            if (volume.compareTo(BigDecimal.ZERO) <= 0) {
                // 资产为0
                continue;
            }
            // 最新成交价(USDT)
            BigDecimal currentPrice;
            if (account.getCoinId().equals(baseCoinId)) {
                currentPrice = BigDecimal.ONE;
            } else {
                // 当前币种对USDT交易对
                Market market = this.getMarket(baseCoinId, coinId, usdtMarketList);
                if (market == null) {
                    // 当前币种对非USDT交易对
                    market = this.getMarket(coinId, otherMarketList);
                    if (market == null) {
                        continue;
                    }
                    TradeMarketDTO tradeMarket = marketService.getTradeMarketById(market.getId());
                    currentPrice = tradeMarket.getPrice();
                    market = this.getMarket(market.getBuyCoinId(), otherMarketList);
                    if (market == null) {
                        continue;
                    }
                    tradeMarket = marketService.getTradeMarketById(market.getId());
                    currentPrice = currentPrice.multiply(tradeMarket.getPrice());
                } else {
                    TradeMarketDTO tradeMarket = marketService.getTradeMarketById(market.getId());
                    currentPrice = tradeMarket.getPrice();
                }
            }
            // 当前资金账户总资产（单位：USDT） = 币总量 * 币价格（单位：USDT）
            BigDecimal amount = volume.multiply(currentPrice);
            total = total.add(amount);
        }
        // CNY/USDT兑换比例
        BigDecimal cny2usdtRate = BigDecimal.ONE;
        // 获取美元兑换率
        wrapper = new QueryWrapper<>();
        wrapper.eq("type", CONFIG_TYPE_CNY).eq("code", CONFIG_CNY2USDT);
        config = configService.selectOne(wrapper);
        if (config != null) {
            cny2usdtRate = new BigDecimal(config.getValue());
        }
        // 总资产美元
        BigDecimal totalCny = total.multiply(cny2usdtRate);
        total.setScale(2, RoundingMode.HALF_UP);       // 保留两位小数
        totalCny.setScale(2, RoundingMode.HALF_UP);    // 保留两位小数
        wrapper = new QueryWrapper<>();
        wrapper.eq("type", CONFIG_TYPE_SYSTEM).eq("code", "PLATFORM_COIN_NAME");
        String coinName = "";
        config = configService.selectOne(wrapper);
        if (config != null) {
            coinName = config.getValue();
        }
        map.put("amount", totalCny.doubleValue());
        map.put("amountUs", total.doubleValue());
        map.put("amountUsUnit", coinName);
        map.put("assertList", accounts);
        return map;
    }

    /**
     * 获取对usdt交易对
     *
     * @param buyCoinId  报价币种
     * @param sellCoinId 卖出币种
     * @param markets    交易对集合
     * @return
     */
    private Market getMarket(Long buyCoinId, Long sellCoinId, List<Market> markets) {
        for (Market market : markets) {
            if (market.getBuyCoinId().equals(buyCoinId)
                    && market.getSellCoinId().equals(sellCoinId)) {
                return market;
            }
        }
        return null;
    }

    /**
     * 获取对usdt交易对
     *
     * @param sellCoinId 卖出币种
     * @param markets    交易对集合
     * @return
     */
    private Market getMarket(Long sellCoinId, List<Market> markets) {
        for (Market market : markets) {
            if (market.getSellCoinId().equals(sellCoinId)) {
                return market;
            }
        }
        return null;
    }

    /**
     * 获取所有的交易对, 去除各个区之间的重叠
     *
     * @return
     */
    public List<List<Market>> getAllMarket(Long baseCoinId) {
        List<List<Market>> marketList = new ArrayList<>();
        List<Market> usdtMarketList = new ArrayList<>();
        List<Market> otherMarketList = new ArrayList<>();
        // 获取交易区域 得到市场buyId
        QueryWrapper<Market> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .orderByAsc("trade_area_id")
                .orderByAsc("sort");
        List<Market> markets = marketService.selectList(wrapper);
        for (Market market : markets) {
            if (market.getBuyCoinId().equals(baseCoinId)) {
                usdtMarketList.add(market);
            } else {
                otherMarketList.add(market);
            }
        }
        marketList.add(usdtMarketList);
        marketList.add(otherMarketList);
        return marketList;
    }
}
