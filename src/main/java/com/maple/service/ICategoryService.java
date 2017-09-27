package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.Category;

import java.util.List;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
public interface ICategoryService {
    ServerResponse<List<Category>> getChildrenParallelCategoryList(Integer categoryId);

    ServerResponse<List<Category>> getRootCategoryList();

    ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId);

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategory(String categoryName, Integer categoryId);
}
