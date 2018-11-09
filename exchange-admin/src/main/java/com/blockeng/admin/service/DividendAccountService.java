package com.blockeng.admin.service;



import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.DividendAccountDTO;
import com.blockeng.admin.entity.DividendAccount;

/**
 * <p>
 * 邀请奖励解冻
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface DividendAccountService extends IService<DividendAccount> {
    Page<DividendAccountDTO> selectListPage(Page<DividendAccountDTO> var1, Wrapper<DividendAccountDTO> var2);


}
