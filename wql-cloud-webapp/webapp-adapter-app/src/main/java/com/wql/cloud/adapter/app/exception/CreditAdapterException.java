package com.wql.cloud.adapter.app.exception;

import java.util.List;
import java.util.Map;

/**
 * 业务异常
 */
public class CreditAdapterException extends RuntimeException {

    private static final long serialVersionUID = 73951957376962427L;

    /**
     * 错误枚举
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    /**
     * 错误数据
     */
    private Object errorData;

    /**
     * 通用业务异常数据
     */
    private Map<String, String> bizDataMap;

    private List<CreditAdaptorErrorCode> groupErrorCode;

    public CreditAdapterException() {
        super();
    }

    public CreditAdapterException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CreditAdapterException(List<CreditAdaptorErrorCode> groupErrorCode,
                                  CreditAdaptorErrorCode grouponErrorCode) {
        this.groupErrorCode = groupErrorCode;
        this.errorCode = grouponErrorCode.getErrorCode();
        this.errorMsg = grouponErrorCode.getErrorMsg();

    }

    public CreditAdapterException(CreditAdaptorErrorCode grouponErrorCode, Map<String, String> bizDataMap) {
        this.errorCode = grouponErrorCode.getErrorCode();
        this.errorMsg = grouponErrorCode.getErrorMsg();
        this.bizDataMap = bizDataMap;
    }

    public CreditAdapterException(CreditAdaptorErrorCode grouponErrorCode) {
        this.errorCode = grouponErrorCode.getErrorCode();
        this.errorMsg = grouponErrorCode.getErrorMsg();
    }

    public CreditAdapterException(CreditAdaptorErrorCode grouponErrorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = grouponErrorCode.getErrorCode();
        this.errorMsg = grouponErrorCode.getErrorMsg();
    }

    public CreditAdapterException(CreditAdaptorErrorCode grouponErrorCode, String errorInfo, Throwable throwable) {
        super(throwable);
        this.errorCode = grouponErrorCode.getErrorCode();
        this.errorMsg = grouponErrorCode.getErrorMsg() + ":" + errorInfo;

    }

    public CreditAdapterException(CreditAdaptorErrorCode grouponErrorCode, String errorInfo) {
        this.errorCode = grouponErrorCode.getErrorCode();
        this.errorMsg = grouponErrorCode.getErrorMsg() + ":" + errorInfo;

    }

    public CreditAdapterException(List<CreditAdaptorErrorCode> groupErrorCode) {
        this.groupErrorCode = groupErrorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }

    public List<CreditAdaptorErrorCode> getGroupErrorCode() {
        return groupErrorCode;
    }

    public void setGroupErrorCode(List<CreditAdaptorErrorCode> groupErrorCode) {
        this.groupErrorCode = groupErrorCode;
    }

    public Map<String, String> getBizDataMap() {
        return bizDataMap;
    }

    public void setBizDataMap(Map<String, String> bizDataMap) {
        this.bizDataMap = bizDataMap;
    }

    @Override
    public String toString() {
        return "CreditAdaptorException [errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
    }

    public String getUserDefinedExceptionName() {
        return this.getClass().getName() + "-" + this.getErrorCode();
    }


}
