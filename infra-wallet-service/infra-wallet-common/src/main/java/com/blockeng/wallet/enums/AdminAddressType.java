package com.blockeng.wallet.enums;

public enum AdminAddressType {
    ADMIN_ADDRESS_TYPE_COLLECT(1), //归账地址
    ADMIN_ADDRESS_TYPE_LOAD(2), //打款
    ADMIN_ADDRESS_TYPE_FEE(3), //手续费
    ADMIN_ADDRESS_TYPE_RECHARGE(4); //充值包地址


    private int type;

    AdminAddressType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
