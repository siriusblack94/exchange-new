package com.blockeng.admin.dto;



import com.blockeng.admin.entity.PoolDividendRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class PoolDividendRecordDTO extends PoolDividendRecord {
    @ApiModelProperty(value="手机号",name ="mobile" ,required = true)
    private String mobile;

    @ApiModelProperty(value="真实姓名",name ="realName" ,required = true)
    private String realName;

    @ApiModelProperty(value="用户名",name ="userName" ,required = true)
    private String username;



}

