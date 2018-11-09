package com.blockeng.admin.web.funds.points;




import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.entity.Points;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountService;
import com.blockeng.admin.service.PointsService;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;


@RestController
@RequestMapping("/points")
@Slf4j
@Api(value = "/points", description = "积分互换", tags = "积分互换")
public class PointsController {

    @Autowired
    AccountService accountService;

    @Autowired
    PointsService pointsService;


    /**
     * 积分兑换记录
     *
     * @return
     */
    @Log(value = "积分兑换记录", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('points_query')")
    @PostMapping(value = "/record")
    @ApiOperation(value = "积分兑换记录", notes = "积分兑换记录", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    public Response synchronousRecord(
            @ApiIgnore Page<Points> page,
            @RequestBody Points points) {

        EntityWrapper<Points> qw = new EntityWrapper<>();

        if (StringUtils.isNotBlank(points.getUserId())) {
            qw.eq("d.user_id", points.getUserId());
        }

        if (StringUtils.isNotBlank(points.getCoinName())) {
            qw.eq("d.coin_name",points.getCoinName());
        }
        if (StringUtils.isNotBlank(points.getCoinName())) {
            qw.eq("d.plus_or_minus",points.getPlusOrMinus());
        }

        if (StringUtils.isNotBlank(points.getRemark())) {
            qw.eq("d.remark",points.getRemark());
        }
        if (StringUtils.isNotBlank(points.getEmail())) {
            qw.eq("u.email",points.getEmail());
        }
        if (StringUtils.isNotBlank(points.getMobile())) {
            qw.eq("u.mobile",points.getMobile());
        }
        if (StringUtils.isNotBlank(points.getUsername())) {
            qw.eq("u.username",points.getUsername());
        }

        return Response.ok(pointsService.selectListPage(page,qw));
    }


    /**
     * 解冻资金
     *
     * @return
     */
    @Log(value = "解冻资金", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('points_update')")
    @PostMapping(value = "/account/unlock")
    @ApiOperation(value = "解冻资金", notes = "解冻资金", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    public Response synchronousRecord(
            String id) {
        Points points = pointsService.selectById(id);
        if (points!=null&&"1".equals(points.getPlusOrMinus())&&("2".equals(points.getRemark())||"3".equals(points.getRemark()))) {
            if (accountService.unlockAmount
                    (Long.valueOf(points.getUserId()),points.getCoinId(),new BigDecimal(points.getCount()),BusinessType.POINTS_UNLOCK,points.getId()))
            return Response.ok(points);
        }
        return Response.err(100,"账户资金异常，无法解冻");
    }
}
