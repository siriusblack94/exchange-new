package com.blockeng.web.vo;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserForm {

    @JSONField(serialize = false)
    @NotEmpty(message = "图片不能为空")
    private String imageUrl;


    /**
     * 1-身份证正面照；2-身份证反面照；3-手持身份证照片
     */
    @JSONField(serialize = false)
    @NotEmpty(message = "图片类型不能为空")
    private int serialno;
}
