package com.dhm.seckillplus.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * 统一返回值类
 *
 * @author 杜洪明
 * @creation 2018年10月10日
 */
//保证序列化json的时候,如果是null的对象,key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标识
     */
    public static final int SUCCESS = 0;
    /**
     * 失败标识
     */
    public static final int FAIL = 1;
    /**
     * 没有权限
     */
    public static final int NO_PERMISSION = 2;
    /**
     * 输出消息
     */
    private String msg = "success";
    /**
     * 输出状态
     */
    private int code = SUCCESS;
    /**
     * 输出JavaBean
     */
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }

    //使之不在json序列化结果当中
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == SUCCESS;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
