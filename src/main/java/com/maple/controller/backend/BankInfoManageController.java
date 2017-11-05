package com.maple.controller.backend;

import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.pojo.User;
import com.maple.service.IBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Maple.Ran on 2017/11/5.
 */
@Controller
@RequestMapping("/manage/bank_info/")
public class BankInfoManageController {
    @Autowired
    private IBankService iBankService;

    @ResponseBody
    @RequestMapping("pingan_balance_list.do")
    public ServerResponse pinganBalanceList(HttpSession session, Integer branch) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iBankService.pingAnQueryOtherBankBalance(branch);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @ResponseBody
    @RequestMapping("refresh_pingan_balance.do")
    public ServerResponse refreshPinganBalance(HttpSession session, Integer branch,String agreementNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return iBankService.refreshPinganBalance(branch,agreementNo);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

}
