package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.User;
import com.maple.service.IPeriodPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/20.
 */
@Controller
@RequestMapping("/manage/period_payment/")
public class PeriodPaymentManageController {


    @Autowired
    private IPeriodPaymentService iPeriodPaymentService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse addOrUpdate(HttpSession session,
                                      Boolean addToAccount,
                                      Integer paymentId,
                                      Integer driverId,
                                      BigDecimal payment,
                                      String payer,
                                      Integer paymentPlatform,
                                      String platformNum,
                                      Long payTime,
                                      String comment) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.addOrUpdate(user,addToAccount,paymentId, driverId, payment, payer,paymentPlatform,platformNum, payTime,comment);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
    //周/月详情
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               Integer branch,
                               @RequestParam(required = false)String date,
                               @RequestParam(required = false)String driverName,
                               @RequestParam(required = false)Integer coModelType,//合作模式代码
                               @RequestParam(required = false)Integer payStatus,//支付状态代码
                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize
    ) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.list(branch,date,driverName,coModelType,payStatus,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
    //报表总览
    @RequestMapping("general_list.do")
    @ResponseBody
    public ServerResponse generalList(HttpSession session,
                                      Integer branch,
                                      Long startDate,
                                      Long endDate,
                                      Integer coModelType,
                                      @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.generalList(branch,startDate,endDate,coModelType,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //具体付款情况渠道
    @RequestMapping("current_payment_detail.do")
    @ResponseBody
    public ServerResponse currentPaymentDetail(HttpSession session,
                                               Integer driverId,
                                               String date) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.currentPaymentDetail(driverId,date);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //个人所有付款情况 periodDetailVoList
    @RequestMapping("driver_total_payment.do")
    @ResponseBody
    public ServerResponse allPaymentDetail(HttpSession session,
                                           Integer driverId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.driverTotalPayment(driverId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }


    @RequestMapping("payment_list.do")
    @ResponseBody
    public ServerResponse paymentList(HttpSession session,
                                      Integer branch,
                                      Long startTime,
                                      Long endTime,
                                      String driverName,
                                      Integer coModelType,
                                      String payer,
                                      Integer platformStatus,
                                      Integer paymentPlatform,
                                      int pageNum,
                                      int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.paymentList(branch,startTime,endTime,driverName,coModelType,payer,platformStatus,paymentPlatform,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("update_payment_status.do")
    @ResponseBody
    // 后端接收数组的格式如下
    public ServerResponse updatePaymentStatus(HttpSession session,
                                              @RequestParam(value = "paymentIdArray[]") Integer[] paymentIdArray,
                                              Integer paymentStatus) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.PRIMARY_PERMISSION.contains(user.getRole())) {
            return iPeriodPaymentService.updatePaymentStatus(paymentIdArray, paymentStatus);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
}
