package com.mooc.house.common.model;

import java.util.Date;

public class HouseUser extends CommonBean {
    private Long id;
    private Long houseId;
    private Long userId;
    private Date createTime;
    private Integer type;

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
