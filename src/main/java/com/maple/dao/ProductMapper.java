package com.maple.dao;

import com.maple.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByNameAndCategoryIdList(@Param("keyword") String keyword, @Param("categoryIdList") List<Integer> categoryIdList);

    List<Product> selectAllProduct();

    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);
}