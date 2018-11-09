package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.mapper.SysUserMapper;
import com.blockeng.admin.security.JwtToken;
import com.blockeng.admin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 平台用户 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-11
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtTokenUtil;

    @Override
    public SysUser login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        String accessToken = jwtTokenUtil.generateToken(sysUser);
        sysUser.setToken("Bearer " + accessToken);
        return sysUser;
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring("Bearer ".length());
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return "error";
    }

    @Override
    public Page<SysUser> selectSysUserPage(Page<SysUser> page, EntityWrapper<SysUser> ew) {
        page.setRecords(baseMapper.selectSysUserPage(page, ew));
        return page;
    }
}
