package com.blockeng.service;

import com.blockeng.entity.CashWithdrawals;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.vo.CashWithDrawalsForm;

/**
 * <p>
 * 法币提现表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CashWithdrawalsService extends IService<CashWithdrawals> {

    /**
     * 法币提现
     *
     * @param cashWithdrawals 法币提现请求参数
     * @param userDetails     当前登录用户ID
     * @return
     */
    Response c2cSell(CashWithDrawalsForm cashWithdrawals, UserDetails userDetails);
}
