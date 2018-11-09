package com.blockeng.service.impl;

import com.blockeng.entity.UserAuthAuditRecord;
import com.blockeng.mapper.UserAuthAuditRecordMapper;
import com.blockeng.service.UserAuthAuditRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实名认证审核信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class UserAuthAuditRecordServiceImpl extends ServiceImpl<UserAuthAuditRecordMapper, UserAuthAuditRecord> implements UserAuthAuditRecordService {

}
