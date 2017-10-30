package com.maple.dao;

import com.maple.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param(value = "username") String username, @Param(value = "password") String password);

    int checkPassword(@Param(value = "password")String password, @Param(value = "userId")Integer userId);

}