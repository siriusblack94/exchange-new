package com.blockeng.admin.dto;

import com.blockeng.admin.entity.DividendAccount;
import com.blockeng.admin.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: sirius
 * @Date: 2018/9/26 17:12
 * @Description:
 */
@Data
@Accessors(chain = true)
public class DividendAccountDTO extends DividendAccount {
    @ApiModelProperty(value="手机号",name ="mobile" ,required = true)
    private String mobile;

    @ApiModelProperty(value="用户名",name ="username" ,required = true)
    private String username;

    @ApiModelProperty(value="身份证号",name ="idCard" ,required = true)
    private String idCard;
}
