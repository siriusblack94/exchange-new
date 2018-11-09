package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.mining.entity.DividendRecordDetail;
import com.blockeng.mining.dto.DividendRecordDetailDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: sirius
 * @Date: 2018/10/12 11:32
 * @Description:
 */
public interface DividendRecordDetailMapper extends BaseMapper<DividendRecordDetail> {

    List<DividendRecordDetailDTO> selectListPage(Page<DividendRecordDetailDTO> page, @Param("ew")Wrapper<DividendRecordDetailDTO> wrapper);
}
