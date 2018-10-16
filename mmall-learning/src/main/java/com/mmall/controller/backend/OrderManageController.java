package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.bean.pojo.User;
import com.mmall.bean.vo.OrderVo;
import com.mmall.common.Const;
import com.mmall.common.response.ResultBean;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ResultBean<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo pageInfo = iOrderService.manageList(pageNum, pageSize);
        //填充我们增加产品的业务逻辑
        return new ResultBean<>(pageInfo);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ResultBean<OrderVo> orderDetail(HttpSession session, Long orderNo) {
        //填充我们增加产品的业务逻辑
        OrderVo orderVo = iOrderService.manageDetail(orderNo);
        return new ResultBean<>(orderVo);
    }


    @RequestMapping("search.do")
    @ResponseBody
    public ResultBean<PageInfo> orderSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        //填充我们增加产品的业务逻辑
        PageInfo pageInfo = iOrderService.manageSearch(orderNo, pageNum, pageSize);
        return new ResultBean<>(pageInfo);
    }


    @RequestMapping("send_goods.do")
    @ResponseBody
    public ResultBean<String> orderSendGoods(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        //填充我们增加产品的业务逻辑
        String s = iOrderService.manageSendGoods(orderNo);
        return new ResultBean<>(s);
    }


}
