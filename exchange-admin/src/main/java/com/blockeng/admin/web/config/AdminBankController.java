package com.blockeng.admin.web.config;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.UserCashRechargeDTO;
import com.blockeng.admin.entity.AdminBank;
import com.blockeng.admin.entity.Config;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AdminBankService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 人民币充值卡号管理 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/adminBank")
@Api(value = "（场外交易/公司）银行卡管理controller", tags = {"公司银行卡管理"})
@Slf4j
public class AdminBankController {


    @Autowired
    private AdminBankService adminBankService;

    @Log(value = "查询银行卡管理列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('admin_bank_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "银行卡管理列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "realName", value = "开户名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "bankCard", value = "卡号", required = false, dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = AdminBank.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(int current,
                             int size,
                             String realName,
                             String bankCard) {

        EntityWrapper<AdminBank> ew = new EntityWrapper<>();
        Page<AdminBank> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(realName)) {
            ew.like("name", realName);
        }
        if (StringUtils.isNotBlank(bankCard)) {
            ew.like("bank_card", bankCard);
        }
        ew.orderBy("id", false);
        return ResultMap.getSuccessfulResult(adminBankService.selectPage(pager, ew));
    }

    /**
     * 获取银行卡详情
     *
     * @param id 银行卡id
     * @return
     */
    @Log(value = "获取银行卡详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('admin_bank_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取银行卡详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "银行卡id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = AdminBank.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("AdminBankController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        AdminBank adminBank = adminBankService.selectById(id);
        return ResultMap.getSuccessfulResult(adminBank);
    }

    /**
     * 新增银行卡
     *
     * @param adminBank
     * @return
     */
    @Log(value = "新增银行卡", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('admin_bank_create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增银行卡", httpMethod = "POST")
    @ApiImplicitParam(name = "adminBank", value = "新增银行卡对象AdminBank", required = true, dataType = "AdminBank")
    public ResultMap create(@RequestBody AdminBank adminBank) {

        log.info("AdminBankController create:" + adminBank.toString());
        if (null != adminBank && StringUtils.isEmpty(adminBank.getName())) {
            return ResultMap.getFailureResult("开户人姓名不能为空！");
        }
        if (null != adminBank && !StringUtils.isEmpty(adminBank.getName()) && adminBank.getName().length() > 50) {
            return ResultMap.getFailureResult("开户人姓名长度不能超过50！");
        }
        if (null != adminBank && StringUtils.isEmpty(adminBank.getBankName())) {
            return ResultMap.getFailureResult("开户行名称不能为空！");
        }
        if (null != adminBank && !StringUtils.isEmpty(adminBank.getBankName()) && adminBank.getBankName().length() > 255) {
            return ResultMap.getFailureResult("开户行名称长度不能超过255！");
        }
        if (null != adminBank && StringUtils.isEmpty(adminBank.getBankCard())) {
            return ResultMap.getFailureResult("卡号不能为空！");
        }
        if (null != adminBank && !StringUtils.isEmpty(adminBank.getBankCard()) && adminBank.getBankCard().length() > 50) {
            return ResultMap.getFailureResult("卡号长度不能超过50！");
        }

        AdminBank adminBank1 = adminBankService.selectOne(new EntityWrapper<AdminBank>().eq("bank_card", adminBank.getBankCard()));
        if (adminBank1 != null) {
            return ResultMap.getFailureResult("该银行卡号已存在，请换个添加！");
        }

        String ms = "操作成功";
        try {
            Boolean rs = adminBankService.insert(adminBank);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("adminBankController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 更新银行卡
     *
     * @param adminBank
     * @return
     */
    @Log(value = "更新银行卡", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('admin_bank_update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新银行卡", httpMethod = "POST")
    @ApiImplicitParam(name = "adminBank", value = "更新银行卡配置", required = true, dataType = "AdminBank")
    public ResultMap update(@RequestBody AdminBank adminBank) {
        log.info("adminBankController create:" + adminBank.toString());

        if (null != adminBank && adminBank.getId() == null) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (null != adminBank && StringUtils.isEmpty(adminBank.getName())) {
            return ResultMap.getFailureResult("开户人姓名不能为空！");
        }
        if (null != adminBank && !StringUtils.isEmpty(adminBank.getName()) && adminBank.getName().length() > 50) {
            return ResultMap.getFailureResult("开户人姓名长度不能超过50！");
        }
        if (null != adminBank && StringUtils.isEmpty(adminBank.getBankName())) {
            return ResultMap.getFailureResult("开户行名称不能为空！");
        }
        if (null != adminBank && !StringUtils.isEmpty(adminBank.getBankName()) && adminBank.getBankName().length() > 255) {
            return ResultMap.getFailureResult("开户行名称长度不能超过255！");
        }
        if (null != adminBank && StringUtils.isEmpty(adminBank.getBankCard())) {
            return ResultMap.getFailureResult("卡号不能为空！");
        }
        if (null != adminBank && !StringUtils.isEmpty(adminBank.getBankCard()) && adminBank.getBankCard().length() > 50) {
            return ResultMap.getFailureResult("卡号长度不能超过50！");
        }

        AdminBank adminBank1 = adminBankService.selectById(adminBank.getId());
        if (adminBank1 != null && !adminBank1.getBankCard().equals(adminBank.getBankCard())) {
            AdminBank adminBank2 = adminBankService.selectOne(new EntityWrapper<AdminBank>().eq("bank_card", adminBank.getBankCard()));
            if (adminBank2 != null) {
                return ResultMap.getFailureResult("该银行卡号已存在，请换个添加！");
            }
        }

        String ms = "操作成功";
        try {
            Boolean rs = adminBankService.updateById(adminBank);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("adminBankController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 银行卡启用禁用
     *
     * @param bankId 主键
     * @param status 状态
     * @return
     */
    @Log(value = "银行卡启用禁用", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('admin_bank_update')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "银行卡启用禁用", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "银行卡id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "int")
    })
    @RequestMapping({"/adminUpdateBankStatus"})
    @ResponseBody
    public ResultMap adminUpdateBank(Long bankId, Integer status) {

        log.info("adminUpdateBankStatus" + bankId + ",status:" + status);
        if (bankId == null || status == null) {

            return ResultMap.getFailureResult("银行卡id和状态不能为空！");
        }
        AdminBank adminBank = new AdminBank();
        adminBank.setId(bankId);
        adminBank.setStatus(status);
        Boolean rs = adminBankService.updateById(adminBank);
        if (rs) {
            return ResultMap.getSuccessfulResult();
        } else {
            return ResultMap.getFailureResult();
        }
    }
}

