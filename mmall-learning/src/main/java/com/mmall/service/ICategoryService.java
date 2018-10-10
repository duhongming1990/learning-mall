package com.mmall.service;

import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(String categoryName, int parentId);

    Integer updateCategory(Category category);

    List<Category> getChildrenParallelCategory(Integer categoryId);
    List<Integer> selectCategoryAndChildrenById(Integer categoryId);
}
