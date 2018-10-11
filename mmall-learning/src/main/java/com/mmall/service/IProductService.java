package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.bean.pojo.Product;
import com.mmall.bean.vo.ProductDetailVo;

/**
 * Created by geely
 */
public interface IProductService {

    Integer saveOrUpdateProduct(Product product);

    Integer setSaleStatus(Integer productId, Integer status);

    ProductDetailVo manageProductDetail(Integer productId);

    PageInfo getProductList(int pageNum, int pageSize);

    PageInfo searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ProductDetailVo getProductDetail(Integer productId);

    PageInfo getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);



}
