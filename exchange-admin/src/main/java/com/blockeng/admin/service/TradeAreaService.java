package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.TradeArea;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.framework.enums.TradeAreaType;

import java.util.List;

/**
 * <p>
 * 交易区 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface TradeAreaService extends IService<TradeArea> {

    /**
     * 分页查询
     *
     * @param page   分页参数
     * @param type   交易区域类型
     * @param name   交易区域名称
     * @param status 状态
     */
    Page<TradeArea> queryPage(Page<TradeArea> page, TradeAreaType type, String name, Integer status);

    /**
     * 按类型查询交易区域
     *
     * @param type 交易区域类型
     * @return
     */
    List<TradeArea> queryByType(TradeAreaType type, String status);

    /**
     * 批量删除交易区域
     *
     * @param idList 交易区域ID集合
     */
    void batchDelete(List<String> idList);
}
