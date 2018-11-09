package com.blockeng.framework.enums;

public enum WorkIssueStatus {


    NOT_AN(1, "待回答"),
    AN(2, "回答");

    private int code;
    private String desc;

    WorkIssueStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static WorkIssueStatus getByCode(int code) {
        for (WorkIssueStatus workIssueStatus : WorkIssueStatus.values()) {
            if (code == workIssueStatus.getCode()) {
                return workIssueStatus;
            }
        }
        return null;
    }
}
