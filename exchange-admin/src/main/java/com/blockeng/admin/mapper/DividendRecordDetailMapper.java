package com.blockeng.admin.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.DividendRecordDetailDTO;
import com.blockeng.admin.entity.DividendRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: sirius
 * @Date: 2018/10/12 11:32
 * @Description:
 */
public interface DividendRecordDetailMapper extends BaseMapper<DividendRecordDetail> {
    List<DividendRecordDetailDTO> selectDetailListPage(Page<DividendRecordDetailDTO> page, @Param("ew") Wrapper<DividendRecordDetailDTO> wrapper);
}
