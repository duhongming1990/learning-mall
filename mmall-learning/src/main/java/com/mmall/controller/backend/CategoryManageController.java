package com.mmall.controller.backend;

import com.mmall.bean.pojo.Category;
import com.mmall.common.response.ResultBean;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/21 12:40
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加商品类别
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @PostMapping("/add_category")
    public ResultBean<Category> addCategory(
            @RequestParam(value = "categoryName") String categoryName,
            @RequestParam(value = "parentId", defaultValue = "0") int parentId
    ) {
        Category category = iCategoryService.addCategory(categoryName, parentId);
        return new ResultBean<>(category);
    }

    /**
     * 更新商品类别
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @PostMapping("/set_category_name")
    public ResultBean<String> updateCategory(
            @RequestParam(value = "categoryId") Integer categoryId,
            @RequestParam(value = "categoryName") String categoryName) {
        iCategoryService.updateCategory(categoryId, categoryName);
        return new ResultBean<>();

    }

    /**
     * 查询子节点的category信息,并且不递归,保持平级
     * @param categoryId
     * @return
     */
    @GetMapping("/get_category")
    public ResultBean<List<Category>> getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        List<Category> categories = iCategoryService.getChildrenParallelCategory(categoryId);
        return new ResultBean<>(categories);
    }

    /**
     * 查询当前节点的id和递归子节点的id
     * 0->10000->100000
     * @param categoryId
     * @return
     */
    @GetMapping("/get_deep_category")
    public ResultBean<List<Integer>> getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        List<Integer> categoryIdList = iCategoryService.selectCategoryAndChildrenById(categoryId);
        return new ResultBean<>(categoryIdList);
    }

    /**
     * 查询当前节点的id和不用递归方式获取子节点的id
     * 0->10000->100000
     * @param categoryId
     * @return
     */
    @GetMapping("/get_deep_category_plus")
    public ResultBean<List<Integer>> getCategoryAndDeepChildrenCategoryPlus(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        List<Integer> categoryIdList = iCategoryService.selectCategoryAndChildrenByParentIds(categoryId);
        return new ResultBean<>(categoryIdList);
    }
}
