package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Config;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.ConfigService;
import com.blockeng.framework.constants.Constant;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * 平台配置信息 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@Slf4j
@Api(value = "平台配置信息 controller", tags = {"系统配置"})
@RestController
@RequestMapping("/config")
public class ConfigController {
    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);
    @Autowired
    private ConfigService configService;

    /**
     * 平台配置信息列表
     *
     * @param code 配置规则代码
     * @param name 配置规则名称
     * @return
     */
    @Log(value = "查询配置信息列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('config_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "配置信息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "配置规则代码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "配置规则名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "配置规则类型,CNY/SYSTEM", required = false, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = Config.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
            int current,
            int size,
            String code,
            String name,
            String type) {
        Page<Config> pager = new Page<Config>(current, size);
        EntityWrapper<Config> ew = new EntityWrapper<>();
        if (StringUtils.isNotBlank(code)) {
            ew.like("code", code);
        }
        if (StringUtils.isNotBlank(name)) {
            ew.like("name", name);
        }
        if (StringUtils.isNotBlank(type) && !type.equalsIgnoreCase("CNY")) {
            ew.notIn("type", "CNY");
        }
        if (StringUtils.isNotBlank(type)) {
            ew.like("type", type);
        }
        ew.orderBy("id", false);
        return ResultMap.getSuccessfulResult(configService.selectPage(pager, ew));
    }

    /**
     * 删除平台配置
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "删除平台配置信息信息", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('config_delete')")
    @PostMapping
    @RequestMapping({"/delete"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除平台配置信息信息", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "平台配置信息id数组", required = true)
    public ResultMap delete(@RequestBody String[] id) {
        logger.info("ConfigController delete id:" + id);
        if (null == id || id.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        Boolean result = configService.deleteBatchIds(Arrays.asList(id));
        String msg = "操作成功";
        if (!result) {
            msg = "操作失败";
            return ResultMap.getFailureResult(msg);
        }
        return ResultMap.getSuccessfulResult(msg);
    }

    /**
     * 获取平台配置详情
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "获取平台配置详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('config_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取平台配置详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "公告id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = Config.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        logger.info("ConfigController delete id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        Config config = configService.selectById(id);
        return ResultMap.getSuccessfulResult(config);
    }

    /**
     * 新增平台配置
     *
     * @param config
     * @return
     */
    @Log(value = "新增平台配置", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('config_create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增平台配置", httpMethod = "POST")
    @ApiImplicitParam(name = "config", value = "平台配置对象", required = true, dataType = "Config")
    public ResultMap create(@RequestBody Config config) {
        log.info("ConfigController create:" + config.toString());
        if (null != config && StringUtils.isEmpty(config.getCode())) {
            return ResultMap.getFailureResult("规则代码不能为空！");
        }
        if (null != config && !StringUtils.isEmpty(config.getCode()) && config.getCode().length() > 50) {
            return ResultMap.getFailureResult("规则代码长度不能超过50！");
        }
        if (null != config && !StringUtils.isEmpty(config.getType()) && config.getType().length() > 64) {
            return ResultMap.getFailureResult("规则代码长度不能超过64！");
        }
        if (null != config && StringUtils.isEmpty(config.getName())) {
            return ResultMap.getFailureResult("配置规则名称不能为空！");
        }
        if (null != config && !StringUtils.isEmpty(config.getName()) && config.getName().length() > 100) {
            return ResultMap.getFailureResult("配置规则名称不能超过100！");
        }
        if (null != config && !StringUtils.isEmpty(config.getDesc()) && config.getDesc().length() > 255) {
            return ResultMap.getFailureResult("配置规则描述不能超过255！");
        }
        if (null != config && !StringUtils.isEmpty(config.getValue()) && config.getValue().length() > 255) {
            return ResultMap.getFailureResult("配置值不能超过255");
        }
        Config config1 = configService.selectOne(new EntityWrapper<Config>().eq("code", config.getCode()));
        if (config1 != null) {
            return ResultMap.getFailureResult("该规则代码已存在，请换个添加！");
        }
        Config config3 = configService.selectOne(new EntityWrapper<Config>().eq("name", config.getName()));
        if (config3 != null) {
            return ResultMap.getFailureResult("该规则名称已存在，请换个添加！");
        }
        String ms = "操作成功";
        try {
            Boolean rs = configService.insert(config);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("ConfigController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 更新平台配置
     *
     * @param config
     * @return
     */
    @Log(value = "更新平台配置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('config_update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新平台配置", httpMethod = "POST")
    @ApiImplicitParam(name = "config", value = "更新平台配置", required = true, dataType = "Config")
    public ResultMap update(@RequestBody Config config) {

        /*
          spring 防止el表达式注入，我们从前台把${abc}变成#{abc}，接到数据后先转换回去 2018.8.31
         */
        if (null != config && null != config.getValue()) ;
        config.setValue(config.getValue().replace("#", "$"));

        log.info("ConfigController create:" + config.toString());
        if (null != config && config.getId() == null) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (null != config && StringUtils.isEmpty(config.getCode())) {
            return ResultMap.getFailureResult("规则代码不能为空！");
        }
        if (null != config && !StringUtils.isEmpty(config.getCode()) && config.getCode().length() > 50) {
            return ResultMap.getFailureResult("规则代码长度不能超过50！");
        }
        if (null != config && !StringUtils.isEmpty(config.getType()) && config.getType().length() > 64) {
            return ResultMap.getFailureResult("规则代码长度不能超过64！");
        }
        if (null != config && StringUtils.isEmpty(config.getName())) {
            return ResultMap.getFailureResult("配置规则名称不能为空！");
        }
        if (null != config && !StringUtils.isEmpty(config.getName()) && config.getName().length() > 100) {
            return ResultMap.getFailureResult("配置规则名称不能超过100！");
        }
        if (null != config && !StringUtils.isEmpty(config.getDesc()) && config.getDesc().length() > 255) {
            return ResultMap.getFailureResult("配置规则描述不能超过255！");
        }
        if (null != config && !StringUtils.isEmpty(config.getValue()) && config.getValue().length() > 255) {
            return ResultMap.getFailureResult("配置值不能超过255");
        }
        Config config1 = configService.selectById(config.getId());
        if (config1 != null && !config1.getCode().equals(config.getCode())) {
            Config config2 = configService.selectOne(new EntityWrapper<Config>().eq("code", config.getCode()));
            if (config2 != null) {
                return ResultMap.getFailureResult("该规则代码已存在，请更换一个！");
            }
        }
        if (config1 != null && !config1.getName().equals(config.getName())) {
            Config config3 = configService.selectOne(new EntityWrapper<Config>().eq("name", config.getName()));
            if (config3 != null) {
                return ResultMap.getFailureResult("该规则名称已存在，请换个添加！");
            }
        }
        String ms = "操作成功";
        try {
            Boolean rs = configService.updateById(config);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);

            }
        } catch (Exception e) {
            log.info("ConfigController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * CNY/USDT汇率兑换列表
     *
     * @param
     * @return
     */
    @Log(value = "CNY/USDT汇率兑换", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('rate_list')")
    @RequestMapping(value = "/rateList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY/USDT汇率兑换", httpMethod = "POST")
    @ApiImplicitParam(name = "config", value = "CNY/USDT汇率兑换", required = true, dataType = "Config")
    public Object USDTRateList(){

        ArrayList<Config> configList = new ArrayList<>();

        configList.add(configService.queryBuyCodeAndType(Constant.CONFIG_TYPE_CNY, Constant.CONFIG_CNY2USDT));
        configList.add(configService.queryBuyCodeAndType(Constant.CONFIG_TYPE_CNY, Constant.CONFIG_USDT2CNY));

        return configList;
    }
}
