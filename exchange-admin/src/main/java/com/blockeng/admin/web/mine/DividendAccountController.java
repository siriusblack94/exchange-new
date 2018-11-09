package com.blockeng.admin.web.mine;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.DividendAccountDTO;
import com.blockeng.admin.dto.DividendRecordDetailDTO;
import com.blockeng.admin.dto.DividendReleaseRecordDTO;
import com.blockeng.admin.dto.PrivatePlacementDTO;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.DividendAccountService;
import com.blockeng.admin.service.DividendRecordService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 邀请奖励
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/dividend/account")
@Slf4j
@Api(value = "邀请奖励", description = "邀请奖励")
public class DividendAccountController {

    @Autowired
    private DividendAccountService dividendAccountService;

    @Autowired
    private DividendRecordService dividendRecordService;

    /**
     * 邀请奖励列表
     */
    @Log(value = "邀请奖励列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('divide_account_query')")
    @GetMapping("/getList")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "邀请奖励列表", httpMethod = "GET")
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
    public Object dividendAccountList(@RequestParam(value = "current", defaultValue = "1") int current,
                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                            String userId,
                                            String username,
                                            String mobile) {
        EntityWrapper<DividendAccountDTO> ew = new EntityWrapper<>();
        Page<DividendAccountDTO> pager = new Page<>(current, size);

        if(StringUtils.isNotBlank(userId)){
            ew.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(username)) {
            ew.like("username", username);
        }
        if (StringUtils.isNotBlank(mobile)) {
            ew.like("mobile", mobile);
        }
      return   ResultMap.getSuccessfulResult(dividendAccountService.selectListPage(pager,ew));
    }
    /**
     * 邀请奖励明细
     */
    @Log(value = "邀请奖励明细", type = SysLogTypeEnum.SELECT)
    @GetMapping("/getRecordDetail")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "邀请奖励明细", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户id",required = true, dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public Object dividendRecordDetailList(@RequestParam(value = "current", defaultValue = "1") int current,
                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                     @RequestParam(value = "userId",required = true)String userId
                                     ) {
        EntityWrapper<DividendRecordDetailDTO> ew = new EntityWrapper<>();
        ew.eq("user_id",userId);
        Page<DividendRecordDetailDTO> pager = new Page<>(current, size);
        return   ResultMap.getSuccessfulResult(dividendRecordService.selectDetailListPage(pager,ew));
    }


    /**
     * 邀请奖励释放明细
     */
    @Log(value = "邀请奖励释放明细", type = SysLogTypeEnum.SELECT)
    @GetMapping("/getReleaseDetail")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "邀请奖励释放明细", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户id",required = true, dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public Object dividendReleaseDetailList(@RequestParam(value = "current", defaultValue = "1") int current,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "userId",required = true)String userId
    ) {
        EntityWrapper<DividendReleaseRecordDTO> ew = new EntityWrapper<>();
        ew.eq("user_id",userId);
        Page<DividendReleaseRecordDTO> pager = new Page<>(current, size);
        return   ResultMap.getSuccessfulResult(dividendRecordService.selectReleaseListPage(pager,ew));
    }
}
