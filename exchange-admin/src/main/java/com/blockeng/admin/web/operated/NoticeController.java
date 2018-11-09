package com.blockeng.admin.web.operated;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Notice;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.NoticeService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 系统资讯公告信息 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/notice")

@Api(value = "系统资讯公告信息controller", tags = {"文章管理"})
public class NoticeController {

    private static Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    /**
     * 公告列表
     *
     * @param title     标题
     * @param status    文章状态
     * @param startTime 创建时间-开始时间
     * @param endTime   创建时间-结束时间
     * @return
     */
    @Log(value = "查询系统资讯公告信息列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('notice_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody()
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "系统资讯公告信息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "文章状态", dataType = "Integer"),
            @ApiImplicitParam(name = "startTime", value = "创建时间-开始时间", dataType = "Integer"),
            @ApiImplicitParam(name = "endTime", value = "创建时间-结束时间", dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = Notice.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
            int current,
            int size,
            String title,
            String status,
            String startTime,
            String endTime) {
        Page<Notice> pager = new Page<>(current, size);
        EntityWrapper<Notice> ew = new EntityWrapper<>();

        if (StringUtils.isNotBlank(title)) {
            ew.like("title", title);
        }
        if (StringUtils.isNotBlank(status)) {
            ew.eq("status", status);
        }
        if (StringUtils.isNotBlank(startTime)) {
            ew.ge("created", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            ew.le("created", endTime + " 23:59:59");
        }
        ew.orderBy("id", false);
        log.info("========" + noticeService.selectPage(pager, ew).getRecords().toString());
        return ResultMap.getSuccessfulResult(noticeService.selectPage(pager, ew));
    }

    /**
     * 删除公告
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "删除系统资讯公告信息", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('notice_delete')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping({"/delete"})
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "公告id数组", required = true)
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除系统资讯公告信息", httpMethod = "POST")
    public ResultMap delete(@RequestBody String[] id) {
        logger.info("NoticeController delete id:" + id);
        if (id == null || id.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        Boolean result = noticeService.deleteBatchIds(Arrays.asList(id));
        if (result) {
            return ResultMap.getSuccessfulResult("操作成功");
        } else {
            return ResultMap.getSuccessfulResult("操作失败");
        }
    }

    /**
     * 获取一个公告详情
     *
     * @param id 公告id
     * @return
     */
    @Log(value = "获取一个系统资讯公告信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('notice_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取一个系统资讯公告信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "公告id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = Notice.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        logger.info("NoticeController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        Notice notice = noticeService.selectById(id);
        return ResultMap.getSuccessfulResult(notice);
    }

    /**
     * 新增公告
     *
     * @param notice
     * @return
     */
    @Log(value = "新增公告", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('notice_create')")
    @PostMapping("/create")
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增公告", httpMethod = "POST")
    @ApiImplicitParam(name = "notice", value = "公告对象", required = true, dataType = "Notice")
    public ResultMap create(@RequestBody Notice notice) {
        log.info("NoticeController create:" + notice.toString());
        if (null != notice && StringUtils.isEmpty(notice.getTitle())) {
            return ResultMap.getFailureResult("标题不能为空！");
        }
        if (null != notice && !StringUtils.isEmpty(notice.getTitle()) && notice.getTitle().length() > 100) {
            return ResultMap.getFailureResult("标题字数长度不能超过100！");
        }
        if (null != notice && !StringUtils.isEmpty(notice.getAuthor()) && notice.getAuthor().length() > 50) {
            return ResultMap.getFailureResult("作者字数长度不能超过50！");
        }
        if (null != notice && notice.getSort() == null) {
            return ResultMap.getFailureResult("排序不能为空！");
        }
        if (null != notice && notice.getSort() != null) {
            String regEx = "[0-9]{0,4}";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(String.valueOf(notice.getSort()));
            boolean rs = matcher.matches();
            if (!rs) {
                return ResultMap.getFailureResult("此值只能为长度不超过5的正数");
            }
        }
        if (null != notice && StringUtils.isEmpty(notice.getContent())) {
            return ResultMap.getFailureResult("内容不能为空！");
        }
        if (null != notice && !StringUtils.isEmpty(notice.getDescription()) && notice.getDescription().length() > 200) {
            return ResultMap.getFailureResult("简介字数长度不能超过200");
        }
        if (notice != null) {
            notice.setCreated(new Date());
        }
        EntityWrapper<Notice> ew = new EntityWrapper<>();
        ew.eq("title", notice.getTitle());
        Notice notice1 = noticeService.selectOne(ew);
        if (notice1 != null) {
            return ResultMap.getFailureResult("该标题已存在！请换个标题");
        }
        notice.setStatus(CommonUtils.STATUS_1);
        String ms = "操作成功";
        try {
            Boolean rs = noticeService.insert(notice);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("NoticeController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 更新公告
     *
     * @param notice
     * @return
     */
    @Log(value = "更新公告", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('notice_update')")
    @RequestMapping({"/update"})
    @ResponseBody
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "更新公告", httpMethod = "POST")
    @ApiImplicitParam(name = "notice", value = "更新公告", required = true, dataType = "Notice")
    public ResultMap update(@RequestBody Notice notice) {
        log.info("NoticeController update:" + notice.toString());
        if (null != notice && notice.getId() == null) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (null != notice && StringUtils.isEmpty(notice.getTitle())) {
            return ResultMap.getFailureResult("标题不能为空！");
        }
        if (null != notice && !StringUtils.isEmpty(notice.getTitle()) && notice.getTitle().length() > 100) {
            return ResultMap.getFailureResult("标题字数长度不能超过100！");
        }
        if (null != notice && !StringUtils.isEmpty(notice.getAuthor()) && notice.getAuthor().length() > 50) {
            return ResultMap.getFailureResult("作者字数长度不能超过50！");
        }
        if (null != notice && notice.getSort() != null) {
            String regEx = "[0-9]{0,4}";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(String.valueOf(notice.getSort()));
            boolean rs = matcher.matches();
            if (!rs) {
                return ResultMap.getFailureResult("此致只能为长度不超过5的正数");
            }
        }
        if (null != notice && StringUtils.isEmpty(notice.getContent())) {
            return ResultMap.getFailureResult("内容不能为空！");
        }
        if (null != notice && !StringUtils.isEmpty(notice.getDescription()) && notice.getDescription().length() > 200) {
            return ResultMap.getFailureResult("简介字数长度不能超过200");
        }
        String ms = "操作成功";
        Notice notice1 = noticeService.selectById(notice.getId());
        if (notice1 != null && !notice1.getTitle().equals(notice.getTitle())) {
            EntityWrapper<Notice> ew = new EntityWrapper<>();
            ew.eq("title", notice.getTitle());
            notice1 = noticeService.selectOne(ew);
            if (notice1 != null) {
                return ResultMap.getFailureResult("该标题已存在！请换个标题");
            }
        }
        try {
            Boolean rs = noticeService.updateById(notice);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }
        } catch (Exception e) {
            log.info("NoticeController create:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }

    /**
     * 禁用启用
     */
    @Log(value = "禁/启信息", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('notice_status_update')")
    @GetMapping
    @RequestMapping({"/updateStatus"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "禁/启信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态值0禁1启用", required = true, dataType = "String")
    })
    public ResultMap updateStatus(String id, String status) {
        log.info("NoticeController updateStatus id:" + id + ",status:" + status);
        if (StringUtils.isBlank(id)) {
            return ResultMap.getFailureResult("必要参数id不能为空！");
        }
        if (StringUtils.isBlank(status)) {
            return ResultMap.getFailureResult("必要参数status不能为空！");
        }
        String ms = "操作成功";
        try {
            Notice notice = new Notice();
            notice.setId(Long.valueOf(id));
            notice.setStatus(Integer.valueOf(status));
            Boolean rs = noticeService.updateById(notice);
            if (!rs) {
                ms = "操作失败";
                return ResultMap.getFailureResult(ms);
            }

        } catch (Exception e) {
            log.info("NoticeController updateStatus:" + e);
        }
        return ResultMap.getSuccessfulResult(ms);
    }
}
