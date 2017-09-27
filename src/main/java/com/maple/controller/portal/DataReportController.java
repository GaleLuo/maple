package com.maple.controller.portal;

import com.google.common.collect.Maps;
import com.maple.service.IDataReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/23.
 */
@Controller
@RequestMapping("/data_report/")
public class DataReportController {

    @Autowired
    private IDataReportService iDataReportService;

    @RequestMapping("finish_order.do")
    @ResponseBody
    public Map finishOrder(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String phone,
                           @RequestParam("start_time") String startTime,
                           @RequestParam("end_time") String end_time,
                           @RequestParam(value = "start",defaultValue = "0")int pageStart,
                           @RequestParam(value = "length",defaultValue = "30")int pageSize, HttpSession session) {
        Map resultMap = Maps.newHashMap();
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if (user == null) {
//            resultMap.put("status", "1");
//            resultMap.put("msg", "请登录后操作");
//            return resultMap;
//        }
        System.out.println("name:" + name);
        System.out.println("phone:"+phone);
        return iDataReportService.finishOrderReport(null,null,"2017-05-20","2017-05-20",pageStart,pageSize);

    }


}
