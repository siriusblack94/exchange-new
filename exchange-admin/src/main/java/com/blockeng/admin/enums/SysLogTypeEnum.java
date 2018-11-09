package com.blockeng.admin.enums;

/**
 * 系统LOG类型
 *
 * @author Haliyo
 */
public enum SysLogTypeEnum {

    NONE(0, ""),
    SELECT(1, "查询"),
    UPDATE(2, "修改"),
    INSERT(3, "新增"),
    DELETE(4, "删除"),
    EXPORT(5, "导出"),
    AUDIT(6, "审核");

    private final int code;

    private final String type;

    private SysLogTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static SysLogTypeEnum getByCode(int code) {
        for (SysLogTypeEnum deviceType : SysLogTypeEnum.values()) {
            if (deviceType.getCode() == code) {
                return deviceType;
            }
        }
        return null;
    }

    public static SysLogTypeEnum getDeviceTypeEnum(String type) {
        for (SysLogTypeEnum deviceType : SysLogTypeEnum.values()) {
            if (deviceType.getType().equals(type)) {
                return deviceType;
            }
        }
        return null;
    }

}
