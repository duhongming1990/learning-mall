package com.mooc.house.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/11 11:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonBean implements Serializable {

    protected Long id;
    protected Date createTime;
    protected Date updateTime;

    protected int pageNum = 1;
    protected int pageSize = 10;
    protected String orderBy;
}