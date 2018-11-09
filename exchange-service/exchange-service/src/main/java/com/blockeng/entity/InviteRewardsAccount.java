package com.blockeng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invite_rewards_account")
public class InviteRewardsAccount {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    private Long coinId;

    /**
     * 总冻结
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 冻结奖励
     */
    private BigDecimal freeze = BigDecimal.ZERO;
    /**
     * 已经解冻量
     */
    private BigDecimal thawed = BigDecimal.ZERO;
    /**
     * 可解冻奖励
     */
    @Field("can_defrost")
    private BigDecimal canDefrost = BigDecimal.ZERO;
}
