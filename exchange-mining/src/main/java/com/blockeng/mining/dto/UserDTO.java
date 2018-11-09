package com.blockeng.mining.dto;

import com.blockeng.mining.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "用户", description = "用户")
public class UserDTO extends User {

    private long total;
}
