package com.blockeng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 最新成交记录
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午3:48
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "turnover_order")
public class TradeDealOrderDTO {

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal volume;

    /**
     * 成交类型
     */
    @Field("trade_type")
    private int type;

    /**
     * 成交时间
     */
    @Field("created")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date time;
}