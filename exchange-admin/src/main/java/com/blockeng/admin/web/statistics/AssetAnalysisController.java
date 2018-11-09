package com.blockeng.admin.web.statistics;


import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AccountBalanceStatiscDTO;
import com.blockeng.admin.dto.AssetAnalysisDTO;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountBalanceCountService;
import com.blockeng.admin.service.AssetAnalysisService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产分析
 * */

@Slf4j
@Api(value = "资产分析", tags = {"资产分析"})
@RestController
@RequestMapping("/assetAnalysis")
public class AssetAnalysisController {

    @Autowired
    private AssetAnalysisService assetAnalysisService;

    @Log(value = "资产异常分析", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = AssetAnalysisDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })

    public ResultMap getListWithoutMining(
            @RequestParam(value = "userId", defaultValue = "") String userId
    ) {

        AssetAnalysisDTO assetAnalysisDTO=assetAnalysisService.assetAnalysisWithoutMining(userId);

        return ResultMap.getSuccessfulResult(assetAnalysisDTO);
    }

}
