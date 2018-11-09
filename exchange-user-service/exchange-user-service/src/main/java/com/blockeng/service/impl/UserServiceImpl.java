package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.api.IdCardApi;
import com.blockeng.dto.ConfigDTO;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.entity.AuthUser;
import com.blockeng.entity.User;
import com.blockeng.entity.UserAuthAuditRecord;
import com.blockeng.entity.UserAuthInfo;
import com.blockeng.enums.AuthStatus;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.feign.RandCodeServiceClient;
import com.blockeng.feign.RewardServiceClient;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.SmsTemplate;
import com.blockeng.framework.enums.UserType;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.JwtToken;
import com.blockeng.framework.security.TokenProvider;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.framework.utils.ShareCodeUtil;
import com.blockeng.mapper.UserMapper;
import com.blockeng.service.UserAuthAuditRecordService;
import com.blockeng.service.UserAuthInfoService;
import com.blockeng.service.UserDetailsService;
import com.blockeng.service.UserService;
import com.blockeng.web.vo.*;
import com.blockeng.web.vo.mappers.RegisterMapper;
import com.blockeng.web.vo.mappers.UserAuthInfoMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RewardServiceClient rewardServiceClient;

    @Autowired
    private RandCodeServiceClient randCodeServiceClient;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConfigServiceClient configServiceClient;

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return
     */
    @Override
    public User selectByUsername(String username) {
//        log.info("username------"+username+"-----");
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("mobile", username);
        ew.or().eq("email", username);
        return super.selectOne(ew);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param mobile 手机号
     * @return
     */
    @Override
    public User selectByMobile(String mobile) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("mobile", mobile);
        ew.or().eq("email", mobile);
        return super.selectOne(ew);
    }

    /**
     * 用户注册
     *
     * @param form 注册信息
     * @return
     */
    @Override
    @Transactional
    public Response register(RegisterForm form) {
        User user = RegisterMapper.INSTANCE.map(form);
        String checkAccount = "";

        if (StringUtils.isNotEmpty(form.getEmail())) {
            checkAccount = form.getEmail();
        }
        if (StringUtils.isNotEmpty(form.getMobile())) {
            checkAccount = form.getMobile();
        }
        if (Optional.ofNullable(selectByMobile(checkAccount)).isPresent()) {
            throw new GlobalDefaultException(40002);
        }
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(form.getCountryCode())
                .setTemplateCode(SmsTemplate.REGISTER_VERIFY.getCode())
                .setPhone(form.getMobile())
                .setEmail(form.getEmail())
                .setCode(form.getValidateCode());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            // if (false) {
            // 验证码错误
            throw new GlobalDefaultException(20007);
        } else {
            String inviteId = "";
            String inviteRelation = "";
            String parent;
            if (StringUtils.isNotEmpty(form.getInvitionCode())) {
                QueryWrapper<User> e = new QueryWrapper<>();
                e.eq("invite_code", form.getInvitionCode());
                User inviter = super.selectOne(e);
                if (!Optional.ofNullable(inviter).isPresent()) {
                    //错误的邀请码
                    throw new GlobalDefaultException(20005);
                }
                inviteId = inviter.getId() + "";
                parent = inviter.getInviteRelation();
                if (StringUtils.isBlank(parent)) {
                    inviteRelation = inviteId + ",";
                } else {
                    String[] strings = parent.split(",");
                    inviteRelation = inviteId + "," + strings[0] + ",";
                    if (strings.length > 1) {
                        inviteRelation = inviteRelation + strings[1] + "";
                    }
                }
            }
            String password = form.getPassword();
            user.setDirectInviteid(inviteId);
            user.setInviteRelation(inviteRelation);
            user.setPassword(new BCryptPasswordEncoder().encode(password)); // 密码
            user.setLogins(0);                              // 登录次数
            user.setStatus(BaseStatus.EFFECTIVE.getCode()); // 状态
            user.setPaypassSetting(0);                      // 支付密码
            user.setType(UserType.CUSTOMER.getCode());      // 类型为：普通用户
            user.setLevel(-1);                              // 普通用户层级为-1
            user.setPlatid(form.getPlatid());
            int count = baseMapper.insert(user);
            if (count > 0) {
                Long userId = user.getId();
                String inviteCode = ShareCodeUtil.idToCode(userId);
                user.setInviteCode(inviteCode);
                baseMapper.updateById(user);
                // 初始化资金账户 和 外汇账户
                rabbitTemplate.convertAndSend("sync.account", user.getId());
                return Response.ok();
            }
            throw new GlobalDefaultException(2007);
        }
    }

    /**
     * 实名认证
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    @Transactional
    public Response updateAuthAccount(UserDetails userDetails, UserAuthInfoForm form) {
        User sameUser = baseMapper.selectByIdCard(form.getIdCard());
        if (Optional.ofNullable(sameUser).isPresent()) {
            throw new GlobalDefaultException(2020);
        }
        try {
            if (!IdCardApi.verify(form.getRealName(), form.getIdCard())) {
                throw new GlobalDefaultException(2040);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new GlobalDefaultException(2040);
        }
        User user2 = UserAuthInfoMapper.INSTANCE.map(form);
        user2.setId(userDetails.getId());
        int count = baseMapper.updateAuthName(user2);
        if (count < 1) {
            throw new GlobalDefaultException(20004);
        }
        // 注册奖励
        rewardServiceClient.registerReward(userDetails.getId());
        User user = baseMapper.selectById(userDetails.getId());
        // 邀请奖励
        if (!StringUtils.isEmpty(user.getDirectInviteid())) {
            rewardServiceClient.inviteReward(Long.parseLong(user.getDirectInviteid()));
        }
        return Response.ok();
    }

    /**
     * 修改支付密码
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    @Transactional
    public Response updatePayPassword(UserDetails userDetails, ChangePasswordForm form) {
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(userDetails.getCountryCode())
                .setTemplateCode(SmsTemplate.CHANGE_PAY_PWD_VERIFY.getCode())
                .setEmail(form.getEmail())
                .setCode(form.getValidateCode());
        if(StringUtils.isBlank(form.getEmail())) randCodeVerifyDTO.setPhone(userDetails.getMobile());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        //验证码错误
        if (!flag) {
            throw new GlobalDefaultException(20007);
        }
        User user = baseMapper.selectById(userDetails.getId());
        String oldPayPassword = user.getPaypassword();
        if (!new BCryptPasswordEncoder().matches(form.getOldpassword(), oldPayPassword)) {
            log.error("原交易密码错误");
            throw new GlobalDefaultException(2042);
        }
        int count = baseMapper.updatePayPassword(user.getId(), new BCryptPasswordEncoder().encode(form.getNewpassword()));
        if (count < 1) {
            throw new GlobalDefaultException(20004);
        }
        return Response.ok();
    }

    /**
     * 修改密码
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    public Response updatePassword(UserDetails userDetails, ChangePasswordForm form) {

        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(userDetails.getCountryCode())
                .setTemplateCode(SmsTemplate.CHANGE_LOGIN_PWD_VERIFY.getCode())
                .setEmail(form.getEmail())
                .setCode(form.getValidateCode());
        if(StringUtils.isBlank(form.getEmail())) randCodeVerifyDTO.setPhone(userDetails.getMobile());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(20007);
        }
        User user = baseMapper.selectById(userDetails.getId());
        String oldPassword = user.getPassword();
        if (!new BCryptPasswordEncoder().matches(form.getOldpassword(), oldPassword)) {
            log.error("原登录密码错误");
            throw new GlobalDefaultException(2041);
        }
        int count = baseMapper.updatePassword(user.getId(), new BCryptPasswordEncoder().encode(form.getNewpassword()));
        if (count < 1) {
            throw new GlobalDefaultException(20004);
        }
        // 刷新token
        refreshToken();
        return Response.ok();
    }

    /**
     * 修改手机号
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    public Response updatePhone(UserDetails userDetails, ChangePhoneForm form) {

        RandCodeVerifyDTO randCodeVerifyDTO;
        boolean flag;
        if(!form.getOldValidateCode().equals("0")){
            //验证老的验证码
            randCodeVerifyDTO = new RandCodeVerifyDTO()
                    .setCountryCode(userDetails.getCountryCode())
                    .setTemplateCode(SmsTemplate.VERIFY_OLD_PHONE.getCode())
                    .setPhone(userDetails.getMobile())
                    .setCode(form.getOldValidateCode());
            flag = randCodeServiceClient.verify(randCodeVerifyDTO);
            if (!flag) {
                //验证码错误
                throw new GlobalDefaultException(50063);
            }
        }

        randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(form.getCountryCode())
                .setTemplateCode(SmsTemplate.CHANGE_PHONE_VERIFY.getCode())
                .setPhone(form.getNewMobilePhone())
                .setCode(form.getValidateCode());

        flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(50064);
        }
        int i = baseMapper.updatePhone(form.getNewMobilePhone(), form.getCountryCode(), userDetails.getId());
        //刷新token
        refreshToken();
        //同步用户信息
        if (i==1){
            ConfigDTO config = configServiceClient.getConfig(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_SWITCH);
            if (config!=null && "1".equals(config.getValue()) ) {
                User user = userService.selectById(userDetails.getId());
                userInfoSyn(user);
            }
        }
        return Response.ok();
    }


    /**
     * 修改邮箱
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    public Response updateEmail(UserDetails userDetails, ChangeEmailForm form) {
        //验证老的验证码
        var randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setTemplateCode(SmsTemplate.VERIFY_OLD_PHONE.getCode())
                .setPhone(userDetails.getEmail())
                .setCode(form.getOldValidateCode());
        var flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(50065);
        }

        randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setTemplateCode(SmsTemplate.CHANGE_PHONE_VERIFY.getCode())
                .setPhone(form.getNewEmail())
                .setCode(form.getValidateCode());

        flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(50066);
        }
        int i = baseMapper.updateEmail(form.getNewEmail(), userDetails.getId());
        //刷新token
        refreshToken();
        //同步用户信息
        if (i==1){
            ConfigDTO config = configServiceClient.getConfig(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_SWITCH);
            if (config!=null && "1".equals(config.getValue()) ) {
                User user = userService.selectById(userDetails.getId());
                userInfoSyn(user);
            }
        }
        return Response.ok();
    }


    /**
     * 设置支付密码
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    public Response setPayPassword(UserDetails userDetails, ChangePayPasswordForm form) {
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(userDetails.getCountryCode())
                .setTemplateCode(SmsTemplate.FORGOT_PAY_PWD_VERIFY.getCode())
                .setEmail(form.getEmail())
                .setCode(form.getValidateCode());
        if(StringUtils.isBlank(form.getEmail())) randCodeVerifyDTO.setPhone(userDetails.getMobile());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(20007);
        }
        int count = baseMapper.updatePayPassword(userDetails.getId(), new BCryptPasswordEncoder().encode(form.getPayPassword()));
        if (count < 1) {
            throw new GlobalDefaultException(20004);
        }
        return Response.ok();
    }

    /**
     * 设置密码
     *
     * @param form
     * @return
     */
    @Override
    public Response setPassword(SetPasswordForm form) {
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(form.getCountryCode())
                .setTemplateCode(SmsTemplate.FORGOT_VERIFY.getCode())
                .setPhone(form.getMobile())
                .setEmail(form.getEmail())
                .setCode(form.getValidateCode());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            // 验证码错误
            throw new GlobalDefaultException(20007);
        }

        if(!Strings.isNullOrEmpty(form.getMobile())){
            int updateCount = 2;
            for (int i = updateCount; i > 0; i--) {
                int count = baseMapper.updatePassWordByMobile(new BCryptPasswordEncoder().encode(form.getPassword()), form.getMobile());
                if (count == 1) {
                    return Response.ok();
                }
            }
        }

        if(!Strings.isNullOrEmpty(form.getEmail())){
            int updateCount = 2;
            for (int i = updateCount; i > 0; i--) {
                int count = baseMapper.updatePassWordByEmail(new BCryptPasswordEncoder().encode(form.getPassword()), form.getEmail());
                if (count == 1) {
                    return Response.ok();
                }
            }
        }

        throw new GlobalDefaultException(20004);
    }

    /**
     * 高级身份认证
     *
     * @param userDetails
     * @param
     * @return
     */
    @Override
    @Transactional
    public Response authUser(UserDetails userDetails, AuthUser authUser) {

        if (authUser.getIdCardType()!=1){
            User sameUser = baseMapper.selectByIdCard(authUser.getIdCard());
            if (Optional.ofNullable(sameUser).isPresent()) {
                throw new GlobalDefaultException(2020);
            }
            User user = userService.selectById(userDetails.getId());

            user.setRealName(authUser.getName())
                .setIdCard(authUser.getIdCard())
                .setIdCardType(authUser.getIdCardType());

            int count = baseMapper.updateAuthName(user);
            if (count < 1) {
                throw new GlobalDefaultException(20004);
            }
            // 注册奖励
            rewardServiceClient.registerReward(userDetails.getId());
            user = baseMapper.selectById(userDetails.getId());
            // 邀请奖励
            if (!StringUtils.isEmpty(user.getDirectInviteid())) {
                rewardServiceClient.inviteReward(Long.parseLong(user.getDirectInviteid()));
            }
        }
        List<String> forms = authUser.getImgUrlList();
        List<UserAuthInfo> list = new ArrayList<>();
        long authCode = System.currentTimeMillis();
        for (int i = 0; i < forms.size(); i++) {
            UserAuthInfo userAuthInfo = new UserAuthInfo();
            userAuthInfo.setImageUrl(forms.get(i));
            userAuthInfo.setSerialno(i + 1);
            userAuthInfo.setCreated(new Date());
            userAuthInfo.setUserId(userDetails.getId());
            userAuthInfo.setLastUpdateTime(new Date());
            userAuthInfo.setAuthCode(authCode);
            list.add(userAuthInfo);
        }
        UserAuthAuditRecord ur = new UserAuthAuditRecord();
        ur.setStatus(AuthStatus.AUTHING.getValue())
                .setCreated(new Date()).
                setUserId(userDetails.getId()).
                setStep(0).
                setAuthCode(authCode);
        userAuthAuditRecordService.insert(ur);
        userService.updateById(new User().setId(userDetails.getId()).setRefeAuthId(ur.getId()));
        boolean flag = userAuthInfoService.insertBatch(list);
        if (!flag) {
            throw new GlobalDefaultException(20004);
        }
        return Response.ok();
    }

    /**
     * 设置用户基本信息
     *
     * @param userDetails
     * @param form
     * @return
     */
    @Override
    public Response setUserBase(UserDetails userDetails, UserBaseForm form) {
        // 判断用户名是否唯一
        QueryWrapper<User> e = new QueryWrapper<>();
        e.eq("username", form.getUsername());
        List<User> u = baseMapper.selectList(e);
        if (!u.isEmpty()) {
            log.error("用户名已存在");
            throw new GlobalDefaultException(40008);
        }

        String email = form.getEmail();
        if (!StringUtils.isEmpty(userDetails.getEmail())) {
            email = userDetails.getEmail();
        } else {
            //判断邮箱是否唯一
            if (StringUtils.isNotEmpty(email)) {
                QueryWrapper<User> e1 = new QueryWrapper<>();
                e1.eq("email", email);
                List<User> u1 = baseMapper.selectList(e1);
                if (!u1.isEmpty()) {
                    log.error("邮箱已存在");
                    throw new GlobalDefaultException(40010);
                }
            }
        }

        int count = baseMapper.updateUserBase(userDetails.getId(),
                form.getUsername(),
                email,
                new BCryptPasswordEncoder().encode(form.getPayPassword()));
        if (count < 1) {
            throw new GlobalDefaultException(20004);
        }
        return Response.ok();
    }

    /**
     * 查询邀请列表
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<User> selectListByInviteId(long userId) {
        return baseMapper.selectListByInviteId(String.valueOf(userId));
    }

    /**
     * 刷新 token
     *
     * @return
     * @throws AuthenticationException
     */
    private Object refreshToken() throws AuthenticationException {
        String accessToken = tokenProvider.resolveToken(request);
        if (!Strings.isNullOrEmpty(accessToken)) {
            JwtToken jwt = userDetailsService.refreshToken(accessToken);
            return Response.ok(jwt);
        } else {
            // 无效凭证，access_token无效或不是最新的。
            throw new GlobalDefaultException(40001);
        }
    }

    /**
     *  GT210对接外部系统-用户信息同步
     * @param user
     */
    public void userInfoSyn(User user){
        String json = new Gson().toJson(user);
        rabbitTemplate.convertAndSend("user.info.update.syn", json);
    }
}
