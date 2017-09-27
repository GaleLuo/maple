package com.maple.service.impl;

import ch.qos.logback.core.util.InvocationGate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.CategoryMapper;
import com.maple.pojo.Category;
import com.maple.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Severity;
import java.util.List;
import java.util.Set;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    //给前台返回所有有效根分类
    public ServerResponse<List<Category>> getRootCategoryList() {
        List<Category> categoryList = categoryMapper.selectRootCategoryList();
        return ServerResponse.createBySuccess(categoryList);
    }

    //查询当前分类的子类
    public ServerResponse<List<Category>> getChildrenParallelCategoryList(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            //返回空值
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    //todo 为什么此处只返回id
    public ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        for (Category category : categorySet) {
            categoryIdList.add(category.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);

    }

    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (categoryName == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("增加成功");
        }
        return ServerResponse.createByErrorMessage("增加失败");
    }

    public ServerResponse updateCategory(String categoryName, Integer categoryId) {
        if (categoryName == null || categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    //将所有本类和本类的子类同时装入set集合中

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
