package com.blockeng.admin.web.funds.buckle;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.BuckleAccountCountDTO;
import com.blockeng.admin.dto.BuckleCountDTOPage;
import com.blockeng.admin.dto.CoinBuckleDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.CoinBuckle;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountService;
import com.blockeng.admin.service.CoinBuckleService;
import com.blockeng.admin.service.CoinService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/buckle")
public class CoinBuckleController {

    @Autowired
    CoinBuckleService coinBuckleService;

    @Autowired
    CoinService coinService;

    @Autowired
    AccountService accountService;

    ReentrantLock addBucklelock = new ReentrantLock(true);

    CoinBuckle lastAddedBuckle;

    @Log(value = "补扣管理列表", type = SysLogTypeEnum.SELECT)
    @ApiOperation(value = "补扣管理列表")
    @GetMapping
    @PreAuthorize("hasAuthority('account_buckle_query')")
    public Object selectPage(@ApiIgnore Page<CoinBuckleDTO> page,
                             String userName, String mobile,
                             String status, String userId,
                             String startTime, String endTime) {
        EntityWrapper<CoinBuckleDTO> ew = new EntityWrapper<>();

        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("b.id", userId);
        }
        if (!Strings.isNullOrEmpty(userName)) {
            ew.eq("b.username", userName);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.eq("b.mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("a.status", status);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.between("a.created", startTime, endTime);
        }

        int size = page.getSize();
        int current = (page.getCurrent() - 1) * size;

        page.setSize(size).setCurrent(current);
        List<CoinBuckleDTO> list = coinBuckleService.selectListPage(page, ew);
        int total = coinBuckleService.selectListPageCount(page, ew);

        return ResultMap.getSuccessfulResult(page.setRecords(list).setTotal(total).setSize(current + 1).setSize(size));
    }

    @Log(value = "增加补扣", type = SysLogTypeEnum.INSERT)
    @ApiOperation(value = "增加补扣")
    @PostMapping("/addCoinBuckle")
    @PreAuthorize("hasAuthority('account_buckle_add')")
    public ResultMap addCoinBuckle(@RequestBody CoinBuckle coinBuckle) {

        if (coinBuckle.getUserId() == null) {
            throw new ExchangeException("用户ID为空");
        }
        if (coinBuckle.getAmount() == null) {
            throw new ExchangeException("金额为空");
        }
        if (coinBuckle.getCoinId() == null) {
            throw new ExchangeException("币种为空");
        }
        if (!StringUtils.isNotBlank(coinBuckle.getReason())) {
            throw new ExchangeException("原因为空");
        }

        try {
            addBucklelock.lock(); //如果分布式应用，请换分布式锁,那么对象应该放入缓存
            if (lastAddedBuckle != null
                    && lastAddedBuckle.getCoinId().longValue() == coinBuckle.getCoinId().longValue()
                    && lastAddedBuckle.getUserId().longValue() == coinBuckle.getUserId().longValue()
                    && lastAddedBuckle.getAmount().compareTo(coinBuckle.getAmount()) == 0
                    && lastAddedBuckle.getDescription().equals(coinBuckle.getDescription())
                    && lastAddedBuckle.getReason().equals(coinBuckle.getReason())
                    && lastAddedBuckle.getRemark().equals(coinBuckle.getRemark())
                    && lastAddedBuckle.getType().equals(coinBuckle.getType())) {
                throw new ExchangeException("重复提交");
            }
            lastAddedBuckle = coinBuckle;
            if (coinBuckleService.addCoinBuckle(coinBuckle)) {
                CoinBuckle cb = coinBuckleService.selectById(coinBuckle.getId());
                cb.setUserId(coinBuckle.getUserId());
                cb.setCoinId(coinBuckle.getCoinId());
                return ResultMap.getSuccessfulResult(cb);
            }
        } finally {
            addBucklelock.unlock();
        }
        return ResultMap.getFailureResult();
    }

    @Log(value = "补扣审批", type = SysLogTypeEnum.AUDIT)
    @ApiOperation(value = "补扣审批")
    @PostMapping("/coinBuckleAudit")
    public ResultMap coinBuckleAudit(@RequestBody AuditDTO auditDTO, @ApiIgnore @AuthenticationPrincipal SysUser sysUser) {
        if (coinBuckleService.audit(auditDTO, sysUser))
            return ResultMap.getSuccessfulResult("审核成功");
        return ResultMap.getFailureResult("审核失败");
    }

    @Log(value = "补扣详细信息", type = SysLogTypeEnum.SELECT)
    @ApiOperation(value = "补扣详细信息")
    @GetMapping("/queryInfo")
    @PreAuthorize("hasAuthority('account_buckle_info_query')")
    public ResultMap queryInfo(Long orderId) {
        CoinBuckle coinBuckle = coinBuckleService.selectById(orderId);
        Account account = accountService.selectById(coinBuckle.getAccountId());
        coinBuckle.setCoinId(account.getCoinId());
        coinBuckle.setUserId(account.getUserId());
        return ResultMap.getSuccessfulResult(coinBuckle);
    }

