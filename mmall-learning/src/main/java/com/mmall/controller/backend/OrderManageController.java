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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/21 12:43
 */
@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("list.do")
    public ResultBean<PageInfo> orderList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo pageInfo = iOrderService.manageList(pageNum, pageSize);
        return new ResultBean<>(pageInfo);
    }

    /**
     *
     * @param orderNo
     * @return
     */
    @GetMapping("detail.do")
    public ResultBean<OrderVo> orderDetail(Long orderNo) {
        OrderVo orderVo = iOrderService.manageDetail(orderNo);
        return new ResultBean<>(orderVo);
    }

    /**
     *
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("search.do")
    public ResultBean<PageInfo> orderSearch(
            Long orderNo,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo pageInfo = iOrderService.manageSearch(orderNo, pageNum, pageSize);
        return new ResultBean<>(pageInfo);
    }

    /**
     *
     * @param orderNo
     * @return
     */
    @PostMapping("send_goods.do")
    public ResultBean<String> orderSendGoods(Long orderNo) {
        String s = iOrderService.manageSendGoods(orderNo);
        return new ResultBean<>(s);
    }


}
