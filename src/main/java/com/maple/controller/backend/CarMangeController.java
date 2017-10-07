package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.User;
import com.maple.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by Maple.Ran on 2017/6/4.
 */
@Controller
@RequestMapping("/manage/car/")
public class CarMangeController {

    @Autowired
    private ICarService carService;
    //更新任意司机数据
    @RequestMapping("add_or_update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session,
                                 @RequestParam(value = "id",required = false)Integer id,
                                 @RequestParam(value = "branch",required = false)Integer branch,
                                 @RequestParam(value = "carStatus",required = false)Integer carStatus,
                                 @RequestParam(value = "name",required = false)String name,
                                 @RequestParam(value = "plateNumber",required = false)String plateNumber,
                                 @RequestParam(value = "engineNumber",required = false)String engineNumber,
                                 @RequestParam(value = "vin",required = false)String vin,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd")
                                 @RequestParam(value = "pickDate",required = false)Date pickDate,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd")
                                 @RequestParam(value = "transferDate",required = false)Date transferDate,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd")
                                 @RequestParam(value = "redeemDate",required = false)Date redeemDate,
                                 @RequestParam(value = "gpsNumber",required = false)String gpsNumber,
                                 @RequestParam(value = "gpsPhone",required = false)String gpsPhone
                                 ) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
           return carService.addOrUpdate(null, id,branch,carStatus,name,plateNumber,engineNumber,vin,pickDate,transferDate,redeemDate,gpsNumber,gpsPhone);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
    //列出所有车辆数据
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "driverName",required = false) String driverName,
                               @RequestParam(value = "branch",required = false) Integer branch,
                               @RequestParam(value = "carStatus",required = false) Integer carStatus,
                               @RequestParam(value = "plateNum" ,required = false) String plateNum,//车牌号
                               @RequestParam(value = "carName", required = false) String carName,//车型
                               @RequestParam(value = "orderBy", required = false) String orderBy,//排序
                               @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return carService.list(null,driverName,branch,carStatus, plateNum, carName,orderBy, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("summary.do")
    @ResponseBody
    public ServerResponse summary(HttpSession session, Integer carId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return carService.summary(carId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("bind.do")
    @ResponseBody
    public ServerResponse bind(HttpSession session, Integer driverId, Integer carId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return carService.bind(driverId,carId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
}
