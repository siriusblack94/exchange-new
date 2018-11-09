package com.blockeng.admin.service;

import com.blockeng.admin.entity.SysUser;

/**
 * @Description: 多级审核权限校验
 * @Author: Chen Long
 * @Date: Created in 2018/5/26 上午11:33
 * @Modified by: Chen Long
 */
public interface MultiLevelAuditService {

    /**
     * 法币充值审核权限校验
     *
     * @param orderId 充值订单号
     * @param sysUser 当前登录用户
     * @return
     */
    boolean cashRechargePermissionCheck(Long orderId, SysUser sysUser);

    /**
     * 法币提现审核权限校验
     *
     * @param orderId 提现申请单号
     * @param sysUser 当前登录用户
     * @return
     */
    boolean cashWithdrawPermissionCheck(Long orderId, SysUser sysUser);

    /**
     * 数字货币提现权限校验
     *
     * @param orderId 提币申请单号
     * @param sysUser 当前登录用户
     * @return
     */
    boolean coinWithdrawPermissionCheck(Long orderId, SysUser sysUser);


    boolean coinBucklePermissionCheck(Long orderId, SysUser sysUser);
}
