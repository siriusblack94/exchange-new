package com.blockeng.mining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.PrivatePlacementReleaseRecord;

/**
 * @Auther: sirius
 * @Date: 2018/8/15 13:31
 * @Description:
 */
public interface PrivatePlacementReleaseRecordService  extends IService<PrivatePlacementReleaseRecord> {
    void release();
}
