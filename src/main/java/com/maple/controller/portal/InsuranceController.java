package com.maple.controller.portal;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.User;
import com.maple.service.IInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/5/31.
 */
@Controller
@RequestMapping("/insurance/")
public class InsuranceController {

    @Autowired
    private IInsuranceService iInsuranceService;

    //查询保险列表
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
        return iInsuranceService.list(user.getId(),carId,insuranceType,companyName,createDateT,createDateB,pageNum,pageSize);
    }
}
