package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.Car;
import com.maple.pojo.User;
import com.maple.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/6/4.
 */
@Controller
@RequestMapping("/manage/car/")
public class CarMangeController {

    @Autowired
    private ICarService iCarService;
    //更新任意司机数据
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Car car) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            iCarService.update(null, car);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
    //列出所有车辆数据
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "createDateTop", required = false) String createDateTop,
                               @RequestParam(value = "createDateBut", required = false) String createDateBut,
                               @RequestParam(value = "driverName",required = false) String driverName,
                               @RequestParam(value = "plateNumber" ,required = false) String plateNumber,//车牌号
                               @RequestParam(value = "name", required = false) String name,//车型
                               @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return ServerResponse.createByErrorMessage("无权限");
        }
        return iCarService.list(null, driverName, plateNumber, name, createDateTop, createDateBut, pageNum, pageSize);
    }
}
