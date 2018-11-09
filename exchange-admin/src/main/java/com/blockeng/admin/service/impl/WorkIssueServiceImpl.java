package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.dto.UserCashRechargeDTO;
import com.blockeng.admin.dto.UserWorkIssueDTO;
import com.blockeng.admin.entity.WorkIssue;
import com.blockeng.admin.mapper.WorkIssueMapper;
import com.blockeng.admin.service.WorkIssueService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 工单记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService {

    @Override
    public Page<UserWorkIssueDTO> selectMapPage(Page<UserWorkIssueDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectMapPage(page, paramMap));
    }

    @Override
    public UserWorkIssueDTO selectOneObj(Long id) {
        return baseMapper.selectOneObj(id);

    }
}
