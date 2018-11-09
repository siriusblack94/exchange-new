package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.TradeTopVolumeDTO;
import com.blockeng.admin.dto.TurnOverOrderTheCountDTO;
import com.blockeng.admin.dto.TurnOverOrderTheTotalCountDTOPage;
import com.blockeng.admin.dto.TurnoverOrderCountDTO;
import com.blockeng.admin.entity.TurnoverOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 成交订单 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface TurnoverOrderService extends IService<TurnoverOrder> {

    /**
     * 分页查询
     *
     * @param var1
     * @param var2
     * @return
     */
    List<TurnoverOrder> selectListPage(int current, int size, Wrapper<TurnoverOrder> var2, Wrapper<TurnoverOrder> var3);

    /**
     * 交易量排行
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<TradeTopVolumeDTO> selectTradeTopVolumePage(Page<TradeTopVolumeDTO> page, Wrapper<TradeTopVolumeDTO> wrapper);

    /**
     * 交易币种 ，交易市场，交易量，交易笔数，统计时间
     *
     * @param page
     * @param paramMap
     * @return
     */
    Page<TurnoverOrderCountDTO> selectCountMain(Page<TurnoverOrderCountDTO> page, Map<String, Object> paramMap);

    /**
     * #卖方 最多交易用户，该用户交易量，
     *
     * @param coins
     * @return
     */
    List<TurnoverOrderCountDTO> selectSellUserCount(String[] coins);


    /**
     * #买方 最多交易用户，该用户交易量，
     *
     * @param coins
     * @return
     */
    List<TurnoverOrderCountDTO> selectBuyUserCount(String[] coins);

    List<TurnoverOrder> selectListPageByUser(int current, int size, Wrapper<TurnoverOrder> ew, Wrapper<TurnoverOrder> ow);

    int selectListPageByUserCount(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow);

    int selectListPageCount(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow);

    List<TurnoverOrder> selectListPageByOrder(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow);

    int selectListPageByOrderCount(int current, int size, EntityWrapper<TurnoverOrder> ew, EntityWrapper<TurnoverOrder> ow);

    List<TurnOverOrderTheCountDTO> selectTheCountDTO(int c, int s, EntityWrapper<TurnOverOrderTheCountDTO> ew);

    int selectTheCountDTOCount(EntityWrapper<TurnOverOrderTheCountDTO> ew);

    TurnOverOrderTheTotalCountDTOPage selectTheTotalCountDTO(EntityWrapper<TurnOverOrderTheCountDTO> ew);

    List<Map<String,Object>> selectBuyAndSellAndFeeGroupMarket(String userId);
}
