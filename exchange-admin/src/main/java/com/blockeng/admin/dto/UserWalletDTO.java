package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.blockeng.admin.entity.UserWallet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Create Time: 2018年05月19日 20:27
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class UserWalletDTO extends UserWallet {

    @ApiModelProperty(value = "用户名称", name = "username", required = false)
    public String username;
    @ApiModelProperty(value = "用户真实名称", name = "realName", required = false)
    public String realName;
    @ApiModelProperty(value = "币种名称（展示取该值）", name = "coinNameTwo", required = false)
    private String coinNameTwo;

}
