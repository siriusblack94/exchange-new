package com.blockeng.mining.web;

import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.service.DividendRecordService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 邀请奖励
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/dividend/record")
@Slf4j
@Api(value = "邀请奖励", description = "邀请奖励")
public class DividendRecordController {

    @Autowired
    private DividendRecordService dividendRecordService;


    /**
     * 根据指定的日期查询详情
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/week/{unLockDate}")
    @ApiOperation(value = "根据指定的日期查询详情", notes = "根据指定的日期查询详情", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendAccountPriWeek(
            @PathVariable("unLockDate") String unLockDate,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(dividendRecordService.dividendAccountPriWeek(unLockDate, userDetails.getId()));
    }


    /**
     * 查询本周可以获得挖矿信息
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/unlock/week")
    @ApiOperation(value = "查询本周可以解冻金额", notes = "查询本周可以解冻金额", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendAccountThisWeek(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(dividendRecordService.dividendAccountThisWeek(userDetails.getId()));
    }


//    @GetMapping("/mining/do")
//    public String dividendAccountTotal() {
//        dividendRecordService.inviteRelation();
//        return "ok";
//    }

}
