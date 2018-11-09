package com.blockeng.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.BuckleAccountCountDTO;
import com.blockeng.admin.dto.CoinBuckleDTO;
import com.blockeng.admin.entity.CoinBuckle;
import com.blockeng.admin.entity.SysUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CoinBuckleService extends IService<CoinBuckle> {

    List<CoinBuckleDTO> selectListPage(Page<CoinBuckleDTO> page, EntityWrapper<CoinBuckleDTO> ew);

    boolean audit(AuditDTO auditDTO, SysUser sysUser);

    boolean addCoinBuckle(CoinBuckle coinBuckle);

    BigDecimal selectSumTotal( EntityWrapper<BuckleAccountCountDTO> ew);

    BigDecimal selectSubTotal( EntityWrapper<BuckleAccountCountDTO> ew);

    List<BuckleAccountCountDTO> selectBuckleAccountCounts(Integer valueOf, Integer valueOf1, EntityWrapper<BuckleAccountCountDTO> ew);

    int selectListPageCount(Page<CoinBuckleDTO> page, EntityWrapper<CoinBuckleDTO> ew);

    int selectBuckleAccountCountsTotal(EntityWrapper<BuckleAccountCountDTO> ew);

    /**
     * 参数1.用户ID
     *    2.类型[1补|2扣]
     * */
    List<Map<String,Object>> selectCoinBuckleGroupCoin(String userId,Integer type);

    /**
     * 查询用户补扣冻结金额
     * */
    List<Map<String,Object>> selectBuckleFreezeByCoin(String userId);

}
