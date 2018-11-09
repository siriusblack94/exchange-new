package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.MinePoolStatus;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.dto.CreateMinePoolDTO;
import com.blockeng.mining.entity.MinePool;
import com.blockeng.mining.service.MinePoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午4:14
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/mine/pool")
@Api(value = "矿池", description = "矿池", tags = "矿池")
public class MinePoolController {

    @Autowired
    private MinePoolService minePoolService;

    /**
     * 创建矿池
     *
     * @param createMinePoolDTO
     * @param userDetails
     * @param result
     * @return
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "创建矿池", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Response create(@RequestBody @Valid CreateMinePoolDTO createMinePoolDTO,
                           @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                           BindingResult result) {
        Long userId = userDetails.getId();
        QueryWrapper<MinePool> wrapper = new QueryWrapper<>();
        ArrayList<Object> status = new ArrayList<>(2);
        status.add(MinePoolStatus.PENDING.getCode());
        status.add(MinePoolStatus.EFFECTIVE.getCode());
        wrapper.eq("create_user", userId).in("status", status);
        MinePool minePool = minePoolService.selectOne(wrapper);
        if (minePool != null) {
            // 您已经创建了一个矿池
            return Response.err(50030,"您已经创建了一个矿池");

        }
        minePool = new MinePool();
        minePool.setName(createMinePoolDTO.getName())
                .setDescription(createMinePoolDTO.getDescription())
                .setCreateUser(userDetails.getId())
                .setStatus(BaseStatus.INVALID.getCode());
        minePoolService.insert(minePool);
        return Response.ok(minePool);
    }

    /**
     * 查询矿池
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "查看矿池", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    public Response getByUser(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<MinePool> qw = new QueryWrapper<>();
        qw.eq("create_user", userDetails.getId());
        qw.orderByDesc("last_update_time");
        qw.last("limit 1");
        return Response.ok(minePoolService.selectList(qw));
    }
}
