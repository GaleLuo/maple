package com.maple.task;

import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Maple.Ran on 2017/7/13.
 * 已经不需要!!!!!!!!!!!!!!
 */
@Component
public class PeriodPaymentGeneratorTask {
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    //每月1号的0点生成
//    @Scheduled(cron="0 0 0 1 * ? ")
    public void month() {
    }

    //每周星期二的1点生成
//    @Scheduled(cron = "0 0 1 ? * TUE ")
    public void week() {
    }
}
