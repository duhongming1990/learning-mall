package com.dhm.seckillplus.service;

import com.dhm.seckillplus.dao.mysql.OrderDao;
import com.dhm.seckillplus.domain.Order;
import com.dhm.seckillplus.domain.SeckillOrder;
import com.dhm.seckillplus.domain.User;
import com.dhm.seckillplus.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
	
	@Autowired
	OrderDao orderDao;
	
	public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
		return orderDao.getSeckillOrderByUserIdGoodsId(userId, goodsId);
	}

	@Transactional
	public Order createOrder(User user, GoodsVo goods) {
		Order order = new Order();
		order.setCreateDate(new Date());
		order.setDeliveryAddrId(0L);
		order.setGoodsCount(1);
		order.setGoodsId(goods.getId());
		order.setGoodsName(goods.getGoodsName());
		order.setGoodsPrice(goods.getSeckillPrice());
		order.setOrderChannel(1);
		order.setStatus(0);
		order.setUserId(user.getId());
		System.out.println(order.toString());
		long orderId = orderDao.insert(order);
		SeckillOrder miaoshaOrder = new SeckillOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setOrderId(orderId);
		miaoshaOrder.setUserId(user.getId());
		orderDao.insertSeckillOrder(miaoshaOrder);
		return order;
	}
	
}
