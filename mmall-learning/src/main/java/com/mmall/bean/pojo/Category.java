package com.mmall.bean.pojo;

import com.mmall.bean.CommonBean;

import java.util.Date;
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/11 11:05
 */
public class Category extends CommonBean {

    private Integer parentId;

    private String parentIds;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    public Category(Integer id,Integer parentId, String parentIds, String name, Boolean status, Integer sortOrder,Date createTime,Date updateTime) {
        this.id = id;
        this.parentId = parentId;
        this.parentIds = parentIds;
        this.name = name;
        this.status = status;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Category() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}