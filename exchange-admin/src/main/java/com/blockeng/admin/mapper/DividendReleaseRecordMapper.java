package com.blockeng.admin.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.DividendReleaseRecordDTO;
import com.blockeng.admin.entity.DividendRecordDetail;
import com.blockeng.admin.entity.DividendReleaseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Auther: sirius
 * @Date: 2018/8/27 22:22
 * @Description:
 */
public interface DividendReleaseRecordMapper extends BaseMapper<DividendReleaseRecord> {

    List<DividendReleaseRecordDTO> selectReleaseDetailListPage(Page<DividendReleaseRecordDTO> page, @Param("ew") Wrapper<DividendReleaseRecordDTO> wrapper);
}
