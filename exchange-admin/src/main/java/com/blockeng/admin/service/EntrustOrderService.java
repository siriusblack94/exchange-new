package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.EntrustOrder;
import com.blockeng.admin.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托订单信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface EntrustOrderService extends IService<EntrustOrder> {

    /**
     * 分页查询
     *
     * @param var1
     * @param var2
     * @return
     */
    Page<EntrustOrder> selectListPage(Page<EntrustOrder> var1, Wrapper<EntrustOrder> var2);

    List<EntrustOrder> selectListPageEmpty(int current, int size);


    List<EntrustOrder> selectListPageByOrder(Wrapper<EntrustOrder> wrapper, Wrapper<User> otherEw, int current, int size);


    List<EntrustOrder> selectListPageByUser(Wrapper<User> wrapper, Wrapper<EntrustOrder> otherEw, int current, int size);

    Integer selectListPageCount();


    Integer selectListPageByOrderCount(Wrapper<EntrustOrder> wrapper, Wrapper<User> otherEw);


    Integer selectListPageByUserCount(Wrapper<EntrustOrder> wrapper, Wrapper<User> otherEw);


    Integer selectListPageCountInDays(Wrapper<EntrustOrder> wrapper, int current, int size);

    List<Map<String,Object>> selectExchangeFreezeByCoin(String userId);

    void startCancel(Long valueOf);
}
