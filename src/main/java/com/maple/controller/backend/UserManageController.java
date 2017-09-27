package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.pojo.User;
import com.maple.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
@RequestMapping("/manage/user/")
@Controller
public class UserManageController {

    @Autowired
    private IUserService iUserService;
    //注册新用户
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse register(User user,HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser.getRole() == Const.Role.ROLE_ADMIN) {
             return iUserService.register(user);
        }
        return ServerResponse.createByErrorMessage("请使用管理员登录");
    }

}
