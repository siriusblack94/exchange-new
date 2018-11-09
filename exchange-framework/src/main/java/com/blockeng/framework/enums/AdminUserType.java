package com.blockeng.framework.enums;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/26 下午9:40
 * @Modified by: Chen Long
 */
public enum AdminUserType {

    ADMIN("Admin", 0, "管理员，收取交易/提币手续费"),
    C2C_ADMIN("C2CAdmin", 0, "C2C管理员，法币充值提现转换平台币"),
    AGENT_ADMIN("AgentAdmin", 2, "代理商管理员账户");

    /**
     * 用户名
     */
    private String userName;

    /**
     * 类型
     */
    private int type;

    /**
     * 描述
     */
    private String desc;

    AdminUserType(String userName, int type, String desc) {
        this.userName = userName;
        this.type = type;
        this.desc = desc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

