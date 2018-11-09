package com.blockeng.admin.service.impl;

import com.blockeng.admin.entity.*;
import com.blockeng.admin.service.*;
import com.blockeng.framework.enums.PrivilegeType;
import com.blockeng.framework.exception.ExchangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 审核权限校验
 * @Author: Chen Long
 * @Date: Created in 2018/5/26 下午12:03
 * @Modified by: Chen Long
 */
@Service
@Slf4j
public class MultiLevelAuditServiceImpl implements MultiLevelAuditService {

    @Autowired
    CashRechargeService cashRechargeService;

    @Autowired
    CashWithdrawalsService cashWithdrawalsService;

    @Autowired
    CoinWithdrawService coinWithdrawService;

    @Autowired
    CoinBuckleService coinBuckleService;

    /**
     * 法币充值审核权限校验
     *
     * @param orderId 充值订单号
     * @param sysUser 当前登录用户
     * @return
     */
    @Override
    public boolean cashRechargePermissionCheck(Long orderId, SysUser sysUser) {
        CashRecharge cashRecharge = cashRechargeService.selectById(orderId);
        if (cashRecharge == null) {
            throw new ExchangeException("订单不存在");
        }
        // 当前审核级数
        int step = cashRecharge.getStep();
        Collection<? extends GrantedAuthority> authorities = sysUser.getAuthorities();
        String privilege = new StringBuffer(PrivilegeType.CASH_RECHARGE_AUDIT.getCode()).append("_").append(step).toString();
        if (authorities.contains(new SimpleGrantedAuthority(privilege))) {
            return true;
        }
        return false;
    }

    /**
     * 法币提现审核权限校验
     *
     * @param orderId 提现申请单号
     * @param sysUser 当前登录用户
     * @return
     */
    @Override
    public boolean cashWithdrawPermissionCheck(Long orderId, SysUser sysUser) {
        CashWithdrawals cashWithdraw = cashWithdrawalsService.selectById(orderId);
        if (cashWithdraw == null) {
            throw new RuntimeException("订单不存在");
        }
        // 当前审核级数
        int step = cashWithdraw.getStep();
        Collection<? extends GrantedAuthority> authorities = sysUser.getAuthorities();
        String privilege = new StringBuffer(PrivilegeType.CASH_WITHDRAW_AUDIT.getCode()).append("_").append(step).toString();
        if (authorities.contains(new SimpleGrantedAuthority(privilege))) {
            return true;
        }
        return false;
    }

    /**
     * 数字货币提现权限校验
     *
     * @param orderId 提币申请单号
     * @param sysUser 当前登录用户
     * @return
     */
    @Override
    public boolean coinWithdrawPermissionCheck(Long orderId, SysUser sysUser) {
        CoinWithdraw coinWithdraw = coinWithdrawService.selectById(orderId);
        if (coinWithdraw == null) {
            throw new RuntimeException("订单不存在");
        }
        // 当前审核级数
        int step = coinWithdraw.getStep();
        Collection<? extends GrantedAuthority> authorities = sysUser.getAuthorities();
        String privilege = new StringBuffer(PrivilegeType.COIN_WITHDRAW_AUDIT.getCode()).append("_").append(step).toString();
        if (authorities.contains(new SimpleGrantedAuthority(privilege))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean coinBucklePermissionCheck(Long orderId, SysUser sysUser) {
        CoinBuckle coinBuckle = coinBuckleService.selectById(orderId);
        if (coinBuckle == null) {
            throw new RuntimeException("订单不存在");
        }
        int step = coinBuckle.getStep();
        Collection<? extends GrantedAuthority> authorities = sysUser.getAuthorities();
        String privilege = new StringBuffer(PrivilegeType.COIN_BUCKLE_AUDIT.getCode()).append("_").append(step).toString();
        if (authorities.contains(new SimpleGrantedAuthority(privilege))) {
            return true;
        }
        return false;
    }
}
