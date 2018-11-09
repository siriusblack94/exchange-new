package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.TurnoverOrderCountDTO;
import com.blockeng.admin.dto.UserBlanceTopDTO;
import com.blockeng.admin.dto.UserCountRegDTO;
import com.blockeng.admin.dto.UserDTO;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.entity.UserAuthAuditRecord;
import com.blockeng.admin.mapper.CashRechargeMapper;
import com.blockeng.admin.mapper.UserMapper;
import com.blockeng.admin.service.UserService;
import com.blockeng.framework.enums.AdminUserType;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.CashRechargeStatus;
import com.google.common.base.Strings;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CashRechargeMapper cashRechargeMapper;

    /**
     * 获取C2C管理员账户（法币充值提现）
     *
     * @return
     */
    @Override
    public User queryAdminUser(AdminUserType adminUserType) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("username", adminUserType.getUserName())
                .eq("status", BaseStatus.EFFECTIVE.getCode())
                .eq("type", adminUserType.getType());
        List<User> users = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public Page<UserCountRegDTO> selectRegCountPage(Page<UserCountRegDTO> page, Wrapper<UserCountRegDTO> wrapper) {
        wrapper = (Wrapper<UserCountRegDTO>) SqlHelper.fillWrapper(page, wrapper);
        List<UserCountRegDTO> countRegDTOList = this.userMapper.selectRegCountPage(page, wrapper);
        for (UserCountRegDTO countRegDTO : countRegDTOList) {
            //手机注册,所以绑定手机人数=注册人数
            countRegDTO.setMobileBindNum(countRegDTO.getRegNum());
            countRegDTO.setEmailBindNum(userMapper.countEmailBindByDate(countRegDTO.getCountDate()));
            countRegDTO.setSetPayPwdNum(userMapper.countSetPayPwdByDate(countRegDTO.getCountDate()));
            String idStr = userMapper.selectIdStrBydate(countRegDTO.getCountDate());
            if (!Strings.isNullOrEmpty(idStr)) {//注册用户充值数量
                Integer rechargeNum = cashRechargeMapper.countByDateAndUidStrs(
                        countRegDTO.getCountDate(), idStr, CashRechargeStatus.SUCCESS.getCode());
                countRegDTO.setRechargeNum(rechargeNum);
            } else {
                countRegDTO.setRechargeNum(0);
            }

        }
        page.setRecords(countRegDTOList);
        return page;
    }

    @Override
    public Page<UserBlanceTopDTO> selectBalanceTopPage(Page<UserBlanceTopDTO> page, Wrapper<UserBlanceTopDTO> wrapper) {
        wrapper = (Wrapper<UserBlanceTopDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(this.userMapper.selectBalanceTopPage(page, wrapper));
        return page;
    }


    @Override
    public List<TurnoverOrderCountDTO> selectUserCount(String[] coins) {
        return baseMapper.selectUserCount(coins);
    }

    @Override
    public Page<UserDTO> selectListPage(Page<UserDTO> page, Wrapper<UserDTO> wrapper) {
        wrapper = (Wrapper<UserDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }

    @Override
    public List<UserDTO> selectListAuditFromAuth(int current, int end, String userAuthStatus, String startTime, String endTime) {
        return baseMapper.selectListAuditFromAuth(current, end, userAuthStatus, startTime, endTime);
    }

    @Override
    public List<UserDTO> selectListAuditFromUser(EntityWrapper<UserDTO> ew) {
        return baseMapper.selectListAuditByUser(ew);
    }

    @Override
    public Page<UserDTO> selectListAuditPage(Page<UserDTO> page, Wrapper<UserDTO> wrapper) {
        wrapper = (Wrapper<UserDTO>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListAuditPage(page, wrapper));
        return page;
    }

    @Override
    public int selectAuditAccount(EntityWrapper<UserAuthAuditRecord> ew) {
        return baseMapper.selectAuditAccount(ew);
    }
}
