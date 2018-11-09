package com.blockeng.admin.service;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.DividendAccountDTO;
import com.blockeng.admin.dto.DividendRecordDetailDTO;
import com.blockeng.admin.dto.DividendReleaseRecordDTO;
import com.blockeng.admin.entity.DividendRecord;
import com.blockeng.admin.entity.DividendRecordDetail;
import com.blockeng.admin.entity.DividendReleaseRecord;

/**
 * <p>
 * 邀请奖励
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

public interface DividendRecordService extends IService<DividendRecord> {

    Page<DividendRecordDetailDTO> selectDetailListPage(Page<DividendRecordDetailDTO> var1, Wrapper<DividendRecordDetailDTO> var2);

    Page<DividendReleaseRecordDTO> selectReleaseListPage(Page<DividendReleaseRecordDTO> var1, Wrapper<DividendReleaseRecordDTO> var2);

}
