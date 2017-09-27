package com.maple.dao;

import com.maple.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdAndProductIdList(@Param("userId")Integer userId, @Param("productIdList")List<String> productIdList);

    int CheckOrUnCheck(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checkStatus") Integer checkStatus);

    int selectProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);

}