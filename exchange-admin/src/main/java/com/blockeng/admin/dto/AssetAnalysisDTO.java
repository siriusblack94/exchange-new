package com.blockeng.admin.dto;

import com.blockeng.admin.entity.AssetAnalysisWithoutMining;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jakiro
 * @Date: 2018-10-14 20:04
 * @Description: 资产分析接口返回实体
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AssetAnalysisDTO {

    @ApiModelProperty(value = "用户名称", name = "userName", required = true)
    String userName;

    @ApiModelProperty(value = "资产是否异常", name = "isUnusual", required = true)
    boolean isUnusual=false;

    @ApiModelProperty(value = "分析结论", name = "analyticResult", required = true)
    String  analyticResult="";

    @ApiModelProperty(value = "结果map", name = "resultMap", required = true)
    Map<String,AssetAnalysisWithoutMining> resultMap=new HashMap<String, AssetAnalysisWithoutMining>();
}
