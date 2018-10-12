package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.bean.pojo.Shipping;

/**
 * Created by geely
 */
public interface IShippingService {

    Shipping add(Integer userId, Shipping shipping);
    int del(Integer userId, Integer shippingId);
    int update(Integer userId, Shipping shipping);
    Shipping select(Integer userId, Integer shippingId);
    PageInfo list(Integer userId, int pageNum, int pageSize);

}
