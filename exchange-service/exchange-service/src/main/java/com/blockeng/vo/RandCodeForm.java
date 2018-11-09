package com.blockeng.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RandCodeForm implements Serializable {

    /**
     * 国际电话区号
     */
    private String countryCode;

    @NotEmpty(message = "templateCode不能为空")
    private String templateCode;

    private String phone;

    private String code;

    private String email;
}