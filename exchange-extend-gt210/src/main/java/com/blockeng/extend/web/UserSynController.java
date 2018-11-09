package com.blockeng.extend.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.extend.common.ResultMap;
import com.blockeng.extend.dto.UserSynDTO;
import com.blockeng.extend.entity.*;
import com.blockeng.extend.service.ConfigService;
import com.blockeng.extend.service.UserSynExceptionService;
import com.blockeng.extend.service.UserSynService;
import com.blockeng.extend.util.DESUtil;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.security.UserDetails;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/extend")
public class UserSynController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserSynService userSynService;

    @Autowired
    private DESUtil desUtil;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserSynExceptionService userSynExceptionService;


    /**
     * 完成实名认证时同步用户信息
     * @param payload
     */
    @RequestMapping("/register")
    @RabbitListener(queues = "user.info.syn")
    public void register(String payload) {

        log.info("message:{}",payload);
        User user = new Gson().fromJson(payload, User.class);
        if (user==null){
            return;
        }
        //注册同步
        UserSyn  userSyn = new UserSyn().getSynUser(user);

        log.info("userSyn:{}",userSyn);

        UserSynDTO userSynDTO = new UserSynDTO().encryptUser(userSyn,desUtil);

        //传参
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("userId",userSynDTO.getUserId());
        param.add("parentId",userSynDTO.getParentId());
        param.add("account",userSynDTO.getAccount());
        param.add("password",userSynDTO.getPassword());
        param.add("mobile",userSynDTO.getMobile());
        param.add("mail",userSynDTO.getMail());
        param.add("userName",userSynDTO.getUserName());
        param.add("realName",userSynDTO.getRealName());
        param.add("sign",userSynDTO.getSign());

        Config configRegister = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_USER_REGISTER);
        log.info("configRegister:***:"+configRegister);

        log.info("param:***"+param);

        ReturnInfo returnInfo = null;
        try {
            returnInfo = restTemplate.postForObject(configRegister.getValue(), param, ReturnInfo.class);
            if (returnInfo==null){throw new NullPointerException("场外交易系统返回null");}

            log.info("returnInfo:"+returnInfo);

        } catch (Exception e) {

            log.info("异常:"+e);

            UserSynExecption synExecption = userSynExceptionService.selectById(userSyn.getId());

            log.info("synExecption:"+synExecption);

            if (synExecption!=null){
                return;
            }
            synExecption = new UserSynExecption().setId(userSyn.getId()).setUserInfo(payload).setType(Constant.EXTEND_USER_REGISTER);
            userSynExceptionService.insert(synExecption);
        }

        if (returnInfo!=null){
            userSyn.setStatus(returnInfo.getStatus())
                   .setToken(returnInfo.getToken())
                   .setMessage(returnInfo.getMessage());
            userSynService.insert(userSyn);
            userSynExceptionService.deleteById(userSyn.getId());
        }
    }

    /**
     * 更新用户信息
     * @param payload
     */
    @GetMapping("/update")
    @RabbitListener(queues = "user.info.update.syn")
    public void update(String payload){




        log.info("message:{}",payload);

        User user = new Gson().fromJson(payload, User.class);
        if (user==null){
            return;
        }
        UserSyn userSyn = userSynService.selectById(user.getId());
        if(userSyn!=null){
            //退出场外系统
            logout(userSyn);
        }
        userSyn = new UserSyn().getSynUser(user);

        UserSynDTO userSynDTO = new UserSynDTO().encryptUser(userSyn,desUtil);

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("userId",userSynDTO.getUserId());
        param.add("password",userSynDTO.getPassword());
        param.add("mobile",userSynDTO.getMobile());
        param.add("mail",userSynDTO.getMail());
        param.add("userName",userSynDTO.getUserName());
        param.add("realName",userSynDTO.getRealName());
        param.add("sign",userSynDTO.getSign());

        log.info("param:{}",param);

        Config configReturnURL = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_RETURN_URL);
        log.info("configReturnURL:"+configReturnURL);

        HttpHeaders headers = getHeaders(userSyn, configReturnURL.getValue());


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
        Config configUpdate = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_USER_UPDATE);
        log.info("configUpdate:"+configUpdate);


        ReturnInfo returnInfo = null;
        try {
            returnInfo = restTemplate.postForObject(configUpdate.getValue(), request, ReturnInfo.class);
            if (returnInfo==null){throw new NullPointerException("场外交易系统返回null");}

            log.info("returnInfo***:"+returnInfo);
        } catch (Exception e) {

            log.info("异常:"+e);

            UserSynExecption synExecption = userSynExceptionService.selectById(userSyn.getId());
            if (synExecption!=null){
                return;
            }
            synExecption = new UserSynExecption().setId(userSyn.getId()).setUserInfo(payload).setType(Constant.EXTEND_USER_UPDATE);
            userSynExceptionService.insert(synExecption);
        }

        if (returnInfo!=null){
            userSyn.setStatus(returnInfo.getStatus())
                    .setToken(returnInfo.getToken())
                    .setMessage(returnInfo.getMessage());
            userSynService.updateById(userSyn);
            userSynExceptionService.deleteById(userSyn.getId());
        }
    }

    /**
     * 外部系统登录接口
     * @param
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/login")
    public Object login(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails){

        if (!Optional.ofNullable(userDetails).isPresent()) {
            return ResultMap.getFailureResult("未登录");
        }
        UserSyn user = userSynService.selectById(userDetails.getId());

        log.info("user:"+user);

        if(user==null) {
            return ResultMap.getFailureResult("场外系统无此用户信息");
        }

            Config configReturnURL = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_RETURN_URL);

            HttpHeaders headers = getHeaders(user, configReturnURL.getValue());

            MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

            //将参数和header组成一个请求
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
            Config configLogin = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_USER_LOGIN);
            ResponseEntity<String> exchange = restTemplate.exchange(configLogin.getValue(), HttpMethod.GET, request, String.class);

        return exchange;
    }


    /**
     * 外部系统退出接口
     * @param
     * @return
     */
    @PostMapping("/logout")
    public void logout(UserSyn user){

        log.info("user:"+user);
      //退出CONFIG
        Config configReturnUrl = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_RETURN_URL);
        HttpHeaders headers = getHeaders(user, configReturnUrl.getValue());

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        //将参数和header组成一个请求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);

        Config configLogout = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_USER_LOGOUT);
        ResponseEntity<ReturnInfo>  exchange;
        try {
            exchange = restTemplate.exchange(configLogout.getValue(), HttpMethod.GET, request, ReturnInfo.class);

            log.info("exchange:{}"+exchange);
        } catch (RestClientException e) {
            log.info("e:{}",e);
        }
    }



    private HttpHeaders getHeaders(UserSyn user, String... returnurl){
        HttpHeaders headers = new HttpHeaders();
        headers.add("gttoken", user.getToken());
        headers.add("returnurl",returnurl[0]);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return headers;
    }

    /**
     * 2分钟定时轮询数据库
     */
    @Scheduled(fixedDelay = 1000*60*2)
    public void userInfoSyn() {

        List<UserSynExecption> users = userSynExceptionService.selectList(new QueryWrapper<>());

        if (users!=null && users.size()>0) {

            users.forEach(user -> {
                if (Constant.EXTEND_USER_REGISTER.equals(user.getType())) {
                    log.info("userInfo:"+user);
                    register(user.getUserInfo());
                }
                if (Constant.EXTEND_USER_UPDATE.equals(user.getType())) {
                    log.info("userInfo:"+user);
                    update(user.getUserInfo());
                }
        }); }
    }
}
