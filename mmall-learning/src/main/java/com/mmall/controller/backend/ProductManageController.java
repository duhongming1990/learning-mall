package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.response.ResultBean;
import com.mmall.bean.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.util.PropertiesUtil;
import com.mmall.bean.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/21 12:46
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * @param product
     * @return
     */
    @PostMapping("/save")
    public ResultBean productSave(Product product) {
        iProductService.saveOrUpdateProduct(product);
        return new ResultBean<>();
    }

    /**
     * 商品状态
     *
     * @param productId
     * @param status
     * @return
     */
    @PostMapping("/set_sale_status")
    public ResultBean setSaleStatus(Integer productId, Integer status) {
        iProductService.setSaleStatus(productId, status);
        return new ResultBean<>();
    }

    /**
     * 产品详情
     *
     * @param productId
     * @return
     */
    @GetMapping("/detail")
    public ResultBean<ProductDetailVo> getDetail(Integer productId) {
        ProductDetailVo productDetailVo = iProductService.manageProductDetail(productId);
        return new ResultBean<>(productDetailVo);
    }

    /**
     * 商品列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResultBean<PageInfo> getList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo pageInfo = iProductService.getProductList(pageNum, pageSize);
        return new ResultBean<>(pageInfo);
    }

    /**
     * 商品搜索
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/search")
    public ResultBean<PageInfo> productSearch(
            String productName, Integer productId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo pageInfo = iProductService.searchProduct(productName, productId, pageNum, pageSize);
        return new ResultBean<>(pageInfo);
    }

    /**
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public ResultBean<Map> upload(
            @RequestParam(value = "upload_file", required = false) MultipartFile file,
            HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return new ResultBean<>(fileMap);
    }


    /**
     * @param file
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/richtext_img_upload")
    public Map richtextImgUpload(
            @RequestParam(value = "upload_file", required = false) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }


}
