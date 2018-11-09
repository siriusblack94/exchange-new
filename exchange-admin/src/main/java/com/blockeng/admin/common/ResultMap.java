package com.blockeng.admin.common;

import java.util.HashMap;

public class ResultMap extends HashMap<String, Object> {

    private static final long serialVersionUID = -4970973511892646114L;

    /**
     * 返回成功結果，所有成功結果都用0表示
     */
    public static final int RETURN_RESULT_SUCCESSFUL = 0;

    /**
     * 返回錯誤結果，大部分的錯誤結果都用1表示，还可以自定义错误结果类型。
     */
    public static final int RETURN_RESULT_ERROR = 1;

    //public final static String ERROR = "error";

    public final static String RESULT = "data";

    public final static String STATUSCODE = "errcode";

    public final static String RESULT_DESC = "errmsg";

    public ResultMap() {
        this(RETURN_RESULT_SUCCESSFUL, null, null);
        //this.put(ERROR, false);
    }

    public ResultMap(Object result) {
        //this.put(ERROR, false);
        this.put(RESULT, result);
    }

    public ResultMap(int statusCode, Object result, String desc) {
        this.put(STATUSCODE, statusCode);
        this.put(RESULT, result);
        this.put(RESULT_DESC, desc);
    }

    public static ResultMap getSuccessfulResult(Object result, String desc) {
        return new ResultMap(RETURN_RESULT_SUCCESSFUL, result, desc);
    }

    public static ResultMap getSuccessfulResult(Object result) {
        return getSuccessfulResult(result, null);
    }

    public static ResultMap getSuccessfulResult(String desc) {
        return getSuccessfulResult(null, desc);
    }

    public static ResultMap getSuccessfulResult(Boolean falg, String desc) {
        return getSuccessfulResult(desc, null);
    }

    public static ResultMap getSuccessfulResult() {
        return getSuccessfulResult(null, null);
    }

    public static ResultMap getFailureResult(int statusCode, Object result,
                                             String desc) {
        return new ResultMap(statusCode, result, desc);
    }

    public static ResultMap getFailureResult(int statusCode, Object result) {
        return getFailureResult(statusCode, result, null);
    }

    public static ResultMap getFailureResult(int statusCode, String desc) {
        return getFailureResult(statusCode, null, desc);
    }

    public static ResultMap getFailureResult(int statusCode) {
        return getFailureResult(statusCode, null, null);
    }

    public static ResultMap getFailureResult(Object result, String desc) {
        return getFailureResult(RETURN_RESULT_ERROR, result, desc);
    }

    public static ResultMap getFailureResult(Object result) {
        return getFailureResult(result, null);
    }

    public static ResultMap getFailureResult(String desc) {
        return getFailureResult(null, desc);
    }

    public static ResultMap getFailureResult() {
        return getFailureResult(null, null);
    }

    public int getStatusCode() {
        Object status = get(STATUSCODE);
        return status == null ? RETURN_RESULT_ERROR : RETURN_RESULT_SUCCESSFUL;
    }
}
