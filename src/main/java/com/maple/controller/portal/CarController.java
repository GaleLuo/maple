package com.maple.controller.portal;

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
 * Created by Maple.Ran on 2017/6/1.
 */
@Controller
@RequestMapping("/car/")
public class CarController {
    @Autowired
    private ICarService iCarService;

    //新增成功会返回新增车辆的id
    //根据产品id生成新的车辆
    //todo 只能更新空值
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse save(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCarService.save(productId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Car car) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCarService.update(user.getId(),car);
    }


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
        return iCarService.list(user.getId(), driverName, plateNumber, name, createDateTop, createDateBut, pageNum, pageSize);
    }

}