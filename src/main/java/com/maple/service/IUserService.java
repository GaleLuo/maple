package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.User;

/**
 * Created by Maple.Ran on 2017/5/23.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse register(User user);

    ServerResponse checkValid(String str, String type);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse resetPassword(User user, String oldPassword, String newPassword);

    ServerResponse updateInformation(User user);
}
