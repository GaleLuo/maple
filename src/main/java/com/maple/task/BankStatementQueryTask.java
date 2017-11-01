package com.maple.task;

import com.maple.common.Const;
import com.maple.dao.AccountMapper;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPaymentMapper;
import com.maple.pojo.Account;
import com.maple.pojo.CoModel;
import com.maple.pojo.Driver;
import com.maple.pojo.PeriodPayment;
import com.maple.util.CrawlerUtil;
import com.maple.util.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
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
    @Autowired
    private AccountMapper accountMapper;

    @Scheduled(cron = "0 0 0/1 * * ?")
    //每小时执行一次
    public void queryKunMingPingAn() throws Exception {
        Date today = new Date();
        List<Map<String, Object>> todayMaps = CrawlerUtil.pingAnStatement(Const.Branch.KM.getCode(),today, today);
        insertPingAn(todayMaps);
    }


    public void insertPingAn(List maps) {
        if (CollectionUtils.isNotEmpty(maps)) {
            for (int i = 0; i < maps.size(); i++) {
                Map map = (Map) maps.get(i);
                String time = (String) map.get("交易时间");
                String accountNo = (String) map.get("交易方账号");
                String name = (String) map.get("交易方姓名");
                BigDecimal amount = (BigDecimal) map.get("交易金额");
                String serialNo = (String) map.get("交易流水号");
                Account account = accountMapper.selectByAccNo(accountNo);
                PeriodPayment newPayment = new PeriodPayment();
                Date payTime = DateTimeUtil.strToDate(time, "yyyy-MM-dd HH:mm:ss");
                if (account == null) {
                    //如果未知交款人
                    newPayment.setPayment(amount);
                    //付款人
                    newPayment.setPayer(name);
                    //付款账号
                    newPayment.setAccountNumber(accountNo);
                    //付款平台
                    newPayment.setPaymentPlatform(Const.PaymentPlatform.pingan.getCode());
                    //平台流水号
                    newPayment.setPlatformNumber(serialNo);
                    //支付状态默认为正常
                    newPayment.setPlatformStatus(Const.PlatformStatus.UNCONFIRMED.getCode());
                    //备注：添加人：系统导入
                    newPayment.setComment("添加人：系统导入");
                    //付款对应日期
                    newPayment.setPayTime(payTime);
                    //付款时间
                    newPayment.setCreateTime(payTime);

                } else {
                    Driver driver = driverMapper.selectByPrimaryKey(account.getDriverId());
                    CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
                    if (coModel.getModelType() == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
                        payTime = DateTimeUtil.getWeekStartDate(payTime);
                    }
                    //平安银行当日数据，只能是起始和结束日期都为当日，否则没有当日数据
                    // 司机id
                    newPayment.setDriverId(account.getDriverId());
                    //车辆id
                    newPayment.setCarId(driver.getCarId());
                    //付款金额
                    newPayment.setPayment(amount);
                    //付款人
                    newPayment.setPayer(name);
                    //付款账号
                    newPayment.setAccountNumber(accountNo);
                    //付款平台
                    newPayment.setPaymentPlatform(Const.PaymentPlatform.pingan.getCode());
                    //平台流水号
                    newPayment.setPlatformNumber(serialNo);
                    //支付状态默认为正常
                    newPayment.setPlatformStatus(Const.PlatformStatus.PAID_NORMAL.getCode());
                    //备注：添加人：系统导入
                    newPayment.setComment("添加人:系统导入");
                    //付款对应日期
                    newPayment.setPayTime(payTime);
                    //付款时间
                    newPayment.setCreateTime(payTime);

                }
                PeriodPayment periodPayment = periodPaymentMapper.selectBySerialNo(serialNo);
                if (periodPayment == null) {
                    periodPaymentMapper.insertSelective(newPayment);
                }
            }
        }
    }

}


