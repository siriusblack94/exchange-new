package com.blockeng.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.dto.UserWorkIssueDTO;
import com.blockeng.admin.entity.WorkIssue;

import java.util.Map;

/**
 * <p>
 * 工单记录 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface WorkIssueService extends IService<WorkIssue> {

    public Page<UserWorkIssueDTO> selectMapPage(Page<UserWorkIssueDTO> page, Map<String, Object> paramMap);

    UserWorkIssueDTO selectOneObj(Long id);

}
