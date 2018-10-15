package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.bean.pojo.Shipping;
import com.mmall.bean.pojo.User;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.common.response.ResultBean;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */

@RestController
@RequestMapping("/shipping")
public class ShippingController {


    @Autowired
    private IShippingService iShippingService;


    @RequestMapping("/add")
    public ResultBean add(HttpSession session, Shipping shipping){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        Shipping currentShipping = iShippingService.add(user.getId(),shipping);
        return new ResultBean(currentShipping);
    }


    @RequestMapping("/del")
    public ResultBean del(HttpSession session,Integer shippingId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iShippingService.del(user.getId(),shippingId);
        return new ResultBean();
    }

    @RequestMapping("/update")
    public ResultBean update(HttpSession session,Shipping shipping){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iShippingService.update(user.getId(),shipping);
        return new ResultBean();
    }


    @RequestMapping("/select")
    public ResultBean<Shipping> select(HttpSession session,Integer shippingId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        Shipping shipping = iShippingService.select(user.getId(),shippingId);
        return new ResultBean<>(shipping);
    }


    @RequestMapping("/list")
    public ResultBean<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        PageInfo pageInfo = iShippingService.list(user.getId(),pageNum,pageSize);
        return new ResultBean<>(pageInfo);
    }














}
