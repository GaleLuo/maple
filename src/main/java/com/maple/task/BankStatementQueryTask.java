package com.maple.task;

import com.maple.common.Const;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPaymentMapper;
import com.maple.pojo.CoModel;
import com.maple.pojo.Driver;
import com.maple.pojo.PeriodPayment;
import com.maple.util.CrawlerUtil;
import com.maple.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/12.
 */
@Component
public class BankStatementQueryTask {
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private CoModelMapper coModelMapper;

    //    @Scheduled(cron = "* */60 * * * ?")
    private void insertPaymentStatement() throws Exception {
        List<Map<String, Object>> maps = CrawlerUtil.bankStatement(new DateTime("2017-06-15").toDate(), new Date());
        for (Map map : maps) {
            String time = (String) map.get("交易时间");
            String No = (String) map.get("交易方账号");
            BigDecimal amount = (BigDecimal) map.get("交易金额");
            String serialNo = (String) map.get("交易流水号");
            Driver driver = null;//根据卡号查询司机

            CoModel coModel = coModelMapper.selectByCarId(driver.getCarId());
            Integer coModelType = coModel.getModelType();
            Date startDate = null;
            Date endDate = null;
            if (coModelType != null && coModelType == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
                startDate = DateTimeUtil.getWeekStartDate(new Date());
                endDate = DateTimeUtil.getWeekEndDate(new Date());
            } else if (coModelType != null && coModelType == Const.CoModel.HIRE_PURCHASE_MONTH.getCode()) {
                startDate = DateTimeUtil.getMonthStartDate(new Date());
                endDate = DateTimeUtil.getMonthEndDate(new Date());
            }
            PeriodPayment newPeriod = new PeriodPayment();
            newPeriod.setDriverId(driver.getId());
            newPeriod.setCarId(driver.getCarId());
            newPeriod.setPayment(amount);
            newPeriod.setPaymentPlatform(Const.PaymentPlatform.pingan.getCode());
            newPeriod.setPlatformNumber(serialNo);
            newPeriod.setPlatformStatus(Const.PlatformStatus.PAID_NORMAL.getCode());
            newPeriod.setPayTime(DateTimeUtil.strToDate(time));
            //查询现有状态,只包含ID和STATUS数据,如果status数据为0,则更新,如果数据大于0则新增,如果数据为Null则不做操作
            PeriodPayment periodPayment = periodPaymentMapper.checkStatus(driver.getId(), serialNo, startDate, endDate);
            Integer result = periodPayment.getPlatformStatus();
            if (result != null && result != 0) {
                periodPaymentMapper.insert(newPeriod);
            } else if (result != null) {
                //如果status=0,则把id赋值给newPayment,并更新
                newPeriod.setId(periodPayment.getId());
                periodPaymentMapper.updateByPrimaryKeySelective(newPeriod);
            }
        }

    }

//    @Scheduled(cron = "0 * * * *")
    private void ticketQuery() {
        System.out.println(new Date());

    }
}


