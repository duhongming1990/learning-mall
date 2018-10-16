package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.bean.pojo.User;
import com.mmall.bean.vo.OrderProductVo;
import com.mmall.bean.vo.OrderVo;
import com.mmall.common.Const;
import com.mmall.common.response.ResultBean;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;


    @RequestMapping("create.do")
    @ResponseBody
    public ResultBean create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        OrderVo orderVo = iOrderService.createOrder(user.getId(), shippingId);
        return new ResultBean(orderVo);
    }


    @RequestMapping("cancel.do")
    @ResponseBody
    public ResultBean cancel(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        iOrderService.cancel(user.getId(), orderNo);
        return new ResultBean();
    }


    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ResultBean getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        OrderProductVo orderProductVo = iOrderService.getOrderCartProduct(user.getId());
        return new ResultBean(orderProductVo);
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ResultBean detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        OrderVo orderVo = iOrderService.getOrderDetail(user.getId(), orderNo);
        return new ResultBean(orderVo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ResultBean list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        PageInfo pageInfo = iOrderService.getOrderList(user.getId(), pageNum, pageSize);
        return new ResultBean(pageInfo);
    }


    @RequestMapping("pay.do")
    @ResponseBody
    public ResultBean pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String path = request.getSession().getServletContext().getRealPath("upload");
        Map<String, String> resultMap = iOrderService.pay(orderNo, user.getId(), path);
        return new ResultBean(resultMap);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSACheckedV2) {
//                return ResultBean.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

        //todo 验证各种数据
        iOrderService.aliCallback(params);
        return Const.AlipayCallback.RESPONSE_SUCCESS;
    }


    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ResultBean<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        return new ResultBean<>(true);
    }


}
