package com.maple.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.maple.common.ServerResponse;
import com.maple.pojo.Product;
import com.maple.vo.ProductDetailVo;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
public interface IProductService {
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> manageProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
