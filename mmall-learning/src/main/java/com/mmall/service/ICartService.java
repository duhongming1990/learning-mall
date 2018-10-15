package com.mmall.service;

import com.mmall.bean.vo.CartVo;

/**
 * Created by geely
 */
public interface ICartService {
    CartVo add(Integer userId, Integer productId, Integer count);
    CartVo update(Integer userId, Integer productId, Integer count);
    CartVo deleteProduct(Integer userId, String productIds);

    CartVo list(Integer userId);
    CartVo selectOrUnSelect(Integer userId, Integer productId, Integer checked);
    Integer getCartProductCount(Integer userId);
}
