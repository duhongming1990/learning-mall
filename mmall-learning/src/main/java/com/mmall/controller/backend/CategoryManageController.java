package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.common.response.ResultBean;
import com.mmall.bean.pojo.Category;
import com.mmall.bean.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加商品类别
     *
     * @param session
     * @param name
     * @param parentId
     * @return
     */
    @PostMapping("/add_category")
    public ResultBean<Category> addCategory(HttpSession session,
                                          String name,
                                          @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iUserService.checkAdminRole(user);
        Category category = iCategoryService.addCategory(name, parentId);
        return new ResultBean<>(category);
    }

    /**
     * 更新商品类别
     *
     * @param session
     * @param category
     * @return
     */
    @PostMapping("/set_category_name")
    public ResultBean<String> updateCategory(HttpSession session, Category category) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iUserService.checkAdminRole(user);
        iCategoryService.updateCategory(category);
        return new ResultBean<>();

    }

    @GetMapping("/get_category")
    public ResultBean<List<Category>> getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iUserService.checkAdminRole(user);
        //查询子节点的category信息,并且不递归,保持平级
        return new ResultBean<>(iCategoryService.getChildrenParallelCategory(categoryId));
    }

    @RequestMapping("/get_deep_category")
    public ResultBean<List<Integer>> getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iUserService.checkAdminRole(user);
        //查询当前节点的id和递归子节点的id
//            0->10000->100000
        return new ResultBean<>(iCategoryService.selectCategoryAndChildrenById(categoryId));
    }
}
