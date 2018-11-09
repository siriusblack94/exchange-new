package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.TradeTopVolumeDTO;
import com.blockeng.admin.dto.TurnOverOrderTheCountDTO;
import com.blockeng.admin.dto.TurnOverOrderTheTotalCountDTOPage;
import com.blockeng.admin.dto.TurnoverOrderCountDTO;
import com.blockeng.admin.entity.TurnoverOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 成交订单 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface TurnoverOrderMapper extends BaseMapper<TurnoverOrder> {

    List<TurnoverOrder> selectListPage(Page<TurnoverOrder> page, @Param("turnover1Ew") Wrapper<TurnoverOrder> wrapper, @Param("turnover2Ew") Wrapper<TurnoverOrder> wapper2);

    List<TurnoverOrder> selectListPageByUser(Page<TurnoverOrder> page, @Param("turnover1Ew") Wrapper<TurnoverOrder> wrapper, @Param("turnover2Ew") Wrapper<TurnoverOrder> wrapper2);

    /**
     * 根据日期和用户统计参与交易人数
     *
     * @param countDate
     * @param uidStrs   用户ID字符串('1,2,3')
     * @return
     */
    Integer countTradeByDateAndUidStrs(@Param("countDate") String countDate, @Param("uidStrs") String uidStrs);


    /**
     * 分页查询交易排行
     *
     * @param page
     * @param wrapper
     * @return
     */
    List<TradeTopVolumeDTO> selectTradeTopVolumePage(Page<TradeTopVolumeDTO> page, @Param("ew") Wrapper<TradeTopVolumeDTO> wrapper);

    /**
     * 交易币种 ，交易市场，交易量，交易笔数，统计时间
     *
     * @param page
     * @param paramMap
     * @return
     */
    List<TurnoverOrderCountDTO> selectCountMain(Pagination page, Map<String, Object> paramMap);


    /**
     * #卖方 最多交易用户，该用户交易量，
     *
     * @param coins
     * @return
     */
    List<TurnoverOrderCountDTO> selectSellUserCount(@Param("coins") String[] coins);


    /**
     * #买方 最多交易用户，该用户交易量，
     *
     * @param coins
     * @return
     */
    List<TurnoverOrderCountDTO> selectBuyUserCount(@Param("coins") String[] coins);


    List<TurnoverOrder> selectListPage(@Param("current") int current, @Param("size") int size, @Param("turnover1Ew") Wrapper<TurnoverOrder> wrapper, @Param("turnover2Ew") Wrapper<TurnoverOrder> wrapper2);

    List<TurnoverOrder> selectListPageByUser(@Param("current") int current, @Param("size") int size, @Param("turnover1Ew") Wrapper<TurnoverOrder> wrapper, @Param("turnover2Ew") Wrapper<TurnoverOrder> wrapper2);

    int selectListPageCount(@Param("current") int current, @Param("size") int size, @Param("turnover1Ew") EntityWrapper<TurnoverOrder> ew, @Param("turnover2Ew") EntityWrapper<TurnoverOrder> ow);

    int selectListPageByUserCount(@Param("current") int current, @Param("size") int size, @Param("turnover1Ew") EntityWrapper<TurnoverOrder> ew, @Param("turnover2Ew") EntityWrapper<TurnoverOrder> ow);

    List<TurnoverOrder> selectListPageByOrder(@Param("current") int current, @Param("size") int size, @Param("turnover1Ew") EntityWrapper<TurnoverOrder> ew, @Param("turnover2Ew") EntityWrapper<TurnoverOrder> ow);

    int selectListPageByOrderCount(@Param("current") int current, @Param("size") int size, @Param("turnover1Ew") EntityWrapper<TurnoverOrder> ew, @Param("turnover2Ew") EntityWrapper<TurnoverOrder> ow);

    int selectTheCountDTOCount(@Param("ew") EntityWrapper<TurnOverOrderTheCountDTO> ew);

    List<TurnOverOrderTheCountDTO> selectTheCountDTO(@Param("current") int c, @Param("size") int s, @Param("ew") EntityWrapper<TurnOverOrderTheCountDTO> ew);

    // TurnOverOrderTheTotalCountDTO selectTheTotalCountDTO(@Param("ew") EntityWrapper<TurnOverOrderTheCountDTO> ew);
    TurnOverOrderTheTotalCountDTOPage selectTheTotalCountDTO(@Param("ew") EntityWrapper<TurnOverOrderTheCountDTO> ew);

    List<TradeTopVolumeDTO> selectTradeTopVolumePage(@Param("current") int current, @Param("size") int size, @Param("ew") Wrapper<TradeTopVolumeDTO> wrapper);

    int selectTradeTopVolumePageCount(@Param("ew") Wrapper<TradeTopVolumeDTO> wrapper);

    List<Map<String,Object>> selectBuyAndSellOrder(Map<String,Object> paramMap);

}
