package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.TradeTopVolumeDTO;
import com.blockeng.admin.dto.UserBlanceTopDTO;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.TurnoverOrderService;
import com.blockeng.admin.service.UserService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 交易统计 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/trade/count")
@Api(value = "交易统计", description = "交易统计控制器")
public class TradeCountController {

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private UserService userService;

    @Log(value = "交易量排行查询", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_statistics_query')")
    @GetMapping("/top/volume")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "交易量排行", httpMethod = "GET")
    public Object selectVolumePage(@ApiIgnore Page<TradeTopVolumeDTO> page,
                                   String startTime, String endTime) {
        EntityWrapper<TradeTopVolumeDTO> ew = new EntityWrapper<>();

        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {

            ew.between("a.created", startTime, endTime + " 23:59:59");

        }


//        if (!Strings.isNullOrEmpty(startTime)) {
//             ew.ge("a.created", startTime);
//        }
//        if (!Strings.isNullOrEmpty(endTime)) {
//            ew.le("a.created", endTime + "23:59:59");
//        }
        //交易成功
        ew.eq("a.status", 1);



        return ResultMap.getSuccessfulResult(turnoverOrderService.selectTradeTopVolumePage(page, ew));
    }

    @Log(value = "持仓排行查询", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('position_statistics_query')")
    @GetMapping("/top/balance")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段(降序)", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "持仓排行查询", httpMethod = "GET")
    public Object selectBlancePage(@ApiIgnore Page<UserBlanceTopDTO> page, String userId,
                                   String orderBy) {
        EntityWrapper<UserBlanceTopDTO> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("user_id", userId);
        }
        if (!Strings.isNullOrEmpty(orderBy)) {
            ew.orderBy(orderBy, false);
        } else {
            ew.orderBy("btc_amount", false);
        }
        return ResultMap.getSuccessfulResult(userService.selectBalanceTopPage(page, ew));
    }
}
