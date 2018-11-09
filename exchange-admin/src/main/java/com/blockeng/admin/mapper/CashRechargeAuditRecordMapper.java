package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.UserCashRechargeDTO;
import com.blockeng.admin.entity.CashRecharge;
import com.blockeng.admin.entity.CashRechargeAuditRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 充值审核记录表 Mapper 接口
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
public interface CashRechargeAuditRecordMapper extends BaseMapper<CashRechargeAuditRecord> {

}
