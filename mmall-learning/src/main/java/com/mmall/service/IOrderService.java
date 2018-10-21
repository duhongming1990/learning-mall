package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.bean.pojo.Order;
import com.mmall.bean.vo.OrderProductVo;
import com.mmall.bean.vo.OrderVo;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * Created by geely
 */
public interface IOrderService {
    Map<String, String> pay(Long orderNo, Integer userId, String path);
    int aliCallback(Map<String, String> params);
    void queryOrderPayStatus(Integer userId, Long orderNo);
    OrderVo createOrder(Integer userId, Integer shippingId);
    int cancel(Integer userId, Long orderNo);
    OrderProductVo getOrderCartProduct(Integer userId);
    OrderVo getOrderDetail(Integer userId, Long orderNo);
    PageInfo getOrderList(Integer userId, int pageNum, int pageSize);



    //backend
    PageInfo manageList(int pageNum, int pageSize);
    OrderVo manageDetail(Long orderNo);
    PageInfo manageSearch(Long orderNo, int pageNum, int pageSize);
    String manageSendGoods(Long orderNo);


    //hour个小时以内未付款的订单，进行关闭
    void closeOrder(int hour);


}
