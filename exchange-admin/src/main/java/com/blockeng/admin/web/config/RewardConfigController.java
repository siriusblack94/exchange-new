package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.RewardConfig;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinService;
import com.blockeng.admin.service.RewardConfigService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 奖励配置(注册,邀请)
 * </p>
 *
 * @author shadow
 * @since 2018/09/18
 */
@Slf4j
@Api(value = "奖励配置信息 controller", tags = {"奖励配置"})
@RestController
@RequestMapping("/rewardConfig")
public class RewardConfigController {
    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private RewardConfigService rewardConfigService;

    @Autowired
    private CoinService coinService;


/**
 * 新增奖励配置
 *
 * @param rewardConfig
 * @return
 */
    @Log(value = "新增奖励配置", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('reward_config_create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增奖励配置", httpMethod = "POST")
    @ApiImplicitParam(name = "rewardConfig", value = "平台配置对象", required = true, dataType = "RewardConfig")
    public ResultMap create(@RequestBody RewardConfig rewardConfig){
        log.info("RewardConfigController create:" + rewardConfig.toString());

        if ( rewardConfig.getCoinId()==null ){
            return ResultMap.getFailureResult("币种不能为空");
        }
        if (StringUtils.isEmpty(rewardConfig.getType())){
            return ResultMap.getFailureResult("奖励类型不能为空");
        }
        if(rewardConfig.getStatus()==null){
            return ResultMap.getFailureResult("奖励开关不能为空");
        }
        if(rewardConfig.getAmount()==null ){
            return ResultMap.getFailureResult("奖励数量不能为空");
        }
        if (rewardConfig.getStartTime()==null){
            return ResultMap.getFailureResult("奖励开始时间不能为空");
        }
        if (rewardConfig.getEndTime()==null){
            return ResultMap.getFailureResult("奖励结束时间不能为空");
        }
        String ms = "操作成功";

        Coin coin = coinService.selectById(rewardConfig.getCoinId());
        rewardConfig.setCoinName(coin.getName());

        try {
            Boolean rs = rewardConfigService.insert(rewardConfig);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("RewardConfigController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);

    }

    /**
     * 奖励配置列表
     *
     * @param coinId 币种
     * @param type 奖励类型
     * @param status 奖励开关(0 关闭，1 开启)
     * @param startTime 奖励开始时间
     * @param endTime 奖励结束时间
     * @return
     */
    @Log(value = "查询配置信息列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('reward_config_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "奖励配置列表", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = RewardConfig.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
            int current, int size,
            String coinId, String type,
            String status, String startTime, String endTime
            ){

        Page<RewardConfig> pager = new Page<>(current, size);

        EntityWrapper<RewardConfig> ew = new EntityWrapper<>();

        if (StringUtils.isNotEmpty(coinId)){
            ew.eq("coin_id",coinId);
        }
        if (StringUtils.isNotEmpty(type)){
            ew.eq("type",type);
        }
        if (StringUtils.isNotEmpty(status)){
            ew.eq("status",status);
        }
        if (StringUtils.isNotEmpty(startTime)){
            ew.ge("start_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            ew.le("end_time",endTime);
        }
        return ResultMap.getSuccessfulResult(rewardConfigService.selectPage(pager,ew));
    }

    /**
     * 修改奖励配置
     *
     * @param rewardConfig
     * @return
     */
    @Log(value = "修改奖励配置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('reward_config_update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改奖励配置", httpMethod = "POST")
    @ApiImplicitParam(name = "rewardConfig", value = "平台配置对象", required = true, dataType = "RewardConfig")
    public ResultMap update(@RequestBody RewardConfig rewardConfig){
        log.info("RewardConfigController update:" + rewardConfig.toString());

        if ( rewardConfig.getId()==null ){
            return ResultMap.getFailureResult("id不能为空");
        }

        if ( rewardConfig.getCoinId()==null ){
            return ResultMap.getFailureResult("币种不能为空");
        }
        if (StringUtils.isEmpty(rewardConfig.getType())){
            return ResultMap.getFailureResult("奖励类型不能为空");
        }
        if(rewardConfig.getStatus()==null){
            return ResultMap.getFailureResult("奖励开关不能为空");
        }
        if(rewardConfig.getAmount()==null ){
            return ResultMap.getFailureResult("奖励数量不能为空");
        }
        if (rewardConfig.getStartTime()==null){
            return ResultMap.getFailureResult("奖励开始时间不能为空");
        }
        if (rewardConfig.getEndTime()==null){
            return ResultMap.getFailureResult("奖励结束时间不能为空");
        }
        String ms = "操作成功";
        Coin coin = coinService.selectById(rewardConfig.getCoinId());
        rewardConfig.setCoinName(coin.getName());
        try {
            Boolean rs = rewardConfigService.updateById(rewardConfig);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("RewardConfigController update:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 删除奖励配置
     *
     * @param id
     * @return
     */
    @Log(value = "删除奖励配置", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('reward_config_delete')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改奖励配置", httpMethod = "POST")
    @ApiImplicitParam(name = "rewardConfig", value = "平台配置对象", required = true, dataType = "RewardConfig")
    public ResultMap delete(@RequestBody String[] id){
        logger.info("RewardConfigController delete id:" + id);
        if (null == id || id.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }

        Boolean result=rewardConfigService.deleteBatchIds(Arrays.asList(id));

        String msg = "操作成功";
        if (!result) {
            msg = "操作失败";
            return ResultMap.getFailureResult(msg);
        }
        return ResultMap.getSuccessfulResult(msg);
    }

    /**
     * 获取奖励配置详情
     *
     * @param id
     * @return
     */
    @Log(value = "获取奖励配置详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('reward_config_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取奖励配置详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = RewardConfig.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        logger.info("ConfigController delete id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        RewardConfig rewardConfig = rewardConfigService.selectById(id);
        return ResultMap.getSuccessfulResult(rewardConfig);
    }


    @Log(value = "币种类型状态设置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('reward_status_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "奖励启禁(status:1启用 0禁用)")
    @RequestMapping(path = "/setStatus", method = RequestMethod.POST)
    public Object setStatus(@RequestBody RewardConfig config) {

        if (config.getId()==null){

            return ResultMap.getFailureResult("输入id为空");
        }
        RewardConfig rewardConfig = rewardConfigService.selectById(config.getId());

        if (rewardConfig==null){
            return ResultMap.getFailureResult("输入id不存在");
        }
        if (config.getStatus()==null ||
            config.getStatus()<0 ||
            config.getStatus()>1
                ) {
            return ResultMap.getFailureResult("输入的启禁状态参数有误!");
        }

        rewardConfig.setStatus(config.getStatus());

        if(rewardConfigService.updateById(config)){
            return ResultMap.getSuccessfulResult(config.getStatus()==CommonUtils.STATUS_1?"启用成功":"禁用成功");
        }else {

            return ResultMap.getFailureResult(config.getStatus()==CommonUtils.STATUS_1?"启用成功":"禁用成功");
        }

    }
}
