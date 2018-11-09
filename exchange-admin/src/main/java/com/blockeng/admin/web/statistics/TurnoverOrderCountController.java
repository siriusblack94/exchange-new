package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.TurnoverOrderCountDTO;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.TurnoverOrderService;
import com.blockeng.admin.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create Time: 2018年06月01日 15:14
 * 交易统计
 *
 * @author lxl
 * @Dec
 **/

@Slf4j
@Api(value = "交易统计controller", tags = {"交易统计"})
@RestController
@RequestMapping("/turnoverOrderCount")
public class TurnoverOrderCountController {

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private UserService userService;

    @Log(value = "交易统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_statistics_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "交易统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = TurnoverOrderCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             String coinName) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<TurnoverOrderCountDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        Page<TurnoverOrderCountDTO> pt = turnoverOrderService.selectCountMain(pager, paramMap);
        String[] coins = new String[pt.getRecords().size()];
        int i = 0;
        for (TurnoverOrderCountDTO t : pt.getRecords()) {
            coins[i] = t.getSellCoinId();
            t.setCoinNum("0");
            t.setMaxTurnoverNum("0");
            i++;
        }
        if (coins.length > 0) {
            //买方 最多交易用户，该用户交易量-
            List<TurnoverOrderCountDTO> ptBuyUsers = turnoverOrderService.selectBuyUserCount(coins);
            //卖方 最多交易用户，该用户交易量-
            List<TurnoverOrderCountDTO> ptSellUsers = turnoverOrderService.selectSellUserCount(coins);
            ptSellUsers.addAll(ptBuyUsers);
            for (int j = 0; j < ptSellUsers.size() - 1; j++) {
                for (int t = ptSellUsers.size() - 1; t > j; t--) {
                    String jdate = ptSellUsers.get(j).getCreated();
                    String jsellCoin = ptSellUsers.get(j).getSellCoinId();
                    String tdate = ptSellUsers.get(t).getCreated();
                    String tsellCoin = ptSellUsers.get(t).getSellCoinId();
                    if (jdate.equals(tdate) && jsellCoin.equals(tsellCoin)) {
                        BigDecimal jsell = new BigDecimal(ptSellUsers.get(j).getMaxTurnoverNum());
                        BigDecimal tbuy = new BigDecimal(ptSellUsers.get(t).getMaxTurnoverNum());
                        if (jsell.compareTo(tbuy) != -1) {
                            ptSellUsers.remove(t);
                        }
                    }
                }
            }
            List<TurnoverOrderCountDTO> pt2 = userService.selectUserCount(coins);
            for (TurnoverOrderCountDTO t : pt.getRecords()) {
                for (TurnoverOrderCountDTO ts : pt2) {
                    //持币人数
                    if (ts.getSellCoinId().equals(t.getSellCoinId())) {
                        t.setCoinNum(ts.getCoinNum());
                    }
                }
                //最多交易用户，该用户交易量
                for (TurnoverOrderCountDTO tlu : ptSellUsers) {
                    if (tlu.getSellCoinId().equals(t.getSellCoinId()) && tlu.getCreated().equals(t.getCreated())) {
                        t.setMaxTurnoverNum(tlu.getMaxTurnoverNum());
                        t.setTurnoverUserId(tlu.getTurnoverUserId());
                    }
                }
            }

        }
        return ResultMap.getSuccessfulResult(pt);
    }
}