    @Log(value = "补扣统计", type = SysLogTypeEnum.SELECT)
    @ApiOperation(value = "补扣统计")
    @GetMapping("/coinBuckleCountTotal")
    @PreAuthorize("hasAuthority('buckle_query')")
    public ResultMap coinBuckleCountTotal(String startTime, String endTime, String userId, String coinId, String current, String size) {

        EntityWrapper<BuckleAccountCountDTO> ew = new EntityWrapper<>();

        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("d.user_id", userId);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("d.coin_id", coinId);
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
            ew.between("a.audit_time", startTime, endTime);
        }
        BigDecimal sumTotal = coinBuckleService.selectSumTotal(ew);
        BigDecimal subTotal = coinBuckleService.selectSubTotal(ew);
        BigDecimal total = sumTotal.subtract(subTotal);

        int s = Integer.valueOf(size);
        int t = 0;
        int c = (Integer.valueOf(current).intValue() - 1) * s;
        List<BuckleAccountCountDTO> buckleAccountCounts = coinBuckleService.selectBuckleAccountCounts(c, s, ew);
        t = coinBuckleService.selectBuckleAccountCountsTotal(ew);
        Coin coin = coinService.selectById(coinId);
        BuckleCountDTOPage<BuckleAccountCountDTO> page = new BuckleCountDTOPage<>(sumTotal, subTotal, total, coin.getName());
        page.setRecords(buckleAccountCounts).setSize(s).setCurrent(c + 1).setTotal(t);
        return ResultMap.getSuccessfulResult(page);
    }

    @Log(value = "补扣管理列表导出", type = SysLogTypeEnum.EXPORT)
    @ApiOperation(value = "补扣管理列表导出")
    @GetMapping("/exportSelectPage")
    @PreAuthorize("hasAuthority('account_buckle_export')")
    public void exportSelectPage(String userName, String mobile,
                                 String status, String userId,
                                 String startTime, String endTime, HttpServletResponse response) {

        EntityWrapper<CoinBuckleDTO> ew = new EntityWrapper<>();

        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("b.id", userId);
        }
        if (!Strings.isNullOrEmpty(userName)) {
            ew.eq("b.username", userName);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.eq("b.mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("a.status", status);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.between("a.created", startTime, endTime);
        }
        Page page = new Page();

        int s = 500000;
        int c = 0;
        page.setSize(s);
        page.setCurrent(0);
        List<CoinBuckleDTO> list = coinBuckleService.selectListPage(page, ew);

        String[] header = {"单号", "用户ID", "用户名", "补扣原因", "币种", "补充类型", "金额", "创建时间", "完成时间", "状态", "一审", "二审"};
        String[] properties = {"id", "userId", "userName", "reason", "coinName", "type", "amount", "created", "auditTime", "status", "auditNameFirst", "auditNameSecond"};

        CellProcessor[] PROCESSORS = new CellProcessor[]{
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);

                        }
                        return "\t" + v;
                    }
                },
                null,
                null,
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
        };

        String fileName = "补扣详情.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, list, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Log(value = "补扣导出统计", type = SysLogTypeEnum.EXPORT)
    @ApiOperation(value = "补扣导出统计")
    @GetMapping("/exportCoinBuckleCountTotal")
    @PreAuthorize("hasAuthority('buckle_export')")
    public void exportCoinBuckleCountTotal(HttpServletResponse response, String startTime, String endTime, String userId, String coinId) {
        int current = 0;
        int size = 500000;

        EntityWrapper<BuckleAccountCountDTO> ew = new EntityWrapper<>();

        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("d.user_id", userId);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("d.coin_id", coinId);
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
            ew.between("a.audit_time", startTime, endTime);
        }
        BigDecimal sumTotal = coinBuckleService.selectSumTotal(ew);
        BigDecimal subTotal = coinBuckleService.selectSubTotal(ew);
        BigDecimal total = subTotal.subtract(subTotal);
        List<BuckleAccountCountDTO> buckleAccountCounts = coinBuckleService.selectBuckleAccountCounts(current, size, ew);
        Coin coin = coinService.selectById(coinId);
        buckleAccountCounts.add(0, new BuckleAccountCountDTO(0L, total, sumTotal, subTotal, coin.getName()));
        String[] header = {"用户ID", "钱包类型", "总扣补数", "补充", "扣减"};
        String[] properties = {"userId", "coinName", "total", "sumTotal", "subTotal"};

        CellProcessor[] PROCESSORS = new CellProcessor[]{
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
//                        String v = "\t" + String.valueOf(value);
                        return coin.getName();
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);

                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);

                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);

                        }
                        return "\t" + v;
                    }
                },
        };

        String fileName = "补扣统计.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, buckleAccountCounts, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
