package com.blockeng.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkIssueForm {

    @NotNull(message = "工单内容不能为空")
    @ApiModelProperty(value = "工单内容", name = "question", example = "1", required = true)
    private String question;
}
