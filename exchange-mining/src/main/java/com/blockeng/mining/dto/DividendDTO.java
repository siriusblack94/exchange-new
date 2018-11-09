package com.blockeng.mining.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Auther: sirius
 * @Date: 2018/8/27 19:30
 * @Description:
 */
@Data
@Accessors(chain = true)
public class DividendDTO {
    @TableId("id")
    private Long id;

    @TableField("user_id")
    private Long userId;
    @TableField("invite_amount")
    private BigDecimal inviteAmount;

}
