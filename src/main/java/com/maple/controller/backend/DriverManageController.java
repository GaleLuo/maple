package com.maple.controller.backend;

import com.google.common.collect.Maps;
import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.DriverMapper;
import com.maple.pojo.Driver;
import com.maple.pojo.User;
import com.maple.service.IDriverService;
import com.maple.service.IFileService;
import com.maple.util.PropertiesUtil;
import com.sun.tools.corba.se.idl.InterfaceGen;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.lwawt.PlatformEventNotifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/5/31.
 */
@Controller
@RequestMapping("/manage/driver/")
public class DriverManageController {


    @Autowired
    private IDriverService driverService;
    @Autowired
    private IFileService fileService;
    //创建或更新司机信息
    @RequestMapping("add_or_update.do")
    @ResponseBody
    public ServerResponse addOrOUpdate(HttpSession session, Driver driver) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.addOrUpdate(user.getId(), driver);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //更改司机状态
    @RequestMapping("set_status.do")
    @ResponseBody
    public ServerResponse setStatus(HttpSession session,String driverIds,Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.manageDriverStatus(driverIds,status);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //返回所有司机列表
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               String plateNum,
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
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.list(null,plateNum,driverName,phoneNum,driverStatus,coModelType,orderBy, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //按照条件搜索司机
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
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.search(null,createDateTop,createDateBut,name,status,pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    //司机详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Integer driverId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.detail(null,driverId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("summary.do")
    @ResponseBody
    public ServerResponse summary(HttpSession session, Integer driverId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.summary(null,driverId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
    //上传图片
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(MultipartFile file, HttpServletRequest request, HttpSession session,String driverIdNum) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path, "driver/" + driverIdNum);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("add_account.do")
    @ResponseBody
    public ServerResponse addAccount(HttpSession session, Integer driverId, String name,Integer platformCode, String account) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.addAccount(driverId, name,platformCode, account);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("get_account_list.do")
    @ResponseBody
    public ServerResponse getAccountList(HttpSession session, Integer driverId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.getAccountList(driverId);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }

    @RequestMapping("name_list.do")
    @ResponseBody
    public ServerResponse nameList(HttpSession session, String driverName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Permission.NORMAL_PERMISSION.contains(user.getRole())) {
            return driverService.nameList(driverName);
        }
        return ServerResponse.createByErrorMessage("无权限");
    }
}
