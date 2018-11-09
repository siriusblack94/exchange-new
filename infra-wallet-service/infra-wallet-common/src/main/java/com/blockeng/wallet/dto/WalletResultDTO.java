package com.blockeng.wallet.dto;

import com.blockeng.wallet.utils.GsonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description: 通用返回结果
 * @Author: Chen Long
 * @Date: Created in 2018/4/8 下午4:00
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
public class WalletResultDTO {

    /**
     * 响应代码
     */
    @ApiModelProperty(example = "响应代码")
    private int statusCode;

    /**
     * 结果描述
     */
    @ApiModelProperty(example = "结果描述")
    private String resultDesc;

    /**
     * 响应数据对象
     */
    @ApiModelProperty(example = "响应数据对象")
    private Object result;

    public WalletResultDTO() {
    }

    public WalletResultDTO(int statusCode, String resultDesc) {
        this.statusCode = statusCode;
        this.resultDesc = resultDesc;
    }


    public WalletResultDTO(int statusCode, Object result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public WalletResultDTO(int statusCode, String resultDesc, Object result) {
        this.statusCode = statusCode;
        this.resultDesc = resultDesc;
        this.result = result;
    }

    private WalletResultDTO(WalletResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    private WalletResultDTO(WalletResultCode resultCode, Object data) {
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public static WalletResultDTO successResult() {
        return new WalletResultDTO(WalletResultCode.SUCCESS);
    }

    public static WalletResultDTO successResult(Object data) {
        return new WalletResultDTO(WalletResultCode.SUCCESS, data);
    }

    public static WalletResultDTO errorResult(WalletResultCode resultCode) {
        return new WalletResultDTO(resultCode);
    }

    public static WalletResultDTO errorResult(int code, String message) {
        return new WalletResultDTO(code, message);
    }

    public static WalletResultDTO errorResult(int code, String message, Object result) {
        return new WalletResultDTO(code, message, result);
    }

    public static WalletResultDTO errorResult(int code, Object result) {
        return new WalletResultDTO(code, result);
    }


    public String toJson() {
        return GsonUtils.toJson(this);
    }
}
