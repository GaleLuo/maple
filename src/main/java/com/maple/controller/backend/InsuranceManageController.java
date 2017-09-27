package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.Insurance;
import com.maple.pojo.User;
import com.maple.service.IInsuranceService;
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
@RequestMapping("/manage/insurance/")
public class InsuranceManageController {

    @Autowired
    private IInsuranceService iInsuranceService;

    //保存+更新
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse save(HttpSession session, Insurance insurance) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iInsuranceService.saveOrUpdateInsurance(insurance);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //列表+查询
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "carId",required = false)Integer carId,
                               @RequestParam(value = "insuranceType",required = false)Integer insuranceType,
                               @RequestParam(value = "companyName",required = false) String companyName,
                               @RequestParam(value = "createDateT",required = false) String createDateT,
                               @RequestParam(value = "createDateB",required = false)String createDateB,
                               @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iInsuranceService.list(null,carId, insuranceType, companyName, createDateT, createDateB, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
}
