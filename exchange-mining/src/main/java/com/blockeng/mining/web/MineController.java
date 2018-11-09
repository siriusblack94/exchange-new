package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mining.dto.MineDecordDTO;
import com.blockeng.mining.entity.Mine;
import com.blockeng.mining.service.MineHelpService;
import com.blockeng.mining.service.MineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 首页信息控制类
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/mine")
@Slf4j
@Api(value = "交易挖矿", description = "交易挖矿")
public class MineController {

    @Autowired
    private MineService mineService;


    @Autowired
    private MineHelpService mineHelpService;

    /**
     * 查询挖矿信息
     *
     * @param page
     * @param userDetails
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查询用户挖矿列表", notes = "查询用户挖矿列表", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object mineInfo(
            @ApiIgnore Page<Mine> page,
            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<Mine> ew = new QueryWrapper<>();
        ew.select("real_mining,time_mining");
        ew.eq("user_id", userDetails.getId());
        IPage<Mine> mineIPage = mineService.selectPage(page, ew);
        List<Mine> records = mineIPage.getRecords();
        Page<MineDecordDTO> mineDecordDTOPage = new Page<MineDecordDTO>().
                setSize(mineIPage.getSize()).
                setCurrent(mineIPage.getCurrent()).
                setTotal(mineIPage.getTotal());
        if (!CollectionUtils.isEmpty(records)) {//
            mineDecordDTOPage.setRecords(new ArrayList<>());
            TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
            for (Mine mine : records) {
                mineDecordDTOPage.getRecords().add(
                        new MineDecordDTO().
                                setMiningAccount(mine.getRealMining()).
                                setRewardDate(mine.getTimeMining()).
                                setMineCny(mineCurrentMarket.getCnyPrice().multiply(mine.getRealMining())).
                                setMineUsdt(mineCurrentMarket.getPrice().multiply(mine.getRealMining())));
            }
        }
        return Response.ok(mineDecordDTOPage);
    }

    /**
     * 查询挖汇总信息
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/total")
    @ApiOperation(value = "查询挖汇总信息", notes = "查询挖汇总信息", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object mineTotal(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (null == userDetails || userDetails.getId() <= 0) {
            return Response.err(1000010, "请求参数错误");
        }
        return Response.ok(mineService.mineTotal(userDetails.getId()));
    }


    /**
     * 查询挖矿币种信息
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/mine/coin/info")
    @ApiOperation(value = "查询挖矿币种信息", notes = "查询挖矿币种信息", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object mineCoinInfo(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (null == userDetails || userDetails.getId() <= 0) {
            return Response.err(1000010, "请求参数错误");
        }
        return Response.ok(mineService.mineCoinInfo(userDetails.getId()));
    }


    /**
     * 查询挖汇总信息
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/is/mine")
    @ApiOperation(value = "查询挖汇总信息", notes = "查询挖汇总信息", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    @PreAuthorize("isAuthenticated()")
    public Object isMine(@ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (null == userDetails || userDetails.getId() <= 0) {
            return Response.err(1000010, "请求参数错误");
        }
        return Response.ok(mineService.mineCoinInfo(userDetails.getId()));
    }
}
