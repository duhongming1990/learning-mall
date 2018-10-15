package com.mmall.service;

import com.mmall.bean.pojo.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(String categoryName, int parentId);

    Integer updateCategory(Integer categoryId,String categoryName);

    List<Category> getChildrenParallelCategory(Integer categoryId);
    List<Integer> selectCategoryAndChildrenById(Integer categoryId);
    List<Integer> selectCategoryAndChildrenByParentIds(Integer categoryId);

}
