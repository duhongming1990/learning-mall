package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.dao.CategoryMapper;
import com.mmall.bean.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private static final String SUFFIX_CHAT = ".";

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category addCategory(String categoryName, int parentId) {
        if (StringUtils.isBlank(categoryName)) {
            throw CommonExceptions.CategoryCommonException.CATEGORY_PARAMETER_REEOR.getCommonException();
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        categoryMapper.insert(createParentIds(category));
        return category;
    }

    @Override
    public Integer updateCategory(Integer categoryId,String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            throw CommonExceptions.CategoryCommonException.CATEGORY_PARAMETER_REEOR.getCommonException();
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(createParentIds(category));
        return rowCount;
    }

    private Category createParentIds(Category category) {
        Integer parentId = category.getParentId();
        if (parentId == null || parentId == 0) {
            category.setParentIds(String.valueOf(0));
        } else {
            Category currentCategory = categoryMapper.selectByPrimaryKey(parentId);
            category.setParentIds(currentCategory.getParentIds() + SUFFIX_CHAT + parentId);
        }
        return category;
    }


    /**
     * 递归查询本节点的id及孩子节点的id
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Integer> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);


        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return categoryIdList;
    }

    @Override
    public List<Integer> selectCategoryAndChildrenByParentIds(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        return categoryMapper.selectCategoryAndChildrenByParentIds(category);
    }

    @Override
    public List<Category> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
//            logger.info("未找到当前分类的子分类");
        }
        return categoryList;
    }

    /**
     * 递归算法,算出子节点
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        //递归里面查询很危险：1111111111111111111111
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        //递归里面查询很危险：2222222222222222222222
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }

}
