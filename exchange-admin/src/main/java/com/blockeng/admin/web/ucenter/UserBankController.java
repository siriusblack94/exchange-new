package com.blockeng.admin.web.ucenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.UserBankDTO;
import com.blockeng.admin.entity.UserBank;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.UserBankService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户人民币提现地址 前端控制器
 * </p>
 * 这个是用户自己添加的银行卡
 *
 * @author lxl
 * @since 2018-05-15
 */
@Slf4j
@RestController
@RequestMapping("/userBank")

@Api(value = "用户人民币提现地址controller", tags = {"注册用户银行卡管理"})
public class UserBankController {

    @Autowired
    private UserBankService userBankService;

    @Log(value = "查询银行卡管理列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_bank_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "银行卡管理列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "realName", value = "开户名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "用户手机号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "usrId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserBankDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(int size, int current,
                             String coinName,
                             String realName,
                             String mobile,
                             String usrId) {
        Page<UserBankDTO> pager = new Page<>(current, size);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("coinName", coinName);
        paramMap.put("realName", realName);
        paramMap.put("mobile", mobile);
        paramMap.put("ID", usrId);
        return ResultMap.getSuccessfulResult(userBankService.selectMapPage(pager, paramMap));
    }

    /**
     * 获取用户银行卡详情信息
     *
     * @param id 银行卡id
     * @return
     */
    @Log(value = "获取用户银行卡详情信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_bank_info_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取用户银行卡详情信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "银行卡id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserBank.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("UserBankController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        UserBank userBank = userBankService.selectById(id);
        return ResultMap.getSuccessfulResult(userBank);
    }

    /**
     * 更新
     *
     * @param userBank
     * @return
     */
    @Log(value = "更新银行卡信息", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('user_bank_update')")
    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新银行卡信息", httpMethod = "POST")
    public ResultMap update(@RequestBody UserBank userBank) {
        log.info("UserBankController update:" + userBank.toString());
        if (null != userBank && userBank.getId() == null) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (null != userBank && StringUtils.isEmpty(userBank.getRemark())) {
            return ResultMap.getFailureResult("银行卡名称不能为空！");
        }
        if (null != userBank && !StringUtils.isEmpty(userBank.getRemark()) && userBank.getRemark().length() > 255) {
            return ResultMap.getFailureResult("银行卡名称不能超过255！");
        }
        if (null != userBank && StringUtils.isEmpty(userBank.getRealName())) {
            return ResultMap.getFailureResult("开户人不能为空！");
        }
        if (null != userBank && !StringUtils.isEmpty(userBank.getRealName()) && userBank.getRealName().length() > 255) {
            return ResultMap.getFailureResult("开户人不能超过255！");
        }
        if (null != userBank && StringUtils.isEmpty(userBank.getBank())) {
            return ResultMap.getFailureResult("开户行不能为空！");
        }
        if (null != userBank && !StringUtils.isEmpty(userBank.getBank()) && userBank.getBank().length() > 255) {
            return ResultMap.getFailureResult("开户行不能超过255！");
        }
        if (null != userBank && StringUtils.isEmpty(userBank.getBankCard())) {
            return ResultMap.getFailureResult("开户账号不能为空！");
        }
        if (null != userBank && !StringUtils.isEmpty(userBank.getBankCard()) && userBank.getBankCard().length() > 255) {
            return ResultMap.getFailureResult("开户账号不能超过255！");
        }
        UserBank userBank1 = userBankService.selectById(userBank.getId());
        if (userBank1 != null && !userBank1.getBankCard().equals(userBank.getBankCard())) {
            UserBank userBank2 = userBankService.selectOne(new EntityWrapper<UserBank>().eq("bank_card", userBank.getBankCard()));
            if (userBank2 != null) {
                return ResultMap.getFailureResult("该银行卡号已存在，请换个卡号！");
            }
        }
        String ms = "操作成功";
        try {
            Boolean rs = userBankService.updateById(userBank);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("UserBankController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 禁用启用
     */
    @Log(value = "禁/启银行卡信息", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('user_bank_update')")
    @RequestMapping(value = {"/updateStatus"}, method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "禁/启银行卡信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "银行卡id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态值0禁1启用", required = true, dataType = "String")
    })
    public ResultMap updateStatus(@RequestParam(value = "id") String id, @RequestParam(value = "status") String status) {
        log.info("UserBankController updateStatus id:" + id + ",status:" + status);
        if (StringUtils.isBlank(id)) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (StringUtils.isBlank(status)) {
            return ResultMap.getFailureResult("必要参数status不能为空！");
        }
        String ms = "操作成功";
        try {
            UserBank userBank = new UserBank();
            userBank.setId(Long.valueOf(id));
            userBank.setStatus(Integer.valueOf(status));
            Boolean rs = userBankService.updateById(userBank);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("UserBankController updateStatus:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }
}
