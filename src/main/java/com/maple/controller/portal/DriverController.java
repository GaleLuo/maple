package com.maple.controller.portal;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.Driver;
import com.maple.pojo.User;
import com.maple.service.IDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/5/26.
 */
@Controller
@RequestMapping("/driver/")
public class DriverController {

    @Autowired
    private IDriverService iDriverService;


    //创建或更新司机信息
    @RequestMapping("save_driver.do")
    @ResponseBody
    public ServerResponse saveDriver(HttpSession session, Driver driver) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iDriverService.saveDriver(user.getId(), driver);
    }

    //设置为无意向客户
    @RequestMapping("set_potential_free.do")
    @ResponseBody
    public ServerResponse saveDriver(HttpSession session, String driverIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iDriverService.setDriverStatus(user.getId(), driverIds,Const.DriverStatus.POTENTIAL_FREE.getCode());
    }

    //删除,只能删除状态为意向客户和无意向的状态
    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session, String driverIds,
                                 @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iDriverService.delete(user.getId(), driverIds,pageNum,pageSize);
    }

    //返回司机列表
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               String driverName,
                               String phoneNum,
                               Integer driverStatus,
                               Integer coModelType,
                               String orderBy,
                               @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iDriverService.list(user.getId(), driverName, phoneNum, driverStatus, coModelType, orderBy, pageNum, pageSize);
    }

    //搜索
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse search(HttpSession session,
                                 @RequestParam(value = "createDateTop", required = false) String createDateTop,
                                 @RequestParam(value = "createDateBut", required = false) String createDateBut,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10")int pageSize
                                 ) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iDriverService.search(user.getId(),createDateTop,createDateBut,name,status,pageNum, pageSize);
    }

    //详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Integer driverId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iDriverService.detail(user.getId(),driverId);
    }



}
