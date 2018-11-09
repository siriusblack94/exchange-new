package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.entity.*;
import com.blockeng.mining.service.PrivatePlacementReleaseRecordService;
import com.blockeng.mining.service.PrivatePlacementService;
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
 * @Auther: sirius
 * @Date: 2018/8/15 13:14
 * @Description:私募控制器
 */

@RestController
@RequestMapping("/private/placement")
@Slf4j
@Api(value = "私募明细", description = "私募明细")
public class PrivatePlacementController {
    @Autowired
    private  PrivatePlacementService privatePlacementService;

    @Autowired
    private PrivatePlacementReleaseRecordService privatePlacementReleaseRecordService;

    @GetMapping("/record")
    @ApiOperation(value = "个人私募明细", notes = "个人私募明细", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")

    public Object record(
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<PrivatePlacement> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        PrivatePlacement privatePlacement = privatePlacementService.selectOne(qw);
        if (privatePlacement==null){privatePlacement =new PrivatePlacement();}
        return Response.ok(privatePlacement);
    }

    @GetMapping("/release/record")
    @ApiOperation(value = "个人私募释放明细", notes = "个人私募释放明细", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object releaseRecordList(
            @ApiIgnore Page<PrivatePlacementReleaseRecord> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<PrivatePlacementReleaseRecord> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        qw.gt("release_amount",0);
        return Response.ok( privatePlacementReleaseRecordService.selectPage(page,qw));
    }

//    @GetMapping("/release/do")
////    @ApiOperation(value = "个人私募释放明细", notes = "个人私募释放明细", httpMethod = "GET"
////            , authorizations = {@Authorization(value = "Authorization")})
////    @PreAuthorize("isAuthenticated()")
//    public Object release() {
//        privatePlacementReleaseRecordService.release();
//        return Response.ok();
//    }

}
