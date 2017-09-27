package com.maple.controller.portal;

import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.pojo.User;
import com.maple.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/5/23.
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    //登录接口
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(@RequestBody User user, HttpSession session) {

        ServerResponse<User> response = iUserService.login(user.getUsername(), user.getPassword());
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
            System.out.println(session.getId());
        }
        return response;
    }
    //注销接口
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    //验证重复接口
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse register(String str,String type) {
       return iUserService.checkValid(str,type);//默认角色为业务员
    }

    //获取用户详情接口
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }

        return ServerResponse.createByErrorMessage("未登录,无法获取当前信息");
    }

    //根据现有密码更改密码
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse resetPassword(HttpSession session, String oldPassword, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return iUserService.resetPassword(user,oldPassword,newPassword);
        }
        return ServerResponse.createByErrorMessage("未登录,无法更改密码");
    }

    //强制获取当前登录用户的详细信息,并强制登录
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
             return iUserService.getInformation(user.getId());
        }
        return ServerResponse.createByErrorMessage("未登录,无法获取当前信息");
    }

    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInformation(User user,HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser != null) {
            return iUserService.updateInformation(user);
        }
        return ServerResponse.createByErrorMessage("未登录");
    }
}
