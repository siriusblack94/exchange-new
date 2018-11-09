package com.blockeng.admin.dto;

import com.blockeng.admin.entity.MinePool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: sirius
 * @Date: 2018/10/17 16:24
 * @Description:
 */
@Data
@Accessors(chain = true)
public class MinePoolDTO extends MinePool {
    @ApiModelProperty(value="手机号",name ="mobile" ,required = true)
    private String mobile;

    @ApiModelProperty(value="真实姓名",name ="realName" ,required = true)
    private String realName;

    @ApiModelProperty(value="用户名",name ="userName" ,required = true)
    private String username;
}
