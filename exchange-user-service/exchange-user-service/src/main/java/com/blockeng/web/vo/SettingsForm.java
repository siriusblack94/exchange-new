package com.blockeng.web.vo;

import com.blockeng.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsForm implements java.io.Serializable {

    /**
     * 用户头像
     */
    private String headimgurl;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Sex sex;
}