package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.CoinBuckleAuditRecord;
import com.blockeng.admin.mapper.CoinBuckleAuditRecordMapper;
import com.blockeng.admin.service.CoinBuckleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoinBuckleAuditRecordServiceImpl extends ServiceImpl<CoinBuckleAuditRecordMapper, CoinBuckleAuditRecord> implements CoinBuckleAuditRecordService {
}
