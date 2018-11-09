package com.blockeng.mining.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.AssetSnapshot;

/**
 * @Description: 资产快照接口
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:14
 * @Modified by: Chen Long
 */
public interface AssetSnapshotService extends IService<AssetSnapshot> {

    /**
     * 创建资产快照
     */
    boolean createAssetSnapshot();
}
