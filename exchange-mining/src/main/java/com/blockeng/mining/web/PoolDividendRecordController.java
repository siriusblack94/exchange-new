package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.dto.PoolUserHoldCoinDTO;
import com.blockeng.mining.entity.MinePool;
import com.blockeng.mining.entity.PoolDividendAccount;
import com.blockeng.mining.entity.PoolDividendRecord;
import com.blockeng.mining.service.MinePoolService;
import com.blockeng.mining.service.PoolDividendRecordService;
import com.blockeng.mining.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 矿池奖励
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/pool/record")
@Slf4j
@Api(value = "矿池奖励", description = "矿池奖励")
public class PoolDividendRecordController {

    @Autowired
    private PoolDividendRecordService poolDividendRecordService;

    @Autowired
    private MinePoolService minePoolService;

    /**
     * 当前矿池成员持有币
     *
     * @param userDetails
     * @return
     */
    @GetMapping
    @ApiOperation(value = "当前矿池成员持有币", notes = "当前矿池成员持有币", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object poolHoldRecord(
            @ApiIgnore Page<PoolDividendAccount> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        MinePool poolUser = minePoolService.getPoolUser(userDetails.getId());
        if (null == poolUser) {
            return Response.err(1000011, "当前用户不是矿主");
        }

        List<PoolUserHoldCoinDTO> poolUserHold = poolDividendRecordService.getPoolUserHold(poolUser.getCreateUser(),
                poolDividendRecordService.getPoolAllUser(poolUser.getCreateUser()));
        if(poolUserHold==null||poolUserHold.size()==0) return Response.err(1,"当前用户创建的矿池没有成员");
        IPage<PoolUserHoldCoinDTO> iPage= new Page<>();
        Integer total =poolUserHold.size() ;
        List<PoolUserHoldCoinDTO> pager = getPager((int) page.getSize(), (int) page.getCurrent(), poolUserHold);
        iPage.setRecords(pager)
                .setTotal((long)total)
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        return Response.ok(iPage);
    }

    /**
     *
     * @param pageSize  当前页面大小
     * @param pageIndex  当前页码
     * @param list  需要分页的集合
     * @return
     */
    private List  getPager(int pageSize,int pageIndex,List list){
        //使用list 中的sublist方法分页
        List dataList;
        // 每页显示多少条记录

        int currentPage; //当前第几页数据

        int totalRecord = list.size(); // 一共多少条记录

        int totalPage = totalRecord % pageSize; // 一共多少页
        if (totalPage > 0) {
            totalPage = totalRecord / pageSize + 1;
        } else {
            totalPage = totalRecord / pageSize;
        }

        System.out.println("总页数:" + totalPage);

        // 当前第几页数据
        currentPage = totalPage < pageIndex ? totalPage : pageIndex;

        // 起始索引
        int fromIndex = pageSize * (currentPage - 1);

        // 结束索引
        int toIndex = pageSize * currentPage > totalRecord ? totalRecord : pageSize * currentPage;

        dataList = list.subList(fromIndex, toIndex);

        return dataList;
    }


    /**
     * 当前矿池每月分红详情
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/detail/{unLockDate}")
    @ApiOperation(value = "当前矿池每月分红详情", notes = "当前矿池每月分红详情", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object poolHoldRecordDetail(
            @ApiIgnore Page<PoolDividendRecord> page,
            @PathVariable("unLockDate") String unLockDate,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!TimeUtils.isValidDate(unLockDate)) {
            return Response.err(1000010, "请求参数错误");
        }
        String startTime = TimeUtils.getPriMonethFirst(unLockDate);
        String endTime = TimeUtils.getPriMonethLast(unLockDate);
        return Response.ok(poolDividendRecordService.poolRecordListDetail(page, startTime, endTime, userDetails.getId()));
    }


    /**
     * 查询本月可以解冻金额
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/unlock/month")
    @ApiOperation(value = "查询本月可以解冻金额", notes = "查询本月可以解冻金额", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendAccountThisWeek(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        return Response.ok(poolDividendRecordService.dividendAccountThisMonth(userDetails.getId()));
    }

    @GetMapping("/getRecordList")
    @ApiOperation(value = "查询矿池奖励记录列表", notes = "查询矿池奖励记录列表", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object dividendRecordList(
            @ApiIgnore Page<PoolDividendRecord> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<PoolDividendRecord> qw = new QueryWrapper<>();
        qw.eq("user_id", userDetails.getId());
        qw.eq("mark","success");
        qw.orderByAsc("reward_date");
        return Response.ok(poolDividendRecordService.selectPage(page, qw));
    }

}
