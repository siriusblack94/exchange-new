package com.blockeng.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午3:42
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepthsVO implements Serializable {

    /**
     * 委托买单
     */
    private List<ArrayList> bids = new ArrayList<>();

    /**
     * 委托卖单
     */
    private List<ArrayList> asks = new ArrayList<>();

}
