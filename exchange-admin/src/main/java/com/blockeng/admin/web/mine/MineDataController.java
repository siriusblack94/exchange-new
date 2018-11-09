package com.blockeng.admin.web.mine;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.entity.MineData;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.MineDataService;
import com.blockeng.framework.http.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午4:14
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/mine/data")
@Api(value = "矿池数据", description = "矿池数据", tags = "矿池数据")
public class MineDataController {

    @Autowired
    private MineDataService mineDataService;


    /**
     * 创建挖矿信息
     *
     * @return
     */
    @Log(value = "创建挖矿信息", type = SysLogTypeEnum.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('mine_data_create')")
    @ApiOperation(value = "创建挖矿信息", httpMethod = "POST", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "MineData", value = "新增挖矿信息", required = true, dataType = "MineData", paramType = "body")
    public Response create(@RequestBody @Valid MineData mineData) {
        mineDataService.insert(mineData);
        return Response.ok();
    }

    /**
     * 查新当日挖矿数据
     *
     * @param current 当前页码
     * @param size    每页显示数据条数
     * @return
     */
    @Log(value = "查询挖矿信息", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @PreAuthorize("hasAuthority('mine_data_query')")
    @ApiOperation(value = "按条件分页查询挖矿信息", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示数据条数", dataType = "int", paramType = "query")
    })
    public Response mineDataList(@RequestParam(value = "current", defaultValue = "1") int current,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<MineData> page = new Page<>(current, size);
        EntityWrapper<MineData> wrapper = new EntityWrapper<>();
        return Response.ok(mineDataService.selectPage(page, wrapper));
    }


    /**
     * 修改挖矿信息
     *
     * @param mineData 修改挖矿信息
     * @return
     */
    @Log(value = "修改挖矿数据", type = SysLogTypeEnum.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('mine_data_update')")
    @ApiOperation(value = "修改挖矿数据", httpMethod = "PUT", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "mineDataDTO", value = "修改挖矿信息", required = true, dataType = "MineData", paramType = "body")
    public Response audit(@RequestBody MineData mineData) {
        if (mineData.getId() <= 0) {
            return Response.err(50033, "修改挖矿信息id不能为空");
        }
        MineData minePool = mineDataService.selectById(mineData.getId());
        if (minePool != null) {
            mineDataService.updateById(minePool);
        } else {
            return Response.err(50033, "修改挖矿信息失败,未找到当前挖矿信息");
        }
        return Response.ok();
    }
}
