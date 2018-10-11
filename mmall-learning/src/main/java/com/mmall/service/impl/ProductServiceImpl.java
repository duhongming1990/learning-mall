package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.bean.pojo.Category;
import com.mmall.bean.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.bean.vo.ProductDetailVo;
import com.mmall.bean.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geely
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public Integer saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                return productMapper.updateByPrimaryKey(product);
            } else {
                return productMapper.insert(product);
            }
        }
        return 0;
    }


    @Override
    public Integer setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            throw CommonExceptions.UserCommonException.PARAMETER_TOKEN_ERROR.getCommonException();
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount == 0) {
            throw CommonExceptions.ProductCommonException.PRODUCT_NOT_FOUND.getCommonException();
        }
        return rowCount;
    }

    @Override
    public ProductDetailVo manageProductDetail(Integer productId) {
        if (productId == null) {
            throw CommonExceptions.UserCommonException.PARAMETER_TOKEN_ERROR.getCommonException();
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return productDetailVo;
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        if (product != null) {
            productDetailVo.setId(product.getId());
            productDetailVo.setSubtitle(product.getSubtitle());
            productDetailVo.setPrice(product.getPrice());
            productDetailVo.setMainImage(product.getMainImage());
            productDetailVo.setSubImages(product.getSubImages());
            productDetailVo.setCategoryId(product.getCategoryId());
            productDetailVo.setDetail(product.getDetail());
            productDetailVo.setName(product.getName());
            productDetailVo.setStatus(product.getStatus());
            productDetailVo.setStock(product.getStock());

            productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

            Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if (category == null) {
                productDetailVo.setParentCategoryId(0);//默认根节点
            } else {
                productDetailVo.setParentCategoryId(category.getParentId());
            }

            productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
            productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        } else {
            throw CommonExceptions.ProductCommonException.PRODUCT_UNSALE_DELETE.getCommonException();
        }
        return productDetailVo;
    }

    @Override
    public PageInfo getProductList(int pageNum, int pageSize) {
        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return pageResult;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }


    @Override
    public PageInfo searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return pageResult;
    }

    @Override
    public ProductDetailVo getProductDetail(Integer productId) {
        if (productId == null) {
            throw CommonExceptions.UserCommonException.PARAMETER_TOKEN_ERROR.getCommonException();
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            throw CommonExceptions.ProductCommonException.PRODUCT_UNSALE_DELETE.getCommonException();
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return productDetailVo;
    }

    @Override
    public PageInfo getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            throw CommonExceptions.UserCommonException.PARAMETER_TOKEN_ERROR.getCommonException();
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return pageInfo;
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId());
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return pageInfo;
    }


}
