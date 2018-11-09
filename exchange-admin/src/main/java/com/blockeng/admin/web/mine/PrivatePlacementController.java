package com.blockeng.admin.web.mine;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.MD5Utils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.PrivatePlacementDTO;
import com.blockeng.admin.entity.PrivatePlacement;
import com.blockeng.admin.entity.PrivatePlacementReleaseRecord;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.idcard.IdCardApi;
import com.blockeng.admin.service.*;
import com.blockeng.dto.SendForm;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.feign.SmsServiceClient;
import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.utils.GsonUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;


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
    PrivatePlacementService privatePlacementService;
    @Autowired
    PrivatePlacementReleaseRecordService privatePlacementReleaseRecordService;

    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountDetailService accountDetailService;

    @Autowired
    ConfigServiceClient configServiceClient;
    @Autowired
    SmsServiceClient smsServiceClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Log(value = "私募一次性释放", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('private_placement_update')")
    @GetMapping("/createOnceRelease")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "私募列表", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap createOnceRelease(String userId, String amount ) {
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(amount)||new BigDecimal(amount).compareTo(BigDecimal.ZERO)<=0){
            return ResultMap.getFailureResult("参数不能为空或参数输入有误");
        }
        log.info(amount+"--amount--");
        EntityWrapper<PrivatePlacement> ew = new EntityWrapper<>();
        ew.eq("user_id", userId);
        PrivatePlacement privatePlacement = privatePlacementService.selectOne(ew);
        if (privatePlacement==null)  return ResultMap.getFailureResult("不存在该私募用户");
        if (privatePlacement.getAmount().compareTo(new BigDecimal(amount))<0)  return ResultMap.getFailureResult("私募金额不足");
        Long plantCoinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue());
        if (plantCoinId.compareTo(0L)==0)  return ResultMap.getFailureResult("未配置平台币");
        BigDecimal releaseCount=new BigDecimal(amount);
        log.info(releaseCount+"--releaseCount--");
        if (privatePlacement.getReleaseAmount().compareTo(privatePlacement.getAmount())==0)  return ResultMap.getFailureResult("私募金额已全部释放");
        if (privatePlacement.getFreezeAmount().compareTo(releaseCount)<=0)//冻结额小于释放量取冻结额
            releaseCount=privatePlacement.getFreezeAmount();
        PrivatePlacementReleaseRecord privatePlacementReleaseRecord = new PrivatePlacementReleaseRecord();
        privatePlacementReleaseRecord.setReleaseAmountRate(BigDecimal.ONE).setUserId(privatePlacement.getUserId())
                .setReleaseAmount(releaseCount);//当次释放记录
        BigDecimal   releaseAmountTotal  =releaseCount.add(privatePlacement.getReleaseAmount());//当前用户总释放量
        privatePlacement.setReleaseAmount(releaseAmountTotal)
                .setFreezeAmount(privatePlacement.getAmount().subtract(releaseAmountTotal)).setLastUpdateTime(new Date());
        if (privatePlacementService.updateById(privatePlacement)&&privatePlacementReleaseRecordService.insert(privatePlacementReleaseRecord)){
            UnlockDTO unlockDTO = new UnlockDTO().
                    setCoinId(plantCoinId).
                    setAmount( privatePlacementReleaseRecord.getReleaseAmount()).
 //                   setBusinessType( BusinessType.ONCE_RELEASE).
//                    setDesc(BusinessType.ONCE_RELEASE.getDesc()).
                    setUserId(privatePlacement.getUserId()).
                    setOrderId(privatePlacementReleaseRecord.getId());
            log.info("----unlockDTO-----"+ GsonUtil.toJson(unlockDTO));
            rabbitTemplate.convertAndSend("pool.unlock", GsonUtil.toJson(unlockDTO));
            return ResultMap.getSuccessfulResult("操作成功");
        }

        return ResultMap.getFailureResult("操作未成功");
    }

    @Log(value = "私募列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('private_placement_query')")
    @GetMapping("/getList")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "私募列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "姓名", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             String userId,
                             String realName,
                             String mobile
    ) {
        EntityWrapper<PrivatePlacementDTO> ew = new EntityWrapper<>();
        Page<PrivatePlacementDTO> pager = new Page<>(current, size);

        if(StringUtils.isNotBlank(userId)){
            ew.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(realName)) {
            ew.like("real_name", realName);
        }
        if (StringUtils.isNotBlank(mobile)) {
            ew.like("mobile", mobile);
        }
        return ResultMap.getSuccessfulResult(privatePlacementService.selectListPage(pager,ew));
    }



    @Log(value = "新增私募", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('private_placement_create')")
    @PostMapping("/create")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增私募", httpMethod = "POST")
    public Object create(@RequestBody PrivatePlacementDTO privatePlacementDTO) {
        try {
            PrivatePlacement privatePlacement;
            if (privatePlacementDTO != null) {
                if (privatePlacementDTO.getMobile() == null || privatePlacementDTO.getRealName() == null || privatePlacementDTO.getIdCard() == null)
                    return ResultMap.getFailureResult("还有参数未填写!");
                try {
                    if (!IdCardApi.verify(privatePlacementDTO.getRealName(), privatePlacementDTO.getIdCard()))
                        return ResultMap.getFailureResult("身份信息验证未通过!");
                } catch (IOException e) {
                    log.error(e.getMessage());
                    return ResultMap.getFailureResult("身份信息验证未通过!");
                }
                User user = userService.selectOne(new EntityWrapper<User>().eq("real_name", privatePlacementDTO.getRealName()).
                        and().eq("id_card", privatePlacementDTO.getIdCard()).or().eq("mobile", privatePlacementDTO.getMobile()));
                if (user != null) {
                    privatePlacement = privatePlacementService.selectOne(new EntityWrapper<PrivatePlacement>().eq("user_id", user.getId()));
                    if (privatePlacement != null) {
                        return ResultMap.getFailureResult("该用户已参与私募资金");
                    }
                } else {
                    user = new User();
                    user.setPassword(new BCryptPasswordEncoder().encode(MD5Utils.getMD5(makeRandomPassword(12))));
                    SendForm sendForm = new SendForm();
                    sendForm.setCountryCode(privatePlacementDTO.getCountryCode());
                    sendForm.setMobile(privatePlacementDTO.getMobile());
                    sendForm.setTemplateCode("PRIVATE_PLACEMENT_INIT_PASSWORD");
                    log.info("私募用户发送初始化密码：sendForm----" + sendForm);
                    smsServiceClient.sendTo(sendForm);
                }
                user.setMobile(privatePlacementDTO.getMobile());
                user.setIdCard(privatePlacementDTO.getIdCard());
                user.setRealName(privatePlacementDTO.getRealName());

                if (userService.insertOrUpdate(user)) {
                    privatePlacement = new PrivatePlacement();
                    privatePlacement.setAmount(privatePlacementDTO.getAmount())
                            .setFreezeAmount(privatePlacementDTO.getAmount())
                            .setUserId(user.getId());
                    log.info("----privatePlacement---" + privatePlacement);
                    if (privatePlacementService.insert(privatePlacement))
                        return ResultMap.getSuccessfulResult("操作成功!");
                }
                return ResultMap.getFailureResult("操作失败!");

            }
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultMap.getFailureResult("操作失败!");
        }
        return ResultMap.getFailureResult("还有参数未填写或权限不足");
    }

    public static String makeRandomPassword(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&".toCharArray();
        //System.out.println("字符数组长度:" + charr.length);	//可以看到调用此方法多少次
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 23; i++) {
//            String initPass = makeRandomPassword(16);
//            String encodePass  = new BCryptPasswordEncoder().encode(MD5Utils.getMD5(initPass));
//            System.out.println(initPass+"----"+encodePass);
//        }
//
//    }

}
