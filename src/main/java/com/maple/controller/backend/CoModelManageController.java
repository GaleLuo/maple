package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.CoModel;
import com.maple.pojo.User;
import com.maple.service.ICoModelService;
import net.sf.jsqlparser.schema.Server;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Maple.Ran on 2017/6/5.
 */
@Controller
@RequestMapping("/manage/co_model/")
public class CoModelManageController {

    @Autowired
    private ICoModelService coModelService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse save(HttpSession session, CoModel coModel) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return coModelService.save(null, coModel);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("add_or_update.do")
    @ResponseBody
    public ServerResponse addOrUpdate(HttpSession session,
                                      Integer driverId,
                                      Integer coModelId,
                                      Integer carId,
                                      Long periodStartDate,
                                      Long periodEndDate,
                                      Integer modelType,
                                      BigDecimal totalAmount,
                                      BigDecimal downAmount,
                                      BigDecimal finalAmount,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodPlanStartDate,
                                      Integer periodNum,
                                      String comment) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return coModelService.addOrUpdate(driverId,coModelId,carId,periodStartDate,periodEndDate,modelType,totalAmount,downAmount,finalAmount,periodPlanStartDate,periodNum,comment);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, CoModel coModel) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return coModelService.update(coModel);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("summary.do")
    @ResponseBody
    public ServerResponse summary(HttpSession session, Integer coModelId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return coModelService.summary(coModelId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
}
