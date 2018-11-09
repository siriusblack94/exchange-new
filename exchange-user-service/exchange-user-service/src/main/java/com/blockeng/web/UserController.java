package com.blockeng.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.config.GoogleAuthProperties;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.dto.UserDTOMapper;
import com.blockeng.dto.UserDetailsMapper;
import com.blockeng.entity.AuthUser;
import com.blockeng.entity.User;
import com.blockeng.entity.UserAuthAuditRecord;
import com.blockeng.enums.AuthStatus;
import com.blockeng.feign.RandCodeServiceClient;
import com.blockeng.framework.enums.SmsTemplate;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.geetest.GeetestLib;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.framework.utils.IpUtil;
import com.blockeng.service.UserAuthAuditRecordService;
import com.blockeng.service.UserDetailsService;
import com.blockeng.service.UserService;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.web.vo.*;
import com.google.common.base.Strings;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 用户Controller
 *
 * @author qiang
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "用户信息", tags = "用户信息")
@EnableConfigurationProperties(GoogleAuthProperties.class)
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private GoogleAuthProperties googleAuthProperties;

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RandCodeServiceClient randCodeServiceClient;

    /**
     * 获取用户信息
     *
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/info")
    @ApiOperation(value = "用户信息", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    public Object info(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        User user = userService.selectById(userDetails.getId());
        UserDetails u = UserDetailsMapper.INSTANCE.toUserDetails(user);
        // 判断用户高级认证审核状态
        QueryWrapper<UserAuthAuditRecord> e1 = new QueryWrapper<>();
        e1.eq("user_id", userDetails.getId());
        e1.orderByDesc("created");
        e1.last(" limit 1");
        UserAuthAuditRecord record = userAuthAuditRecordService.selectOne(e1);
        Integer seniorAuthStatus = AuthStatus.NOTAUTH.getValue();//未上传图片
        String seniorAuthDesc = AuthStatus.NOTAUTH.getDesc();//未上传图片
        if (Optional.ofNullable(record).isPresent()) {
            seniorAuthStatus = record.getStatus();
            seniorAuthDesc = record.getRemark();
        }
        u.setSeniorAuthStatus(seniorAuthStatus);
        u.setSeniorAuthDesc(seniorAuthDesc);
        return Response.ok(u);
    }

    /**
     * 国际化语言切换
     *
     * @param request
     * @param response
     * @param lang
     * @return
     */
    @GetMapping("/lang/{lang}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lang", value = "语言-zh en", dataType = "String", paramType = "query")
    })
    @ApiOperation(value = "切换语言", httpMethod = "GET")
    Object lang(HttpServletRequest request, HttpServletResponse response, @PathVariable String lang) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

        if ("zh".equals(lang)) {
            localeResolver.setLocale(request, response, new Locale("zh", "CN"));
        } else if ("en".equals(lang)) {
            localeResolver.setLocale(request, response, new Locale("en", "US"));
        }
        return Response.ok();
    }

    /**
     * 注册
     *
     * @param form
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "首页注册信息", notes = "首页注册信息", httpMethod = "POST")
    public Object register(@ApiParam(value = "注册信息", required = true)  RegisterForm form) {
        String ip = IpUtil.getIpAddr(request);
        // 自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", "qiang");   // 网站用户id
        param.put("client_type", "web"); // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("ip_address", ip);     // 传输用户请求验证时所携带的IP
        int gtResult = geetestLib.enhencedValidateRequest(form.getGeetest_challenge(), form.getGeetest_validate(), form.getGeetest_seccode(), param);
        if (gtResult == 1) {
            return userService.register(form);
        } else {
            // 验证失败
            throw new GlobalDefaultException(40011);
        }
    }

    /**
     * 校验手机号是否已被注册
     *
     * @param mobile
     * @return
     */
    @GetMapping("/checkTel")
    @ApiOperation(value = "手机号注册校验", notes = "手机号注册校验", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "countryCode", value = "区号", required = true, dataType = "String", paramType = "query")
    })
    public Object checkTel(String mobile, String countryCode) {
        String ip = IpUtil.getIpAddr(request);
        String ipKey = "RATE_LIMITER:CHECK_TEL:ip:" + ip;
        Long ipResult = stringRedisTemplate.opsForValue().increment(ipKey, 1);
        if (ipResult.equals(1L)) {
            stringRedisTemplate.expire(ipKey, 10, TimeUnit.MINUTES);
        }
        if (ipResult > 100) {
            // 超过上限
            throw new GlobalDefaultException(40018);
        }
        if (StringUtils.isEmpty(mobile)) {
            throw new GlobalDefaultException(2005);
        }
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("mobile", mobile);
        ew.eq("country_code", countryCode);
        int count = userService.selectCount(ew);
        if (count > 0) {
            throw new GlobalDefaultException(40002);
        }
        return Response.ok();
    }

    /**
     * 校验用户名是否已被注册
     *
     * @param username
     * @return
     */
    @GetMapping("/checkUname")
    @ApiOperation(value = "用户名注册校验", notes = "用户名注册校验", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
    })
    public Object checkUname(String username) {
        String ip = IpUtil.getIpAddr(request);
        String ipKey = "RATE_LIMITER:CHECK_UNAME:ip:" + ip;
        Long ipResult = stringRedisTemplate.opsForValue().increment(ipKey, 1);
        if (ipResult.equals(1L)) {
            stringRedisTemplate.expire(ipKey, 10, TimeUnit.MINUTES);
        }
        if (ipResult > 100) {
            // 超过上限
            throw new GlobalDefaultException(40018);
        }
        if (StringUtils.isEmpty(username)) {
            throw new GlobalDefaultException(41041);
        }
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("username", username);
        int count = userService.selectCount(ew);
        if (count > 0) {
            throw new GlobalDefaultException(40008);
        }
        return Response.ok();
    }

    /**
     * 校验邮箱是否已被注册
     *
     * @param email
     * @return
     */
    @GetMapping("/checkEmail")
    @ApiOperation(value = "邮箱注册校验", notes = "邮箱注册校验", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String", paramType = "query")})
    public Object checkEmail(String email) {
        String ip = IpUtil.getIpAddr(request);
        String ipKey = "RATE_LIMITER:CHECK_EMAIL:ip:" + ip;
        Long ipResult = stringRedisTemplate.opsForValue().increment(ipKey, 1);
        if (ipResult.equals(1L)) {
            stringRedisTemplate.expire(ipKey, 10, TimeUnit.MINUTES);
        }
        if (ipResult > 100) {
            // 超过上限
            throw new GlobalDefaultException(40018);
        }
        if (StringUtils.isEmpty(email)) {
            throw new GlobalDefaultException(41042);
        }
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("email", email);
        int count = userService.selectCount(ew);
        if (count > 0) {
            throw new GlobalDefaultException(40010);
        }
        return Response.ok();
    }

    /**
     * 设置支付密码
     *
     * @param userDetails
     * @param changePayPasswordForm
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/setPayPassword")
    @ApiOperation(value = "设置资金密码", notes = "设置资金密码", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object setPayPassword(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ChangePayPasswordForm changePayPasswordForm) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        return userService.setPayPassword(userDetails, changePayPasswordForm);
    }

    /**
     * 设置用户基本信息
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/userBase")
    @ApiOperation(value = "设置用户基础信息", notes = "设置用户基础信息", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object userBase(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserBaseForm form) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        return userService.setUserBase(userDetails, form);
    }

    /**
     * 修改手机号
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updatePhone")
    @ApiOperation(value = "ACCOUNT-003 修改手机号", notes = "修改手机号", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object updatePhone(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ChangePhoneForm form) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        if (StringUtils.isEmpty(form.getOldValidateCode())) {
            throw new GlobalDefaultException(50059);
        }
        if (StringUtils.isEmpty(form.getNewMobilePhone())
                || StringUtils.isEmpty(form.getOldValidateCode())
                || StringUtils.isEmpty(form.getValidateCode())
        ) {
            throw new GlobalDefaultException(50061);
        }
        return userService.updatePhone(userDetails, form);
    }

    /**
     * 修改邮箱
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updateEmail")
    @ApiOperation(value = "ACCOUNT-004 修改邮箱", notes = "修改邮箱", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object updateEmail(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ChangeEmailForm form) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        if (StringUtils.isEmpty(form.getOldValidateCode())) {
            throw new GlobalDefaultException(50060);
        }
        if (StringUtils.isEmpty(form.getNewEmail()) || StringUtils.isEmpty(form.getOldValidateCode()) || StringUtils.isEmpty(form.getValidateCode())) {
            throw new GlobalDefaultException(50062);
        }
        return userService.updateEmail(userDetails, form);
    }


    /**
     * 修改登录密码
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updateLoginPassword")
    @ApiOperation(value = "ACCOUNT-004 修改登录密码", notes = "修改登录密码", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object updatePassword(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ChangePasswordForm form) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        return userService.updatePassword(userDetails, form);
    }

    /**
     * 查询邀请列表
     *
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/invitation")
    @ApiOperation(value = "ACCOUNT-005 邀请列表", notes = "邀请列表", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    public Object invitation(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        List<User> userList = userService.selectListByInviteId(userDetails.getId());

        return Response.ok(userList);
    }

    /**
     * 修改支付密码
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updatePayPassword")
    @ApiOperation(value = "ACCOUNT-006 修改资金密码", notes = "修改资金密码", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object updatePayPassword(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ChangePasswordForm form) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        return userService.updatePayPassword(userDetails, form);
    }

    /**
     * 实名认证
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/authAccount")
    @ApiOperation(value = "ACCOUNT-007 实名认证", notes = "实名认证", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object authAccount(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserAuthInfoForm form) {
        String ip = IpUtil.getIpAddr(request);
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", userDetails.getId().toString()); //网站用户id
        param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("ip_address", ip); //传输用户请求验证时所携带的IP
        int gtResult = geetestLib.enhencedValidateRequest(form.getGeetest_challenge(), form.getGeetest_validate(), form.getGeetest_seccode(), param);
        if (gtResult == 1) {
            // 验证成功
            return userService.updateAuthAccount(userDetails, form);
        } else {
            // 验证失败
            throw new GlobalDefaultException(40011);
        }
    }

    /**
     * 设置密码
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/setPassword")
    @ApiOperation(value = "忘记密码", notes = "忘记密码", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object setPassword(@RequestBody @Valid SetPasswordForm form) {
        return userService.setPassword(form);
    }

    /**
     * 高级实名认证
     *
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/authUser")
    @ApiOperation(value = "高级认证", notes = "高级认证", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Object AuthUser(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody AuthUser authUser) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);//用户未登录
        }
        if (authUser.getImgUrlList().isEmpty()) {
            throw new GlobalDefaultException(20010);//上传图片不能为空
        }
        return userService.authUser(userDetails, authUser);
    }

    /**
     * 根据ID查询用户信息（微服务）
     *
     * @param userId
     * @return
     */
    @ApiIgnore
    @PostMapping("/selectById")
    public UserDTO selectById(@RequestParam("userId") Long userId) {
        return UserDTOMapper.INSTANCE.from(userService.selectById(userId));
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @ApiIgnore
    @PostMapping("/loadUserByUsername")
    public Object loadUserByUsername(@RequestParam("userName") String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    /**
     * GA秘钥生成
     *
     * @param userDetails
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ga/generate")
    @ApiOperation(value = "GA秘钥生成", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = Response.class)
    })
    public Object googleAuthenticatorGenerate(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        JSONObject result = new JSONObject();
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String otpAuthTotpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(googleAuthProperties.getIssuer(), userDetails.getId().toString(), key);
        result.put("otpAuthTotpURL", otpAuthTotpURL);
        result.put("secret_key", key.getKey());
        return Response.ok(result);
    }

    /**
     * GA验证并绑定
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/ga/verify")
    @ApiOperation(value = "GA验证并绑定", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = Response.class),
            @ApiResponse(code = 40004, message = "谷歌验证码错误，请重新输入", response = Response.class)
    })
    public Object googleAuthenticatorVerify(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid GoogleAuthenticatorVerifyForm form, BindingResult result) {
        User user = userService.selectById(userDetails.getId());
        if (user.getGaStatus().equals(0)) {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            if (gAuth.authorize(form.getSecret(), form.getCode())) {
                User userVo = new User();
                userVo.setId(userDetails.getId());
                userVo.setGaSecret(form.getSecret());
                userVo.setGaStatus(1);
                userService.updateById(userVo);
                return Response.ok();
            } else {
                //谷歌验证码错误，请重新输入
                throw new GlobalDefaultException(40004);
            }
        } else {
            //您已开启二次动态口令验证
            throw new GlobalDefaultException(40015);
        }
    }

    /**
     * 关闭GA验证
     *
     * @param userDetails
     * @param form
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/ga/cancel")
    @ApiOperation(value = "关闭GA验证", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "请求成功", response = Response.class),
            @ApiResponse(code = 40004, message = "谷歌验证码错误，请重新输入", response = Response.class)
    })
    public Object googleAuthenticatorCancel(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid GoogleAuthenticatorCancelForm form, BindingResult result) {
        User user = userService.selectById(userDetails.getId());
        if (user.getGaStatus().equals(1)) {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            if (gAuth.authorize(user.getGaSecret(), form.getCode())) {
                User userVo = new User();
                userVo.setId(userDetails.getId());
                userVo.setGaSecret(null);
                userVo.setGaStatus(0);
                userService.updateById(userVo);
                return Response.ok();
            } else {
                //谷歌验证码错误，请重新输入
                throw new GlobalDefaultException(40004);
            }
        } else {
            //您未开启二次动态口令验证
            throw new GlobalDefaultException(40015);
        }
    }

    @PostMapping("/apikey")
    @PreAuthorize("isAuthenticated()")
    public Object apikey(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody ApikeyGetForm form) {
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(userDetails.getCountryCode())
                .setTemplateCode(SmsTemplate.API_KEY_GET_VERIFY.getCode())
                .setPhone(userDetails.getMobile())
                .setEmail(userDetails.getEmail())
                .setCode(form.getValidateCode());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(20007);
        } else {
            User user = userService.selectById(userDetails.getId());
            return Response.ok(user);
        }
    }

    @PostMapping("/apikey/get")
    @PreAuthorize("isAuthenticated()")
    public Object getApikey(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails, @RequestBody ApikeyGetForm form) {
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(userDetails.getCountryCode())
                .setTemplateCode(SmsTemplate.API_KEY_GET_VERIFY.getCode())
                .setPhone(userDetails.getMobile())
                .setEmail(userDetails.getEmail())
                .setCode(form.getValidateCode());
        boolean flag = randCodeServiceClient.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(20007);
        } else {
            User user = userService.selectById(userDetails.getId());
            if (Strings.isNullOrEmpty(user.getAccessKeyId())) {
                User userVo = new User();
                userVo.setId(userDetails.getId());
                String accessKeyId = RandomStringUtils.random(16, true, true);
                String accessKeySecret = RandomStringUtils.random(30, true, true);
                userVo.setAccessKeyId(accessKeyId);
                userVo.setAccessKeySecret(accessKeySecret);
                userService.updateById(userVo);
                return Response.ok(userVo);
            } else {
                //已开启API访问权限
                throw new GlobalDefaultException(42001);
            }
        }
    }
}