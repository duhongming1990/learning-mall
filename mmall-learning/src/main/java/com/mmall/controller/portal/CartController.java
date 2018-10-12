package com.mmall.controller.portal;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.bean.pojo.User;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.common.response.ResultBean;
import com.mmall.service.ICartService;
import com.mmall.bean.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;

/**
 * Created by geely
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;


    @RequestMapping("/list")
    public ResultBean<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.list(user.getId());
        return new ResultBean<>(cartVo);
    }

    @RequestMapping("/add")
    public ResultBean<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.add(user.getId(), productId, count);
        return new ResultBean<>(cartVo);
    }


    @RequestMapping("/update")
    public ResultBean<CartVo> update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.update(user.getId(), productId, count);
        return new ResultBean<>(cartVo);
    }

    @RequestMapping("/delete_product")
    public ResultBean<CartVo> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.deleteProduct(user.getId(), productIds);
        return new ResultBean<>(cartVo);
    }


    @RequestMapping("/select_all")
    public ResultBean<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
        return new ResultBean<>(cartVo);
    }

    @RequestMapping("/un_select_all")
    public ResultBean<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
        return new ResultBean<>(cartVo);
    }


    @RequestMapping("/select")
    public ResultBean<CartVo> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
        return new ResultBean<>(cartVo);
    }

    @RequestMapping("/un_select")
    public ResultBean<CartVo> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        CartVo cartVo = iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
        return new ResultBean<>(cartVo);
    }


    @RequestMapping("/get_cart_product_count")
    public ResultBean<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return new ResultBean<>(0);
        }
        Integer count = iCartService.getCartProductCount(user.getId());
        return new ResultBean<>(count);
    }


    //全选
    //全反选

    //单独选
    //单独反选

    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.


}
