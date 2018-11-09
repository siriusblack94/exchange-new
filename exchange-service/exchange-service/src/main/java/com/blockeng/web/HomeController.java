package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.entity.WebConfig;
import com.blockeng.framework.http.Response;
import com.blockeng.service.WebConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页信息控制类
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/home")
@Slf4j
@Api(value = "数字货币-首页", description = "数字货币-首页 REST API")
public class HomeController {

    @Autowired
    private WebConfigService webConfigService;

    @GetMapping("/other/banner")
    @ApiOperation(value = "HOME-002 首页banner信息", notes = "首页banner信息", httpMethod = "GET")
    public Object banner() {
        QueryWrapper<WebConfig> e = new QueryWrapper<>();
        e.eq("type", "WEB_BANNER");
        e.eq("status", 1);
        e.orderByAsc("sort");
        List<WebConfig> webs = webConfigService.selectList(e);

        QueryWrapper<WebConfig> e1 = new QueryWrapper<>();
        e1.eq("type", "LINK_BANNER");
        e1.eq("status", 1);
        e1.orderByAsc("sort");
        List<WebConfig> linkWebs = webConfigService.selectList(e1);

        QueryWrapper<WebConfig> e2 = new QueryWrapper<>();
        e2.eq("type", "MOBILE_BANNER");
        e2.eq("status", 1);
        e2.orderByAsc("sort");
        List<WebConfig> mobileWebs = webConfigService.selectList(e2);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("web", webs);
        returnMap.put("link", linkWebs);
        returnMap.put("mobile", mobileWebs);
        return Response.ok(returnMap);
    }

    @GetMapping("/other/documents")
    @ApiOperation(value = "HOME-003 首页底部文案信息", notes = "首页底部文案信息", httpMethod = "GET")
    public Object documents() {
        return Response.ok();
    }
}
