package com.blockeng.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.blockeng.admin.dto.UserWorkIssueDTO;
import com.blockeng.admin.entity.WorkIssue;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单记录 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface WorkIssueMapper extends BaseMapper<WorkIssue> {

    public List<UserWorkIssueDTO> selectMapPage(Pagination page, Map<String, Object> paramMap);

    UserWorkIssueDTO selectOneObj(Long id);


}
