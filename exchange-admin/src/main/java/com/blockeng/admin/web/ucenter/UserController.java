package com.blockeng.admin.web.ucenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.UserAuthInfoDTO;
import com.blockeng.admin.dto.UserDTO;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.*;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.dto.SendForm;
import com.blockeng.dto.SmsDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.feign.RewardServiceClient;
import com.blockeng.feign.SmsServiceClient;
import com.blockeng.framework.constants.Constant;
import com.google.gson.Gson;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/user")

@Api(value = "用户管理controller", tags = {"平台的注册的用户"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private SmsServiceClient smsServiceClient;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private RewardServiceClient rewardServiceClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Log(value = "查询用户管理列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户管理列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "移动电话号码", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "用户真实名称", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：0，禁用；1，启用", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
            String userId,
            String mobile,
            String userName,
            String realName,
            String status,
            Integer[] auth_status,
            String email,
            int current,
            int size) {
        EntityWrapper<UserDTO> ew = new EntityWrapper<>();
        Page<UserDTO> pager = new Page<>(current, size);
        //去除了密码这些敏感值
        String sql = "id, type, username, country_code AS countryCode, mobile, email, real_name AS realName, id_card_type AS idCardType, auth_status AS authStatus, id_card AS idCard, level, authtime, logins, status, invite_code AS inviteCode, invite_relation AS inviteRelation, direct_inviteid AS directInviteid, is_deductible AS isDeductible, reviews_status AS reviewsStatus, agent_note AS agentNote, last_update_time AS lastUpdateTime, access_key_id as accessKeyId,access_key_secret as  accessKeySecret ,created,paypass_setting as paypassSetting";
        ew.setSqlSelect(sql);
        if (StringUtils.isNotBlank(userId)) {
            ew.eq("id", userId);
        }
        if (StringUtils.isNotBlank(mobile)) {
            ew.like("mobile", mobile);
        }
        if (StringUtils.isNotBlank(userName)) {
            ew.like("username", userName);
        }
        if(StringUtils.isNotBlank(email)){
            ew.like("email",email);
        }
        if (StringUtils.isNotBlank(realName)) {
            ew.like("real_name", realName);
        }
        if (StringUtils.isNotBlank(status)) {
            ew.eq("status", status);
        }
        if (auth_status != null && auth_status.length > 0) {
            ew.in("auth_status", auth_status);
        }
        return ResultMap.getSuccessfulResult(userService.selectListPage(pager, ew));
    }


    @Log(value = "查询该用户邀请的用户列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_query')")
    @GetMapping
    @RequestMapping({"/getDirectInviteidList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户邀请的用户列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "移动电话号码", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "用户真实名称", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：0，禁用；1，启用", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = User.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getDirectInviteidList(
            String userId,
            String mobile,
            String userName,
            String realName,
            String status,
            Integer[] auth_status,
            int current,
            int size) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        Page<User> pager = new Page<>(current, size);
        Page<InvitedUserInfo> invitedUserInfoPage = new Page<>(current, size);
        //去除了密码这些敏感值
        String sql = "id, type, username, country_code AS countryCode, mobile, email, real_name AS realName, id_card_type AS idCardType, auth_status AS authStatus, id_card AS idCard, level, authtime, logins, status, invite_code AS inviteCode, invite_relation AS inviteRelation, direct_inviteid AS directInviteid, is_deductible AS isDeductible, reviews_status AS reviewsStatus, agent_note AS agentNote, last_update_time AS lastUpdateTime, access_key_id as accessKeyId,access_key_secret as  accessKeySecret ,created,paypass_setting as paypassSetting";
        ew.setSqlSelect(sql);
        if (StringUtils.isNotBlank(mobile)) {
            ew.like("mobile", mobile);
        }
        if (StringUtils.isNotBlank(userName)) {
            ew.like("username", userName);
        }
        if (StringUtils.isNotBlank(realName)) {
            ew.like("real_name", realName);
        }
        if (StringUtils.isNotBlank(status)) {
            ew.eq("status", status);
        }
        if (auth_status != null && auth_status.length > 0) {
            ew.in("auth_status", auth_status);
        }

        ew.eq("direct_inviteid", userId);
        ew.orderBy("id", false);
        //被邀请人持币量
        Long plantCoinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue());
        Page<User> userPage = userService.selectPage(pager, ew);
        List<User> invitedUserList = userPage.getRecords();
        List<InvitedUserInfo> pageRecord = new ArrayList<>();
        for (User user : invitedUserList) {
            EntityWrapper<Account> aw = new EntityWrapper<>();
            aw.eq("user_id",user.getId()).eq("coin_id",plantCoinId);
            InvitedUserInfo invitedUser = new InvitedUserInfo();
            Account account = accountService.selectOne(aw);
            BeanUtils.copyProperties(user,invitedUser);
            invitedUser.setAmount(account.getBalanceAmount().add(account.getFreezeAmount()));
            pageRecord.add(invitedUser);
        }
        invitedUserInfoPage.setRecords(pageRecord);
        return ResultMap.getSuccessfulResult(invitedUserInfoPage);

    }


    @Log(value = "查询实名认证审核列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_auth_query')")
    @GetMapping
    @RequestMapping({"/getUserAuthList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "实名认证审核列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "移动电话号码", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "用户真实名称", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userAuthStatus", value = "认证状态", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getUserAuthList(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "mobile", defaultValue = "") String mobile,
            @RequestParam(value = "userName", defaultValue = "") String userName,
            @RequestParam(value = "realName", defaultValue = "") String realName,
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @RequestParam(value = "userAuthStatus", defaultValue = "") String userAuthStatus,
            @RequestParam(value = "size", defaultValue = "10") int size) {


        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }

        Wrapper<UserDTO> ew = new EntityWrapper<>();


        if (!StringUtils.isEmpty(userId)) {
            ew.eq("user1.id", userId);
        }

        if (!StringUtils.isEmpty(mobile)) {
            ew.eq("user1.mobile", mobile);
        }

        if (!StringUtils.isEmpty(userName)) {
            ew.eq("user1.username", userName);
        }

        if (!StringUtils.isEmpty(realName)) {
            ew.eq("user1.real_name", realName);
        }

        if (!StringUtils.isEmpty(userAuthStatus))
            ew.eq("auth.status", userAuthStatus);
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime))
            ew.between("auth.created", startTime, endTime);

        Page<UserDTO> pager = new Page<>(current, size);

        Page<UserDTO> userDTOPage = userService.selectListAuditPage(pager, ew);

        return ResultMap.getSuccessfulResult(userDTOPage);
//        List<UserDTO> userDTO;
//
//        if (!ew.isEmptyOfWhere()) {//按照精准查找的sql走
//            userDTO = userService.selectListAuditFromUser(ew);
//            return ResultMap.getSuccessfulResult(new PageDTO().setCurrent(current).setTotal(userDTO.size()).setSize(size).setRecords(userDTO));
//        } else { //按照非精准查找走
//            if (StringUtils.isEmpty(userAuthStatus)) {
//                userAuthStatus = "0";
//            }
//            current = (current - 1) * size;
//            authEw.isNull("auth1.id");
//            if (!StringUtils.isEmpty(userAuthStatus))
//                authEw.eq("auth1.status", userAuthStatus);
//            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime))
//                authEw.between("created", startTime, endTime);
//            //userDTO = userService.selectListAuditFromAuth(current, size, userAuthStatus,startTime,endTime);
//            userDTO = userService.selectListAuditFromAuth(current, size, userAuthStatus, startTime, endTime);
//            return ResultMap.getSuccessfulResult(new PageDTO().setCurrent(current).setTotal(userService.selectAuditAccount(authEw)).setSize(size).setRecords(userDTO));
//        }
    }

    @Log(value = "查询用戶认证审核记录列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_auth_query')")
    @GetMapping
    @RequestMapping({"/getUserAuthRecordList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用戶认证审核记录列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用戶id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页条数", dataType = "Integer")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserAuthAuditRecord.class),
            @ApiResponse(code = 1, message = "失败")

    })
    public ResultMap getUserAuthRecordList(
            String userId,
            Integer current,
            Integer size) {
        EntityWrapper<UserAuthAuditRecord> ew = new EntityWrapper<>();
        if (current == null) {
            current = 1;
        }
        if (size == null) {
            size = 10;
        }
        Page<UserAuthAuditRecord> pager = new Page<UserAuthAuditRecord>(current, size);
        //去除了密码这些敏感值
        if (StringUtils.isNotBlank(userId)) {
            ew.eq("user_id", userId);
        }
        ew.isNotNull("audit_user_id");
        ew.orderBy("id", false);
        return ResultMap.getSuccessfulResult(userAuthAuditRecordService.selectPage(pager, ew));
    }

    /**
     * 获取某个用户详情
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "查询获取某个用户详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取某个用户详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = User.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("UserController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        User user = userService.selectById(id);
        log.info("user:" + user);
        user.setPassword(null);
        user.setPaypassword(null);
        user.setGaSecret(null);
        return ResultMap.getSuccessfulResult(user);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Log(value = "更新用户信息", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('user_update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新用户信息", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "更新用户信息user对象", required = true, dataType = "User")
    public ResultMap update(@RequestBody User user) {
        log.info("UserController update:" + user.toString());
        if (null != user && user.getId() == null) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (null != user && StringUtils.isEmpty(user.getMobile())) {
            return ResultMap.getFailureResult("手机不能为空！");
        }
        if (null != user && !StringUtils.isEmpty(user.getMobile()) && user.getMobile().length() > 50) {
            return ResultMap.getFailureResult("手机长度不能超过50！");
        }
        String ms = "操作成功";
        try {
            User user2 = userService.selectById(user.getId());
            if (null == user2) {
                return ResultMap.getFailureResult("该用户不存在!");
            }
            if (user.getPassword() != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }
            if (user.getPaypassword() != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPaypassword()));
            }
            Boolean rs = userService.updateById(user);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("UserController update:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 用户实名审核1-初级实名认证；2-高级实名认证
     */
    @Log(value = "用户实名高级认证", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('user_auth_audit')")
    @PostMapping
    @RequestMapping({"/userAuthReviewStatus"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户实名高级认证", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "authStatus", value = "审核状态1通过2拒绝", required = true, dataType = "String"),
            @ApiImplicitParam(name = "note", value = "拒绝的原因", dataType = "String"),
            @ApiImplicitParam(name = "authCode", value = "图片authCode", required = true, dataType = "String")
    })
    @Transactional
    public ResultMap userAuthReviewStatus(@RequestParam(value = "id") String id,
                                          @RequestParam(value = "authStatus") String authStatus,
                                          String note, @RequestParam(value = "authCode") String authCode) {
        log.info("UserController userAuthReviewStatus id:" + id + ",authStatus:" + authStatus + ",note:" + note + ",authCode:" + authCode);
        if (StringUtils.isBlank(id)) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (StringUtils.isEmpty(authStatus)) {
            return ResultMap.getFailureResult("请选择一个认证状态！");
        }
        if (StringUtils.isEmpty(authCode)) {
            return ResultMap.getFailureResult("请选择传入图片的authCode值！");
        }
        if (!authStatus.equals("1") && !authStatus.equals("2")) {
            return ResultMap.getFailureResult("审核状态无效!");
        }
        if (!StringUtils.isEmpty(authStatus) && authStatus.equals(String.valueOf(CommonUtils.REVIEWSTATUS_2)) && StringUtils.isBlank(note)) {
            return ResultMap.getFailureResult("请填写拒绝原因");
        }
        int status = Integer.valueOf(authStatus) == CommonUtils.REVIEWSTATUS_1 ? CommonUtils.AUTH_STATUS_2 : CommonUtils.AUTH_STATUS_1;
        EntityWrapper<User> ew = new EntityWrapper<>();

        String ms = "操作失败" + id;
        try {
            User user = userService.selectById(Long.valueOf(id));
            user.setAuthStatus(status).setAuthtime(new Date());
            if (user.getIdCardType()!=1){
                if (authStatus.equals(String.valueOf(CommonUtils.AUTH_STATUS_2))){
                    user.setIdCard(StringUtils.EMPTY);
                }
            }
            Boolean rs = userService.updateById(user);
            if(authStatus.equals(String.valueOf(CommonUtils.AUTH_STATUS_1))){
                // 注册奖励
                rewardServiceClient.registerReward(user.getId());
                // 邀请奖励
                if (!StringUtils.isEmpty(user.getDirectInviteid())) {
                    rewardServiceClient.inviteReward(Long.parseLong(user.getDirectInviteid()));
                }

                Config config = configService.queryBuyCodeAndType(Constant.CONFIG_TYPE_SYSTEM, Constant.EXTEND_SWITCH);
                if (config!=null && "1".equals(config.getValue()) ){
                    //同步用户信息
                    log.info("user: "+user);
                    userInfoSyn(user);
                }
            }
            //记录审核人的消息s
            SysUser loginUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //记录实名认证审核信息
            EntityWrapper<UserAuthAuditRecord> ew1 = new EntityWrapper<>();
            ew1.eq("auth_code", authCode);
            ew1.orderBy("created", false);
            ew1.last(" limit 1");
            UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordService.selectOne(ew1);
            Boolean rs1;
            if (userAuthAuditRecord == null) {
                userAuthAuditRecord = new UserAuthAuditRecord();
            }
            userAuthAuditRecord.setAuthCode(Long.valueOf(authCode));
            userAuthAuditRecord.setStatus(Integer.valueOf(authStatus));
            userAuthAuditRecord.setStep(2);//身份证审核，是第二步
            userAuthAuditRecord.setRemark(note);
            userAuthAuditRecord.setUserId(Long.valueOf(id));
            userAuthAuditRecord.setAuditUserId(loginUser.getId());
            userAuthAuditRecord.setAuditUserName(loginUser.getFullname());
            if (userAuthAuditRecord.getId() == null) {
                userAuthAuditRecord = new UserAuthAuditRecord();
                rs1 = userAuthAuditRecordService.insert(userAuthAuditRecord);
            } else {
                rs1 = userAuthAuditRecordService.updateById(userAuthAuditRecord);
            }
            userService.updateById(new User().setId(userAuthAuditRecord.getUserId()).setRefeAuthId(userAuthAuditRecord.getId()));
            if (rs && rs1) {
                ms = "操作成功";
                return ResultMap.getSuccessfulResult(ms);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("UserController userAuthReviewStatus:" + e);
        }
        return ResultMap.getFailureResult(ms);
    }

    /**
     * 获取用户认证详情
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "获取用户认证详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_auth_query')")
    @GetMapping
    @RequestMapping({"/getuserAuthObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取用户认证详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserAuthInfoDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getuserAuthObj(Long id) {
        log.info("UserController getuserAuthObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        User user = userService.selectById(id);
        user.setPassword(null);
        user.setPaypassword(null);
        user.setGaSecret(null);
        EntityWrapper<UserAuthAuditRecord> ew1 = new EntityWrapper<UserAuthAuditRecord>();
        ew1.eq("user_id", id);
        ew1.orderBy("created", false);
        ew1.last(" limit 1");
        UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordService.selectOne(ew1);
        List<UserAuthInfo> userAuthInfos = null;
        if (userAuthAuditRecord != null) {
            EntityWrapper<UserAuthInfo> ew = new EntityWrapper<UserAuthInfo>();
            ew.eq("user_id", id);
            ew.eq("auth_code", userAuthAuditRecord.getAuthCode());
            userAuthInfos = userAuthInfoService.selectList(ew);
        } else {
            EntityWrapper<UserAuthInfo> ew = new EntityWrapper<UserAuthInfo>();
            ew.eq("user_id", id);
            ew.orderBy("created", false);
            ew.last(" limit 3");
            userAuthInfos = userAuthInfoService.selectList(ew);
        }
        UserAuthInfoDTO userAuthInfoDTO = new UserAuthInfoDTO();
        userAuthInfoDTO.setUser(user);
        userAuthInfoDTO.setUserAuthInfoList(userAuthInfos);
        if (userAuthAuditRecord != null && userAuthAuditRecord.getAuditUserId() != null) {
            userAuthInfoDTO.setUserAuthAuditRecord(userAuthAuditRecord);
        }
        return ResultMap.getSuccessfulResult(userAuthInfoDTO);
    }

    /**
     * 用户禁用启用
     */
    @Log(value = "禁/启用户", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('user_update')")
    @GetMapping
    @RequestMapping({"/updateStatus"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "禁/启用户", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态值0禁1启用", required = true, dataType = "String")
    })
    public ResultMap updateStatus(String id, String status) {
        log.info("UserController updateStatus id:" + id + ",status:" + status);
        if (StringUtils.isBlank(id)) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (StringUtils.isBlank(status)) {
            return ResultMap.getFailureResult("必要参数status不能为空！");
        }
        String ms = "操作成功";
        try {
            User user = new User();
            user.setId(Long.valueOf(id));
            user.setStatus(Integer.valueOf(status));
            Boolean rs = userService.updateById(user);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("UserController updateStatus:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    @Log(value = "用户导出", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('user_export')")
    @GetMapping
    @RequestMapping({"/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户导出", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "移动电话号码", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "用户真实名称", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：0，禁用；1，启用", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "创建时间-开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "创建时间-结束时间", dataType = "String")
    })
    public void exportList(String userId,
                           String mobile,
                           String userName,
                           String realName,
                           String status,
                           String startTime,
                           String endTime, HttpServletResponse response) throws IOException {
        EntityWrapper<User> ew = new EntityWrapper<>();
        //去除了密码这些敏感值
        String sql = "id, username, mobile, email, real_name AS realName, id_card_type AS idCardType,status,created";
        ew.setSqlSelect(sql);
        if (StringUtils.isNotBlank(userId)) {
            ew.eq("id", userId);
        }
        if (StringUtils.isNotBlank(mobile)) {
            ew.like("mobile", mobile);
        }
        if (StringUtils.isNotBlank(userName)) {
            ew.like("username", userName);
        }
        if (StringUtils.isNotBlank(status)) {
            ew.eq("status", status);
        }
        if (StringUtils.isNotBlank(realName)) {
            ew.like("real_name", realName);
        }
        if (StringUtils.isNotBlank(startTime)) {
            ew.ge("created", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            ew.le("created", endTime);
        }
        List<User> dataList = userService.selectList(ew);
        log.info("--dataList--" + dataList.size());
        String[] header = {"ID", "用户名", "手机号", "邮箱", "真实姓名", "身份证号", "状态", "注册时间"};
        String[] properties = {"id", "username", "mobile", "email", "realName", "idCardType", "status", "created"};
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //有多少列就写多少个，不要处理的就null
        CellProcessor[] PROCESSORS = new CellProcessor[]{
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                null,
                null,
                null,
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        if (String.valueOf(value).equals("0")) {
                            return "禁用";
                        }
                        if (String.valueOf(value).equals("1")) {
                            return "启用";
                        }

                        return "未知";
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String dateString = "\t" + formatter.format(value);
                        return dateString;
                    }
                }};
        String fileName = "CNY充值记录.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, dataList, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Log(value = "代理人注册审核列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_agent_auth_query')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "代理人注册审核列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", value = "代理等级", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "电话号码", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "真实名称", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")

    })
    @RequestMapping({"/reviewSubagentList"})
    @ResponseBody
    public ResultMap reviewSubagentList(int current,
                                        int size,
                                        @RequestParam(value = "level", defaultValue = "") String level,
                                        @RequestParam(value = "userId", defaultValue = "") String userId,
                                        @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                        @RequestParam(value = "realName", defaultValue = "") String realName) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        Page<User> pager = new Page<User>(current, size);
        if (StringUtils.isNotBlank(userId)) {
            ew.eq("id", userId);
        }
        if (StringUtils.isNotBlank(mobile)) {
            ew.like("mobile", mobile);
        }
        if (StringUtils.isNotBlank(realName)) {
            ew.eq("real_name", realName);
        }
        if (StringUtils.isNotBlank(level)) {
            ew.eq("level", level);
        }
        ew.orderBy("id", false);
        ew.eq("type", CommonUtils.USER_TYPE_2);
        return ResultMap.getSuccessfulResult(userService.selectPage(pager, ew));
    }

    /**
     * 代理人注册审核
     */
    @Log(value = "代理人注册审核", type = SysLogTypeEnum.AUDIT)
    @PreAuthorize("hasAuthority('user_agent_auth_audit')")
    @PostMapping
    @RequestMapping({"/updateReviewsStatus"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "代理人注册审核", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "reviewsStatus", value = "审核状态1通过2拒绝", required = true, dataType = "String"),
            @ApiImplicitParam(name = "note", value = "拒绝的原因", required = true, dataType = "String")
    })
    public ResultMap updateReviewsStatus(@RequestParam(value = "id") String id,
                                         @RequestParam(value = "reviewsStatus") String reviewsStatus,
                                         String note) {


        log.info("UserController updateReviewsStatus id:" + id + ",reviewsStatus:" + reviewsStatus + ",note:" + note);
        if (StringUtils.isBlank(id)) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (StringUtils.isEmpty(reviewsStatus)) {
            return ResultMap.getFailureResult("请选择一个审核状态！");
        }
        if (!reviewsStatus.equals("1") && !reviewsStatus.equals("2")) {
            return ResultMap.getFailureResult("审核状态无效!");
        }
        if (!StringUtils.isEmpty(reviewsStatus) && reviewsStatus.equals(String.valueOf(CommonUtils.REVIEWSTATUS_2)) && StringUtils.isBlank(note)) {
            return ResultMap.getFailureResult("请填写拒绝原因");
        }
        EntityWrapper<User> ew = new EntityWrapper<>();
        //int staus = review == CommonUtils.REVIEWSTATUS_2 ? 0 : 1;
        String ms = "操作失败" + id;
        try {
            User user1 = userService.selectById(Long.valueOf(id));
            if (user1 == null) {
                return ResultMap.getFailureResult("不存在该用户信息");
            }

            //系统最高代理不能修改
            if (StringUtils.isNotBlank(user1.getUsername()) && user1.getUsername().equals("AgentAdmin") && reviewsStatus.equals(String.valueOf(CommonUtils.REVIEWSTATUS_2))) {
                return ResultMap.getFailureResult("系统默认代理！不能进行修改");
            }

            User user = new User();
            user.setId(Long.valueOf(id));
            //  user.setStatus(staus);
            user.setReviewsStatus(Integer.valueOf(reviewsStatus));
            user.setAgentNote(note);
            Boolean rs = userService.updateById(user);
            log.info("e--dlr-updateReviwsStatus-----user:" + user + ",rs:" + rs);
            //记录审核人的消息
            SysUser loginUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("--dlr-updateReviewsStatus-----reviewId:" + loginUser.getId());
            log.info("--dlr-updateReviewsStatus-----reviewName:" + loginUser.getFullname());
            if (rs) {
                ms = "操作成功";
                String msc="【币小牛】恭喜您注册成为代理商用户。";
                SmsDTO smsDTO = new SmsDTO();
                smsDTO.setCountryCode(user1.getCountryCode());
                smsDTO.setMobile(user1.getMobile());
                smsDTO.setEmail(user1.getEmail());
                smsDTO.setTemplateCode("REGISTER_AGENT");
                log.info("updateReviewsStatus sms" + smsDTO);
                SendForm sendForm = new SendForm();
                sendForm.setCountryCode(user.getCountryCode())
                        .setMobile(user.getMobile())
                        .setTemplateCode("REGISTER_AGENT")
                        .setEmail(user.getEmail());


                smsServiceClient.sendTo(sendForm);
                return ResultMap.getSuccessfulResult(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("UserController updateStatus:" + e);
        }
        return ResultMap.getFailureResult(ms);
    }

    @Log(value = "查询代理商列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_agent_query')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "代理商列表,代理商下级列表,如果是代理商下级列表，必须传入用户id和lower=2", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", value = "代理等级", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id，如果是代理下级，必须传入该值", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "lower", value = "查代理商下级使用的，传入值2", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "电话号码", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "trueName", value = "真实名称", defaultValue = "", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")

    })
    @RequestMapping({"/subagentList"})
    @ResponseBody
    public ResultMap subagentList(
            String level,
            String userId,
            String lower,
            String mobile,
            String trueName, int current, int size) {

        log.info("UserController subagentList lower:" + lower + ",userId:" + userId);
        Page<User> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(lower) && lower.equals("2")) {
            if (StringUtils.isEmpty(userId)) {
                return ResultMap.getSuccessfulResult();
            }
            User user = userService.selectById(Long.valueOf(userId));
            if (user == null) {
                return ResultMap.getFailureResult("错误用户id");
            }
            EntityWrapper<User> ew = new EntityWrapper<>();
            // ew.eq("status",CommonUtils.STATUS_1);
            ew.eq("reviews_status", CommonUtils.REVIEWSTATUS_1);
            ew.eq("type", CommonUtils.USER_TYPE_2);
            ew.andNew().in("invite_relation", userId).or().eq("direct_inviteid", userId);
            ew.orderBy("id", false);
            return ResultMap.getSuccessfulResult(userService.selectPage(pager, ew));
        } else {
            EntityWrapper<User> ew = new EntityWrapper<>();
            if (StringUtils.isNotBlank(userId)) {
                ew.eq("id", userId);
            }
            if (StringUtils.isNotBlank(mobile)) {
                ew.like("mobile", mobile);
            }
            if (StringUtils.isNotBlank(trueName)) {
                ew.like("real_name", trueName);
            }
            if (StringUtils.isNotBlank(level)) {
                ew.eq("level", level);
            }
            //  ew.eq("status",CommonUtils.STATUS_1);
            ew.eq("reviews_status", CommonUtils.REVIEWSTATUS_1);
            ew.eq("type", CommonUtils.USER_TYPE_2);
            ew.orderBy("id", false);
            return ResultMap.getSuccessfulResult(userService.selectPage(pager, ew));
        }
    }

    /**
     * 获取要当前用户代理信息
     *
     * @param id 用户id
     * @return
     */
    @Log(value = "获取要当前用户代理信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_agent_query')")
    @GetMapping
    @RequestMapping({"/getSubagentInfo"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取要当前用户代理信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long")
    public ResultMap getSubagentInfo(@RequestParam(value = "id", defaultValue = "0") long id) {
        //获取用户信息
        log.info("UserController  getSubagentInfo id:" + id);
        List<User> listsubAgent = new ArrayList<>();
        User user = userService.selectById(id);
        if (user == null) {
            return ResultMap.getFailureResult("用户不存在！");
        }
        EntityWrapper<User> ew = new EntityWrapper<>();
        //获取除了自己的所有代理商
        ew.eq("type", CommonUtils.USER_TYPE_2);
        ew.eq("status", CommonUtils.STATUS_1);
        ew.eq("reviews_status", CommonUtils.REVIEWSTATUS_1);
        ew.ne("id", id);
        listsubAgent = this.userService.selectList(ew);

        //自己代理的下级代理商
        ew = new EntityWrapper<>();
        ew.eq("type", CommonUtils.USER_TYPE_2);
        ew.eq("status", CommonUtils.STATUS_1);
        ew.eq("reviews_status", CommonUtils.REVIEWSTATUS_1);
        ew.like("invite_relation", String.valueOf(id));
        List<User> agentChs = this.userService.selectList(ew);
        //移除自己代理的下级代理商
        listsubAgent.removeAll(agentChs);
        return ResultMap.getSuccessfulResult(listsubAgent);
    }

    /**
     * 更新编辑代理信息
     */
    @Log(value = "更新编辑代理信息", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('user_agent_update')")
    @PostMapping
    @RequestMapping({"/updatesubagent"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新编辑代理信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "changeUserId", value = "上级代理商id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "status", value = "代理商状态", required = true, dataType = "int"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "countryCode", value = "区号", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "姓名", dataType = "String"),
            @ApiImplicitParam(name = "idCard", value = "身份证", dataType = "String"),
            @ApiImplicitParam(name = "agentNote", value = "审核备注", dataType = "String"),

    })
    @Transactional
    public ResultMap updatesubagent(long userId,
                                    long changeUserId,
                                    int status,
                                    String mobile,
                                    String countryCode,
                                    String email,
                                    String username,
                                    String idCard,
                                    String agentNote
    ) {
        log.info("UserController updatesubagent id:" + userId + ",changeUserId:" + changeUserId + ",status:" + status);
        log.info("UserController updatesubagent mobile:" + mobile + ",countryCode:" + countryCode + ",email:" + email + ",username:" + username + ",idCard:" + idCard + ",agentNote:" + agentNote);

        if (!StringUtils.isEmpty(mobile) && mobile.length() > 50) {
            return ResultMap.getFailureResult("手机长度不能超过50！");
        }
        if (!StringUtils.isEmpty(countryCode) && countryCode.length() > 50) {
            return ResultMap.getFailureResult("手机区号长度不能超过50！");
        }
        if (!StringUtils.isEmpty(email) && email.length() > 50) {
            return ResultMap.getFailureResult("邮箱不能超过50！");
        }
        if (!StringUtils.isEmpty(username) && username.length() > 50) {
            return ResultMap.getFailureResult("用户不能超过50！");
        }
        if (!StringUtils.isEmpty(idCard) && idCard.length() > 50) {
            return ResultMap.getFailureResult("身份证长度不能超过50！");
        }
        if (userId <= 0 || changeUserId <= 0) {
            return ResultMap.getFailureResult("用户id和代理id值不能为空");
        } else {
            try {
                User user = userService.selectById(userId);
                if (user == null) {
                    return ResultMap.getFailureResult("不存在的用户！");
                }
                //系统最高代理不能修改
                if (StringUtils.isNotBlank(user.getUsername()) && user.getUsername().equals("AgentAdmin")) {
                    return ResultMap.getFailureResult("系统默认代理！不能进行修改");
                }
                user.setAgentNote(agentNote);
                user.setUsername(username);
                user.setEmail(email);
                user.setMobile(mobile);
                user.setCountryCode(countryCode);
                user.setIdCard(idCard);
                Boolean res = userService.updateById(user);//更新用户基本信息
                //用户不为空！用户的直接上级和更改的不一样，更改
                if (null != user && user.getDirectInviteid() != changeUserId + "") {
                    //获取更变的用户信息
                    User changeUser = userService.selectById(changeUserId);
                    int changeLevel = changeUser.getLevel() + 1; //更改的级别
                    int oldLevel = user.getLevel();//当前的级别
                    String oldRelation = user.getInviteRelation();//当前用户的间接关系
                    int countLevel = oldLevel - changeLevel; //当前级别减去更改到的级别,如果是正数说明是升级,0是级别不变,负数是降级
                    //获取当前前用户的下级代理信息
                    EntityWrapper<User> ew = new EntityWrapper<>();
                    ew = new EntityWrapper<>();
                    ew.eq("type", CommonUtils.USER_TYPE_2);
                    ew.like("invite_relation", String.valueOf(user.getId()));

                    List<User> childList = userService.selectList(ew);
                    childList.add(user);//同时把自己的信息加入统一处理
                    for (User item : childList) {
                        //下级代理的新的代理关系
                        String newRelation = StringUtils.isEmpty(changeUser.getInviteRelation()) ? "" + changeUser.getId() + "," :
                                changeUser.getInviteRelation() + changeUser.getId() + ",";
                        String inviteRelation = item.getInviteRelation();//获取当前下级代理的间接上级
                        String newInviteRelation = newRelation;
                        if (StringUtils.isNotBlank(inviteRelation)) {
                            newInviteRelation = inviteRelation.replace(oldRelation, newRelation);
                        }
                        if (userId == item.getId()) {
                            item.setDirectInviteid(changeUserId + "");
                        }
                        item.setInviteRelation(newInviteRelation);
                        item.setLevel(item.getLevel() - countLevel);//更改下级代理的级别
                        User userNew = new User();
                        userNew.setId(item.getId());
                        userNew.setLevel(item.getLevel());
                        userNew.setInviteRelation(item.getInviteRelation());
                        userNew.setDirectInviteid(item.getDirectInviteid());
                        Boolean rs = userService.updateById(userNew);
                        if (!rs) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return ResultMap.getFailureResult("系统出错！");
                        }
                    }
                }
                if (null != user && user.getStatus() != status) {
                    User userNew = new User();
                    userNew.setId(userId);
                    userNew.setStatus(status);
                    Boolean rs = userService.updateById(userNew);
                    if (!rs) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResultMap.getFailureResult("系统出错！");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("updatesubagent err:" + e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultMap.getFailureResult("系统出错！");
            }
            return ResultMap.getSuccessfulResult("修改成功");
        }
    }

    /**
     *  GT210对接外部系统-用户信息同步
     * @param user
     */
    public void userInfoSyn(User user){
        String json = new Gson().toJson(user);
        rabbitTemplate.convertAndSend("user.info.syn", json);
    }
}
