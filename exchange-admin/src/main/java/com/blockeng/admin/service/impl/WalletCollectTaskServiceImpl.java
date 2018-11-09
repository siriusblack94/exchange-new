package com.blockeng.admin.service.impl;

import com.blockeng.admin.entity.WalletCollectTask;
import com.blockeng.admin.mapper.WalletCollectTaskMapper;
import com.blockeng.admin.service.WalletCollectTaskService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Service
public class WalletCollectTaskServiceImpl extends ServiceImpl<WalletCollectTaskMapper, WalletCollectTask> implements WalletCollectTaskService {

}
