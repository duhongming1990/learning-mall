package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.response.ResultBean;
import com.mmall.service.IProductService;
import com.mmall.bean.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by geely
 */

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;



    @GetMapping("/detail")
    public ResultBean<ProductDetailVo> detail(Integer productId){
        ProductDetailVo productDetailVo = iProductService.getProductDetail(productId);
        return new ResultBean<>(productDetailVo);
    }

    /**
     * 商品列表
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/list")
    public ResultBean<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        PageInfo pageInfo = iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
        return new ResultBean<>(pageInfo);
    }





}
