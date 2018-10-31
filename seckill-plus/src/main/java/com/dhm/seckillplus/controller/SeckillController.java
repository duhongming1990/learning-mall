package com.dhm.seckillplus.controller;

import com.dhm.seckillplus.domain.Order;
import com.dhm.seckillplus.domain.SeckillOrder;
import com.dhm.seckillplus.domain.User;
import com.dhm.seckillplus.service.GoodsService;
import com.dhm.seckillplus.service.OrderService;
import com.dhm.seckillplus.service.SeckillService;
import com.dhm.seckillplus.service.UserService;
import com.dhm.seckillplus.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class SeckillController {

	@Autowired
	UserService userService;

	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SeckillService seckillService;
	
    @RequestMapping("/do_miaosha")
    public String list(Model model,User user,
    		@RequestParam("goodsId")long goodsId) {
		System.out.println(user.toString());
    	model.addAttribute("user", user);
    	if(user == null) {
    		return "login";
    	}
    	//判断库存
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
//    		model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
    		return "miaosha_fail";
    	}
    	//判断是否已经秒杀到了
    	SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
//    		model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
    		return "miaosha_fail";
    	}
    	//减库存 下订单 写入秒杀订单
    	Order orderInfo = seckillService.seckill(user, goods);
    	model.addAttribute("orderInfo", orderInfo);
    	model.addAttribute("goods", goods);
        return "order_detail";
    }
}
