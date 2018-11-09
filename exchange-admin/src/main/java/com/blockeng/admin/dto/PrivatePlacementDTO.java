package com.blockeng.admin.dto;

import com.blockeng.admin.entity.PrivatePlacement;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @Auther: sirius
 * @Date: 2018/8/15 20:14
 * @Description:
 */
@Data
@Accessors(chain = true)
public class PrivatePlacementDTO extends PrivatePlacement {

    @ApiModelProperty(value="国际码",name ="countryCode" ,required = true)
    private String countryCode;

    @ApiModelProperty(value="手机号",name ="mobile" ,required = true)
    private String mobile;

    @ApiModelProperty(value="真实姓名",name ="realName" ,required = true)
    private String realName;

    @ApiModelProperty(value="身份证号",name ="idCard" ,required = true)
    private String idCard;
}
