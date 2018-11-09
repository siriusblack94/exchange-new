package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.mining.entity.DividendRecord;
import com.blockeng.mining.entity.Mine;
import com.blockeng.mining.entity.User;
import com.blockeng.mining.mapper.UserMapper;
import com.blockeng.mining.service.AssetSnapshotDetailService;
import com.blockeng.mining.service.DividendRecordService;
import com.blockeng.mining.service.MineService;
import com.blockeng.mining.service.UserService;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public List<User> authStatusList() {
        log.info("查询认证用户数据");
        return this.baseMapper.authStatusList();
    }

    @Override
    public List<User> inviteList() {
        log.info("查询邀请用户数据");
        return this.baseMapper.inviteList();
    }


}
