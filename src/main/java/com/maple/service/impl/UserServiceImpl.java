package com.maple.service.impl;

import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.UserMapper;
import com.maple.pojo.User;
import com.maple.service.IUserService;
import com.maple.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Maple.Ran on 2017/5/23.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password) {
        int rowResult = userMapper.checkUsername(username);
        if (rowResult == 0) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //将返回给前台的用户信息中去掉密码和问题回答
        user.setPassword(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
        user.setQuestion(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    public ServerResponse register(User user) {
        int rowResult = userMapper.checkUsername(user.getUsername());
        if (rowResult == 0) {
            return ServerResponse.createByErrorMessage("用户名已经存在");
        }
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createByErrorMessage("无法注册管理员账户");
        }
        switch (user.getRole()) {
            case Const.Role.ROLE_SALESMAN:
                user.setRole(Const.Role.ROLE_SALESMAN);
                break;
            case Const.Role.ROLE_FINANCIAL:
                user.setRole(Const.Role.ROLE_FINANCIAL);
                break;
            case Const.Role.ROLE_MANAGER:
                user.setRole(Const.Role.ROLE_MANAGER);
                break;
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        rowResult = userMapper.insert(user);
        if (rowResult == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(str)) {
                int rowResult = userMapper.checkUsername(str);
                if (rowResult > 0) {
                    return ServerResponse.createByErrorMessage("用户已存在");
                }
            }
            if (Const.EMAIL.equals(str)) {
                int rowResult = userMapper.checkEmail(str);
                if (rowResult > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        user.setQuestion(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse resetPassword(User user, String oldPassword, String newPassword) {
        int rowResult = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if (rowResult == 0) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //防止前台更改其他信息
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateResult = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateResult == 0) {
            return ServerResponse.createByErrorMessage("更新密码失败");
        }
        return ServerResponse.createBySuccessMessage("更新面膜成功");
    }

    public ServerResponse updateInformation(User user) {
        //username不能被更新
        //email也要进行校验,是否存在,存在相同,不能是当前用户的

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");

    }

}
