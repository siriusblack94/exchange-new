package com.blockeng.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.RewardRecord;
import com.blockeng.mapper.RewardRecordMapper;
import com.blockeng.service.RewardRecordService;
import org.springframework.stereotype.Service;
/**
 * 奖励记录
 * @author shadow
 * @created 2018/10/19
 */
@Service
public class RewardRecordServiceImpl extends ServiceImpl<RewardRecordMapper,RewardRecord> implements RewardRecordService {
}
