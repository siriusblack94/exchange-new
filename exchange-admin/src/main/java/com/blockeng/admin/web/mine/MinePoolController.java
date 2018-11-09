package com.blockeng.admin.web.mine;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.dto.AuditMinePoolDTO;
import com.blockeng.admin.dto.CreateMinePoolDTO;
import com.blockeng.admin.dto.MinePoolDTO;
import com.blockeng.admin.entity.MinePool;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.http.Response;
import com.blockeng.admin.service.MinePoolService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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
     * @param result
     * @return
     */
    @Log(value = "创建矿池", type = SysLogTypeEnum.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('mine_pool_create')")
    @ApiOperation(value = "创建矿池", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    public Response create(@RequestBody @Valid CreateMinePoolDTO createMinePoolDTO, BindingResult result) {
        Long userId = createMinePoolDTO.getCreateUser();
        EntityWrapper<MinePool> wrapper = new EntityWrapper<>();
        wrapper.eq("create_user", userId);
        MinePool minePool = minePoolService.selectOne(wrapper);
        if (minePool != null) {
            return Response.err(50030, "此用户已经创建了一个矿池");
        }
        minePool = new MinePool();
        minePool.setName(createMinePoolDTO.getName())
                .setDescription(createMinePoolDTO.getDescription())
                .setCreateUser(userId)
                .setStatus(BaseStatus.INVALID.getCode());
        minePoolService.insert(minePool);
        return Response.ok();
    }

    /**
     * 查询矿池
     *
     * @param name    矿池名称
     * @param userId  用户ID
     * @param status  用户名
     * @param current 当前页码
     * @param size    每页显示数据条数
     * @return
     */
    @Log(value = "查询矿池", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @PreAuthorize("hasAuthority('mine_pool_query')")
    @ApiOperation(value = "按条件分页查询矿池", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示数据条数", dataType = "int", paramType = "query")
    })
    public Response minePoolList(@RequestParam(value = "name", defaultValue = "") String name,
                                 @RequestParam(value = "username", defaultValue = "") String username,
                                 @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                 @RequestParam(value = "userId", defaultValue = "") String userId,
                                 @RequestParam(value = "status", defaultValue = "") String status,
                                 @RequestParam(value = "id", defaultValue = "") String id,
                                 @RequestParam(value = "current", defaultValue = "1") int current,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<MinePoolDTO> page = new Page<>(current, size);
        EntityWrapper<MinePoolDTO> wrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("p.name", name);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("p.create_user", userId);
        }
        if (!StringUtils.isEmpty(id)) {
            wrapper.eq("p.id", id);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("p.status", Integer.parseInt(status));
        }
        if (!StringUtils.isEmpty(username)) {
            wrapper.eq("u.username", username);
        }
        if (!StringUtils.isEmpty(mobile)) {
            wrapper.eq("u.mobile", mobile);
        }
        return Response.ok(minePoolService.getPoolListPage(page, wrapper));
    }

    /**
     * 审核矿池
     *
     * @param auditMinePoolDTO 审核请求参数
     * @return
     */
    @Log(value = "审核矿池", type = SysLogTypeEnum.AUDIT)
    @PutMapping
    @PreAuthorize("hasAuthority('mine_pool_audit')")
    @ApiOperation(value = "审核矿池", httpMethod = "PUT", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "auditMinePoolDTO", value = "审核请求参数", required = true, dataType = "AuditMinePoolDTO", paramType = "body")
    public Response audit(@RequestBody AuditMinePoolDTO auditMinePoolDTO) {
        MinePool minePool = minePoolService.selectById(auditMinePoolDTO.getId());
        if (minePool != null) {
            minePool.setStatus(auditMinePoolDTO.getStatus())
                    .setLastUpdateTime(new Date())
                    .setRemark(auditMinePoolDTO.getRemark());
            minePoolService.updateById(minePool);
        }
        return Response.ok();
    }
}
