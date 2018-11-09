package com.blockeng.admin.web.mine;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.dto.PoolDividendAccountDTO;
import com.blockeng.admin.dto.PoolDividendRecordDTO;
import com.blockeng.admin.entity.MinePool;
import com.blockeng.admin.entity.PoolDividendAccount;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.PoolDividendAccountService;
import com.blockeng.admin.service.PoolDividendRecordService;
import com.blockeng.framework.http.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: sirius
 * @Date: 2018/10/16 17:02
 * @Description:
 */

@RestController
@RequestMapping("/pool/dividend")
@Api(value = "矿池奖励", description = "矿池奖励", tags = "矿池奖励")
public class PoolDividendController {

    @Autowired
    PoolDividendAccountService poolDividendAccountService;

    @Autowired
    PoolDividendRecordService poolDividendRecordService;
    /**
     * 查询矿池奖励
     *
     * @param username    矿池名称
     * @param userId  用户ID
     * @param current 当前页码
     * @param size    每页显示数据条数
     * @return
     */
    @Log(value = "查询矿池奖励", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @PreAuthorize("hasAuthority('mine_pool_dividend_query')")
    @ApiOperation(value = "按条件分页查询矿池奖励", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示数据条数", dataType = "int", paramType = "query")
    })
    public Response getPoolDividendAccountList(@RequestParam(value = "username", defaultValue = "") String username,
                                 @RequestParam(value = "userId", defaultValue = "") String userId,
                                 @RequestParam(value = "current", defaultValue = "1") int current,
                                 @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PoolDividendAccountDTO> page = new Page<>(current, size);
        EntityWrapper<PoolDividendAccountDTO> wrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            wrapper.eq("u.username", username);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("d.user_id", userId);
        }
        if (!StringUtils.isEmpty(mobile)) {
            wrapper.eq("u.mobile", mobile);
        }

        return Response.ok(poolDividendAccountService.getPoolDividendAccountList(page, wrapper));
    }


    /**
     * 查询矿池奖励释放明细
     *
     * @param username    矿池名称
     * @param userId  用户ID
     * @param current 当前页码
     * @param size    每页显示数据条数
     * @return
     */
    @Log(value = "查询矿池奖励释放明细", type = SysLogTypeEnum.SELECT)
    @GetMapping("/getReleaseList")
    @ApiOperation(value = "按条件分页查询矿池奖励释放明细", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示数据条数", dataType = "int", paramType = "query")
    })
    public Response getPoolDividendAccountDetailList(@RequestParam(value = "username", defaultValue = "") String username,
                                         @RequestParam(value = "userId", defaultValue = "") String userId,
                                         @RequestParam(value = "current", defaultValue = "1") int current,
                                         @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PoolDividendAccountDTO> page = new Page<>(current, size);
        EntityWrapper<PoolDividendAccountDTO> wrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            wrapper.eq("u.username", username);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("d.user_id", userId);
        }
        if (!StringUtils.isEmpty(mobile)) {
            wrapper.eq("u.mobile", mobile);
        }

        return Response.ok(poolDividendAccountService.getPoolDividendAccountDetailList(page, wrapper));
    }



    /**
     * 查询矿池奖励记录
     *
     * @param username    矿池名称
     * @param userId  用户ID
     * @param current 当前页码
     * @param size    每页显示数据条数
     * @return
     */
    @Log(value = "查询矿池奖励记录", type = SysLogTypeEnum.SELECT)
    @GetMapping("/getRecordList")

    @ApiOperation(value = "按条件分页查询矿池奖励记录", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示数据条数", dataType = "int", paramType = "query")
    })
    public Response getPoolDividendRecordList(@RequestParam(value = "username", defaultValue = "") String username,
                                                     @RequestParam(value = "userId", defaultValue = "") String userId,
                                                     @RequestParam(value = "current", defaultValue = "1") int current,
                                                     @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PoolDividendRecordDTO> page = new Page<>(current, size);
        EntityWrapper<PoolDividendRecordDTO> wrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            wrapper.eq("u.username", username);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("d.user_id", userId);
        }
        if (!StringUtils.isEmpty(mobile)) {
            wrapper.eq("u.mobile", mobile);
        }

        return Response.ok(poolDividendRecordService.getPoolDividendRecordList(page, wrapper));
    }
}
