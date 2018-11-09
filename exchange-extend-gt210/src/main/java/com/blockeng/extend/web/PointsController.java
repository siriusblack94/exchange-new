package com.blockeng.extend.web;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.extend.annotation.RequestIpLimit;
import com.blockeng.extend.common.Constants;
import com.blockeng.extend.dto.PointsDTO;
import com.blockeng.extend.entity.Account;
import com.blockeng.extend.entity.Points;
import com.blockeng.extend.entity.User;
import com.blockeng.extend.entity.UserSyn;
import com.blockeng.extend.exception.GlobalException;
import com.blockeng.extend.function.GroupInterface;
import com.blockeng.extend.function.GroupInterface1;
import com.blockeng.extend.service.*;
import com.blockeng.extend.util.DESUtil;
import com.blockeng.extend.util.MD5Utils;
import com.blockeng.extend.util.ObjectToMap;
import com.blockeng.extend.util.RestClient;
import com.blockeng.feign.RandCodeServiceClient;
import com.blockeng.framework.enums.SmsTemplate;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/points")
@Slf4j
@Api(value = "/points", description = "积分互换", tags = "积分互换")
public class PointsController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PointsService pointsService;

    @Autowired
    DESUtil desUtil;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserSynService userSynService;

    @Autowired
    private RandCodeServiceClient randCodeServiceClient;


    /**
     * 积分兑换记录
     *
     * @return
     */
    @RequestMapping(value = "/synchronous/record", method = RequestMethod.GET )
    @ApiOperation(value = "积分兑换记录", notes = "积分兑换记录", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Response synchronousRecord(
            @ApiIgnore Page<Points> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,Points points) {

        QueryWrapper<Points> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        if (StringUtils.isNotBlank(points.getCoinName())) {
            qw.eq("coin_name",points.getCoinName());
        }
        if (StringUtils.isNotBlank(points.getCoinName())) {
            qw.eq("plus_or_minus",points.getPlusOrMinus());
        }
        return Response.ok(  pointsService.selectPage(page,qw));
    }

//    /**
//     * 积分兑换重传
//     *
//     * @return
//     */
//    @RequestGeetestLimit
//    @PostMapping(value = "/synchronousRetry")
//    @ApiOperation(value = "积分兑换重试", notes = "积分兑换重试", httpMethod = "POST"
//            , authorizations = {@Authorization(value = "Authorization")})
//    @PreAuthorize("isAuthenticated()")
//    public Response synchronousRetry(
//            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,@RequestParam("id") String id) {
//        Points points = pointsService.selectById(id);
//        PointsDTO pointsDTO = new PointsDTO();
//        pointsDTO.setCount(desUtil.encrypt(points.getCount()))
//                .setPlusOrMinus(desUtil.encrypt(points.getPlusOrMinus()))
//                .setUserId(desUtil.encrypt(points.getUserId()))
//                .setType(desUtil.encrypt(points.getType()))
//                .setSign(points.getSign());
//        Account account = accountService.selectByUserAndCoinName(String.valueOf(userDetails.getId()), points.getCoinName());
//        if (account==null) return  Response.err(1002,"不存在该账户");
//
//        if ("0".equals(points.getRemark())&&"1".equals(points.getPlusOrMinus())){
//            if (!lockAmount(account,points)) return Response.err(1003, "冻结资金失败,余额不足");
//        }
//        if (("2".equals(points.getRemark())||"3".equals(points.getRemark()))&&"1".equals(points.getPlusOrMinus())) {
//            return getResponse(points, account);
//        }
//        return Response.err(1,"操作失败" );
//    }

    private boolean lockAmount(Account account,Points points) {
        String remark="2";
        String message="余额冻结成功";
        if (accountService.lockAmount(account.getId(),account.getUserId(),account.getCoinId(),new BigDecimal(points.getCount()),points.getId()))
        {
            points.setMessage(message);
            points.setRemark(remark);
            pointsService.updateById(points);
            return true;
        }
        remark="1";
        message="余额不足";
        points.setMessage(message);
        points.setRemark(remark);
        pointsService.updateById(points);
        return false;
    }

    /**
     * 积分兑换
     *
     * @return
     */
    @PostMapping(value = "/synchronous")
    @ApiOperation(value = "积分兑换", notes = "积分兑换", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Response synchronous(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails,@Validated({GroupInterface1.class}) @RequestBody Points points)  {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalException("用户未登录");
        }
        log.info("userDetails---"+userDetails);
        User user = userService.selectById(userDetails.getId());
        if (!"2".equals(user.getAuthStatus()))
        return Response.err(2000,"请先完成高级实名认证");

        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(userDetails.getCountryCode())
                .setTemplateCode(SmsTemplate.EXTEND_POINT_REDEMPTION.getCode())
                .setEmail(points.getEmail())
                .setCode(points.getValidateCode());
        if(StringUtils.isBlank(points.getEmail())) randCodeVerifyDTO.setPhone(userDetails.getMobile());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalException("验证码错误");
        }

        if (StringUtils.isBlank(user.getPaypassword())) {
            log.error("您还未设置交易密码");
            throw new GlobalException("您还未设置交易密码");
        }
        if (!new BCryptPasswordEncoder().matches(points.getPayPassword(), user.getPaypassword())) {
            log.error("资金交易密码错误");
            throw new GlobalException("资金交易密码错误");
        }
        Account account = accountService.selectByUserAndCoinName(String.valueOf(userDetails.getId()), points.getCoinName());
        if (account==null) return  Response.err(1002,"不存在该账户");


        String sign =MD5Utils.getMD5("password=" +(userDetails.getPassword()==null? "":userDetails.getPassword())
                +"&mobile=" +(userDetails.getMobile()==null? "":userDetails.getMobile())
                +"&mail=" +(userDetails.getEmail()==null? "":userDetails.getEmail())
                +"&username=" +(userDetails.getUsername()==null? "":userDetails.getUsername())
                +"&realname="+(userDetails.getRealName()==null? "":userDetails.getRealName()));

        points.setCoinId(account.getCoinId())
            .setType(Constants.getNameKeyMap().get(points.getCoinName()))
            .setUserId(String.valueOf(userDetails.getId()));
        points.setSign(sign);
        points.setCoinName(points.getCoinName());
        pointsService.insert(points);
        if (!lockAmount(account,points)) return Response.err(1003, "冻结资金失败,余额不足");
        return getResponse(points, account);


    }

    private Response getResponse( Points points, Account account) {
        String url = configService.queryByTypeAndCode("SYSTEM", "EXTEND_SYNCHRONOUS_URL").getValue();
        log.info("EXTEND_SYNCHRONOUS_URL---"+url);
        if(StringUtils.isBlank(url)) return Response.err(1001,"未配置积分兑换URL");
        UserSyn userSyn = userSynService.selectById(account.getUserId());
        if (userSyn==null||userSyn.getToken()==null) return  Response.err(1002,"该账户信息未同步");
        String remark;
        String message;
        Integer status;
        PointsDTO pointsDTO = new PointsDTO();
        pointsDTO.setCount(desUtil.encrypt(points.getCount()))
                .setPlusOrMinus(desUtil.encrypt(points.getPlusOrMinus()))
                .setUserId(desUtil.encrypt(points.getUserId()))
                .setType(desUtil.encrypt(points.getType()))
                .setSign(points.getSign())
                .setOrderId(String.valueOf(points.getId()));
        Map<String, String> map = new HashMap<>();
        map.put("gttoken",userSyn.getToken());
        map.put("returnurl",points.getReturnurl());
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        try {
            RestClient.setRestTemplate(restTemplate);
            linkedMultiValueMap.setAll(ObjectToMap.objectToMap(pointsDTO));
            log.info("map--"+map);
            log.info("linkedMultiValueMap--"+linkedMultiValueMap);
            String response = RestClient.postForm(url,map,linkedMultiValueMap);
            log.info("response---"+response);
            Map mapTypes = JSON.parseObject(response);
            status = (Integer)mapTypes.get("status");
            message= (String) mapTypes.get("message");
        } catch (Exception e) {
            remark="3";
            message="积分兑换系统超时,正在重试";
            points.setRemark(remark);
            points.setMessage(message);
            pointsService.updateById(points);
            log.error("积分同步系统异常",e);
//            asyncCommit();
            return Response.err(1004,"积分同步系统异常:返回值"+e.getMessage());
        }
        if (status==1&&accountService.subtractAmount(account.getId(),account.getUserId(),account.getCoinId(),new BigDecimal(points.getCount()),points.getId())) {
            remark="5";
            points.setRemark(remark);
            points.setMessage(message);
            points.setStatus(status);
            pointsService.updateById(points);
            return Response.ok(points);
        }
        remark="4";
        message="扣减资金失败";
        points.setRemark(remark);
        points.setMessage(message);
        pointsService.updateById(points);
        return Response.err(1005,"扣减资金失败");

    }


    /**
     * 积分赎回
     *
     * @return
     */
    @RequestIpLimit
    @RequestMapping(value = "/anti-synchronous",method = RequestMethod.POST)
    @ApiOperation(value = "积分赎回", notes = "积分赎回", httpMethod = "POST")
    public String antiSynchronous( @Validated({GroupInterface.class})PointsDTO pointsDTO) {
        Map<String, String> map = new HashMap<>();
        Points points = new Points();
        Integer status = 0;
        String message = "等待赎回";
        String remark = "0";
        try {
            points.setUserId(desUtil.decrypt(pointsDTO.getUserId()))
                    .setType(desUtil.decrypt(pointsDTO.getType()))
                    .setCount(desUtil.decrypt(pointsDTO.getCount()))
                    .setPlusOrMinus("0")
                    .setSign(pointsDTO.getSign());
            points.setCoinName(Constants.getNumberKeyMap().get(points.getType()));
            Account account = accountService.selectByUserAndCoinName(points.getUserId(),points.getCoinName());
            if (account != null) {
                points.setCoinId(account.getCoinId())
                        .setRemark("0")
                        .setMessage(message)
                        .setRemark(remark);
                pointsService.insert(points);
                if (accountService.addAmount(account.getId(), account.getUserId(), account.getCoinId(),
                        new BigDecimal(points.getCount()),points.getId())) {
                    status = 1;
                    message = "操作成功";
                    remark="2";
                    points.setStatus(status).setMessage(message).setRemark(remark);
                    pointsService.updateById(points);
                } else {
                    message = "资金增加异常";
                    remark="4";
                    points.setMessage(message).setRemark(remark);
                    pointsService.updateById(points);
                }
            } else {
                message = "不存在该账户";
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error(e.getMessage(),e);
        }
        map.put("status", String.valueOf(status));
        map.put("message", message);
        return JSON.toJSONString(map);
    }
}
