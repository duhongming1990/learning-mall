package com.dhm.seckillplus.common.exception;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/8/31 15:16
 */
public class CommonException extends RuntimeException {

    protected int errorCode;
    protected String errorInfo;

    public CommonException(int errorCode,String errorInfo){
        this.errorCode=errorCode;
        this.errorInfo=errorInfo;
    }

    @Override
    public String toString() {
        return "CommonException{" +
                "errorCode=" + errorCode +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}