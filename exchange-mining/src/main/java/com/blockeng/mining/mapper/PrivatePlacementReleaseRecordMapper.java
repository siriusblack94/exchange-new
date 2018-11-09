package com.blockeng.mining.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.mining.entity.PrivatePlacementReleaseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: sirius
 * @Date: 2018/8/15 13:34
 * @Description:
 */
public interface PrivatePlacementReleaseRecordMapper extends BaseMapper<PrivatePlacementReleaseRecord> {
    int insertBatch(@Param("privatePlacementReleaseRecords") List<PrivatePlacementReleaseRecord> privatePlacementReleaseRecords);
}
