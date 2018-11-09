package com.blockeng.web;

import com.blockeng.dto.CoinTransferForm;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.entity.User;
import com.blockeng.feign.CoinTransferServiceClient;
import com.blockeng.feign.RandCodeServiceClient;
import com.blockeng.framework.enums.SmsTemplate;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @Author: jakiro
 * @Date: 2018-10-26 10:46
 * @Description: 站内转帐控制类
 */

@RestController
@RequestMapping("/transfer")
@Slf4j
@Api(value = "站内转帐", tags = "站内转帐")
public class UserTransferController {


    @Autowired
    private CoinTransferServiceClient coinTransferServiceClient;

    @Autowired
    private RandCodeServiceClient randCodeServiceClient;

    @Autowired
    private UserService userService;

    /**
     * 用户转帐
     *
     * @param coinTransferForm
     * @return 操作结果
     */
    @PostMapping("/doTransfer")
    @ApiOperation(value = "用户转帐", httpMethod = "POST")
    public Object transfer(@ApiParam(value = "转帐", required = true) @RequestBody @Valid CoinTransferForm coinTransferForm, @ApiIgnore @AuthenticationPrincipal UserDetails userDetails)  {

        //先第一步,侧面校验一下 打款人的用户ID 和 UserDetail中的Id 应该一致
        String moneyMakerUserId=String.valueOf(coinTransferForm.getMoneyMakerUserId());
        String userId=String.valueOf(userDetails.getId());
        if(!moneyMakerUserId.equals(userId)){//问题很大
             log.error("打款人信息篡改,打款人ID:"+moneyMakerUserId+",收款人ID:"+coinTransferForm.getPayeeUserId());
             return Response.err(90001,"打款人信息篡改");
        }

        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(coinTransferForm.getCountryCode())
                .setTemplateCode(SmsTemplate.COIN_TRANSFER.getCode())
                .setPhone(coinTransferForm.getMobile())
                .setEmail(coinTransferForm.getEmail())
                .setCode(coinTransferForm.getValidateCode());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            // 验证码错误
            throw new GlobalDefaultException(20007);
        }
        User user = userService.selectById(userDetails.getId());
        if (!new BCryptPasswordEncoder().matches(coinTransferForm.getPayPassWord(), user.getPaypassword())) {
            throw new GlobalDefaultException(2012);
        }
        boolean result=coinTransferServiceClient.doTransfer(coinTransferForm);
        if(result){
            return Response.ok();
        }
        return Response.err(90002,"打款失败");
    }


    @PostMapping("/doTransferTest")
    @ApiOperation(value = "用户转帐", httpMethod = "POST")
    public Object transferTest(@ApiParam(value = "转帐", required = true) @RequestBody @Valid CoinTransferForm coinTransferForm)  {

        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(coinTransferForm.getCountryCode())
                .setTemplateCode(SmsTemplate.COIN_TRANSFER.getCode())
                .setPhone(coinTransferForm.getMobile())
                .setEmail(coinTransferForm.getEmail())
                .setCode(coinTransferForm.getValidateCode());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            // 验证码错误
            throw new GlobalDefaultException(20007);
        }
        User user = userService.selectById(coinTransferForm.getMoneyMakerUserId());
        if (!new BCryptPasswordEncoder().matches(coinTransferForm.getPayPassWord(), user.getPaypassword())) {
            throw new GlobalDefaultException(2012);
        }
        boolean result=coinTransferServiceClient.doTransfer(coinTransferForm);
        if(result){
            return Response.ok();
        }
        return Response.err(1,"打款失败");
    }
}
