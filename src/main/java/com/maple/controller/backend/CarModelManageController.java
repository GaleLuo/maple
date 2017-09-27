package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.CoModel;
import com.maple.pojo.User;
import com.maple.service.ICoModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/6/5.
 */
@Controller
@RequestMapping("/manage/co_model/")
public class CarModelManageController {

    @Autowired
    private ICoModelService iCoModelService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse save(HttpSession session, CoModel coModel) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return ServerResponse.createByErrorMessage("无权限");
        }
        return iCoModelService.save(null, coModel);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, CoModel coModel) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return ServerResponse.createByErrorMessage("无权限");
        }
        return iCoModelService.update(coModel);
    }
}
