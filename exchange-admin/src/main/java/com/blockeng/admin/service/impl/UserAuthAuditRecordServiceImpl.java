package com.blockeng.admin.service.impl;

import com.blockeng.admin.entity.UserAuthAuditRecord;
import com.blockeng.admin.mapper.UserAuthAuditRecordMapper;
import com.blockeng.admin.service.UserAuthAuditRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实名认证审核信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserAuthAuditRecordServiceImpl extends ServiceImpl<UserAuthAuditRecordMapper, UserAuthAuditRecord> implements UserAuthAuditRecordService {

}
