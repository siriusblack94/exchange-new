package com.blockeng.extend.dto;

import com.blockeng.extend.entity.UserSyn;
import com.blockeng.extend.util.DESUtil;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserSynDTO {

    /**
     * userId
     */
    private String userId;
    /**
     * 邀请人id
     */
    private String parentId;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实用户名
     */
    private String RealName;
    /**
     * 签名
     */
    private String sign;



    /**
     * user加密
     * @param userSyn
     * @param desUtil
     * @return
     */
    public UserSynDTO encryptUser(UserSyn userSyn, DESUtil desUtil){
        this.setUserId(desUtil.encrypt(userSyn.getId()))
                .setParentId(desUtil.encrypt(userSyn.getParentId()))
                .setAccount(desUtil.encrypt(userSyn.getMobile()))
                .setMail(desUtil.encrypt(userSyn.getMail()))
                .setMobile(desUtil.encrypt(userSyn.getMobile()))
                .setUserName(desUtil.encrypt(userSyn.getUserName()))
                .setRealName(desUtil.encrypt(userSyn.getRealName()))
                .setPassword(desUtil.encrypt(userSyn.getPassword()))
                .setSign(userSyn);
        return this;
    }

    private void setSign(UserSyn userSyn) {
        this.sign =
                 "userId="+userSyn.getId()
                +"&parentId="+userSyn.getParentId()
                +"&account="+userSyn.getAccount()
                +"&password="+userSyn.getPassword()
                +"&mobile="+userSyn.getMobile()
                +"&mail="+userSyn.getMail()
                +"&username="+userSyn.getUserName()
                +"&realname="+userSyn.getRealName();
    }


}
