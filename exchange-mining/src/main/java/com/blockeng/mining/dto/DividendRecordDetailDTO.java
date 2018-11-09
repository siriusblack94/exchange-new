package com.blockeng.mining.dto;

import com.blockeng.mining.entity.DividendRecordDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: sirius
 * @Date: 2018/11/2 10:29
 * @Description:
 */
@Data
@Accessors(chain = true)
public class DividendRecordDetailDTO extends DividendRecordDetail {

    @ApiModelProperty(value="手机号",name ="mobile" ,required = true)
    private String mobile;

    @ApiModelProperty(value="真实姓名",name ="realName" ,required = true)
    private String realName;

    @ApiModelProperty(value="用户名",name ="userName" ,required = true)
    private String username;

    @ApiModelProperty(value="邮箱",name ="email" ,required = true)
    private String email;


}
