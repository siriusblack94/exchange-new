package com.blockeng.admin.web.operated;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.WebConfig;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.WebConfigService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 网站配置信息 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@Slf4j
@Api(value = "网站配置信息controller", tags = {"资源配置"})

@RestController
@RequestMapping("/webConfig")
public class WebConfigController {

    @Autowired
    private WebConfigService webConfigService;

    /**
     * 网站配置配置信息列表
     *
     * @param type 类型
     * @param name 名称
     * @return
     */
    @Log(value = "网站配置信息列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('web_config_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "网站配置信息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = WebConfig.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
            int current,
            int size,
            String type,
            String name) {
        EntityWrapper<WebConfig> ew = new EntityWrapper<>();
        Page<WebConfig> pager = new Page<WebConfig>(current, size);
        if (StringUtils.isNotBlank(type)) {
            ew.like("type", type);
        }
        if (StringUtils.isNotBlank(name)) {
            ew.like("name", name);
        }
        ew.orderBy("id", false);
        return ResultMap.getSuccessfulResult(webConfigService.selectPage(pager, ew));
    }

    /**
     * 删除网站配置
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "删除网站配置信息", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('web_config_delete')")
    @PostMapping
    @RequestMapping({"/delete"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除网站配置信息", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "网站配置信息id数组", required = true)
    public ResultMap delete(@RequestBody String[] id) {
        log.info("WebConfigController delete id:" + id);
        if (null == id || id.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        Boolean result = webConfigService.deleteBatchIds(Arrays.asList(id));
        String msg = "操作成功";
        if (!result) {
            msg = "操作失败";
        }
        return ResultMap.getSuccessfulResult(msg);
    }

    /**
     * 获取网站配置详情
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "获取网站配置详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('web_config_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取网站配置详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "网站配置id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = WebConfig.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("WebConfigController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        WebConfig config = webConfigService.selectById(id);
        return ResultMap.getSuccessfulResult(config);
    }

    /**
     * 新增网站配置
     *
     * @param config
     * @return
     */
    @Log(value = "新增网站配置", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('web_config_create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增网站配置", httpMethod = "POST")
    @ApiImplicitParam(name = "config", value = "网站配置对象", required = true, dataType = "WebConfig")
    public ResultMap create(@RequestBody WebConfig config) {

        log.info("WebConfigController create:" + config.toString());
        if (null != config && StringUtils.isEmpty(config.getType())) {
            return ResultMap.getFailureResult("分组类型不能为空！");
        }
        if (null != config && !StringUtils.isEmpty(config.getType()) && config.getType().length() > 50) {
            return ResultMap.getFailureResult("分组类型长度不能超过50！");
        }

        if (null != config && !StringUtils.isEmpty(config.getName()) && config.getName().length() > 100) {
            return ResultMap.getFailureResult("名称不能超过100！");
        }
        if (null != config && StringUtils.isEmpty(config.getValue())) {
            return ResultMap.getFailureResult("配置值不能为空！");
        }
        if (null != config && config.getSort() != null) {
            String regEx = "[0-9]{0,4}";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(String.valueOf(config.getSort()));
            boolean rs = matcher.matches();
            if (!rs) {
                return ResultMap.getFailureResult("此值只能为长度不超过5的正数");
            }
        }
        EntityWrapper<WebConfig> ew = new EntityWrapper<>();
        ew.eq("name", config.getName());
        WebConfig webConfig1 = webConfigService.selectOne(ew);
        if (webConfig1 != null) {
            return ResultMap.getFailureResult("该标题已存在！请换个标题");
        }
        String ms = "操作成功";
        try {
            Boolean rs = webConfigService.insert(config);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("WebConfigController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 更新网站配置
     *
     * @param config
     * @return
     */
    @Log(value = "更新网站配置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('web_config_update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新网站配置", httpMethod = "POST")
    @ApiImplicitParam(name = "config", value = "更新网站配置", required = true, dataType = "Config")
    public ResultMap updateArticle(@RequestBody WebConfig config) {
        log.info("WebConfigController create:" + config.toString());
        if (null != config && config.getId() == null) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (null != config && StringUtils.isEmpty(config.getType())) {
            return ResultMap.getFailureResult("分组类型不能为空！");
        }
        if (null != config && !StringUtils.isEmpty(config.getType()) && config.getType().length() > 50) {
            return ResultMap.getFailureResult("分组类型长度不能超过50！");
        }

        if (null != config && !StringUtils.isEmpty(config.getName()) && config.getName().length() > 100) {
            return ResultMap.getFailureResult("名称不能超过100！");
        }
        if (null != config && StringUtils.isEmpty(config.getValue())) {
            return ResultMap.getFailureResult("配置值不能为空！");
        }
        if (null != config && config.getSort() != null) {
            String regEx = "[0-9]{0,4}";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(String.valueOf(config.getSort()));
            boolean rs = matcher.matches();
            if (!rs) {
                return ResultMap.getFailureResult("此值只能为长度不超过5的正数");
            }
        }
        WebConfig webConfig = webConfigService.selectById(config.getId());
        if (webConfig != null && !webConfig.getName().equals(config.getName())) {
            EntityWrapper<WebConfig> ew = new EntityWrapper<>();
            ew.eq("name", config.getName());
            webConfig = webConfigService.selectOne(ew);
            if (webConfig != null) {
                return ResultMap.getFailureResult("该标题已存在！请换个标题");
            }
        }
        String ms = "操作成功";
        try {
            Boolean rs = webConfigService.updateById(config);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("WebConfigController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }
}
