package com.blockeng.admin.web.ucenter;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.MakeUpRechargeDTO;
import com.blockeng.admin.dto.UserWalletDTO;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.UserWalletService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户提币表 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-19
 */
@RestController
@RequestMapping("/userWallet")
@Api(value = "用户提币controller", tags = {"用户提币"})
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @Log(value = "查询用户的提币地址", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_wallet_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "query")
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户的提币地址", httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET, value = "selectPage")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserWalletDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap selectUserWalletList(int size, int current, Long userId) {
        if (size == 0 || current == 0) {
            size = 10;
            current = 1;
        }
        Page<UserWalletDTO> page = new Page<>(current, size);
        if (userId == null) {
            return ResultMap.getFailureResult("用户id不能为空！");
        }
        return ResultMap.getSuccessfulResult(userWalletService.selectUserWalletList(page, userId));
    }


    @Log(value = "新增", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/updateRecharge", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "补单,当充值不到的时候补单", httpMethod = "POST")
    public ResultMap updateRecharge(@RequestBody MakeUpRechargeDTO makeUpRecharge) {
        if (null == makeUpRecharge || StringUtils.isEmpty(makeUpRecharge.getAddress()) || StringUtils.isEmpty(makeUpRecharge.getTxId())) {
            return ResultMap.getFailureResult("参数错误");
        }
        return userWalletService.updateRecharge(makeUpRecharge);
    }
}
