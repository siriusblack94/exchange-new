package com.blockeng.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuthUser {

    /**
     * 高级认证姓名
     */
    private String name;

    /**
     * 高级认证证件号码
     */
    private String idCard;
    /**
     * 证件类型
     * 1-身份证；
     * 3-护照；
     * 4-台湾居民通行证；
     * 5-港澳居民通行证；
     * 9-其他；
     */
    private Integer idCardType;

    /**
     * 高级认证图片列表
     */
    private List<String> imgUrlList;

}
