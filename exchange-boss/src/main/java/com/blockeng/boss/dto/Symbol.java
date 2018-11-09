package com.blockeng.boss.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午1:46
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Symbol {

    private Long buyCoinId;

    private Long sellCoinId;
}
