package com.mmall.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/11 11:01
 */
public class CommonBean implements Serializable {

    protected Integer id;
    protected Date createTime;
    protected Date updateTime;

    protected int pageNum = 1;
    protected int pageSize = 10;
    protected String orderBy;
}