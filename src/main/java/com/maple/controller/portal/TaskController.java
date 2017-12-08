package com.maple.controller.portal;

import com.maple.task.AddTask;
import com.maple.task.PaymentQueryTask;
import com.maple.task.TicketQueryTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Maple.Ran on 2017/12/8.
 */
@Controller
@RequestMapping("/task/")
public class TaskController {
    @Autowired
    private AddTask addTask;
    @Autowired
    private PaymentQueryTask paymentQueryTask;
    @Autowired
    private TicketQueryTask ticketQueryTask;

    @RequestMapping("add_task.do")
    private void addTask() {
        addTask.addDriver();
    }

    @RequestMapping("payment_query.do")
    private void paymentQuery() {
        paymentQueryTask.queryPingAn();
    }

    @RequestMapping("ticket_query.do")
    private void ticketQuery(){
        ticketQueryTask.weicheQuery();
    }

}
