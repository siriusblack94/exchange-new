package com.blockeng.mining.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.dto.PlantCoinDividendRecordDTO;
import com.blockeng.mining.entity.PlantCoinDividendRecord;
import com.blockeng.mining.service.PlantCoinDividendRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 邀请奖励
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/coin/dividend")
@Slf4j
@Api(value = "持有平台币分红", description = "持有平台币分红")
public class PlantCoinDividendAccountController {

    @Autowired
    private PlantCoinDividendRecordService plantCoinDividendRecordService;

    /**
     * 持有平台币分红记录
     *
     * @param page
     * @param userDetails
     * @return
     */
    @GetMapping
    @ApiOperation(value = "持有平台币分红", notes = "持有平台币分红", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object plantCoinDividendRecord(
            @ApiIgnore Page<PlantCoinDividendRecord> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(plantCoinDividendRecordService.plantCoinDividendRecord(page, userDetails.getId()));
    }

    /**
     * 持有平台币分红汇总
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/total")
    @ApiOperation(value = "持有平台币分红", notes = "持有平台币分红", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object plantCoinDividendTotal(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(plantCoinDividendRecordService.plantCoinDividendTotal(userDetails.getId()));
    }

}
