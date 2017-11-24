package com.maple.task;

import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.AccountMapper;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPaymentMapper;
import com.maple.pojo.Account;
import com.maple.pojo.CoModel;
import com.maple.pojo.Driver;
import com.maple.pojo.PeriodPayment;
import com.maple.service.IBankService;
import com.maple.service.impl.BankServiceImpl;
import com.maple.util.AliQueryUtil;
import com.maple.util.DateTimeUtil;
import com.maple.util.JsonUtil;
import org.apache.bcel.classfile.Code;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/12.
 */
@Component
public class PaymentQueryTask {
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IBankService iBankService;

    @Scheduled(cron = "0 10 0/1 * * ?")
    public void queryPingAn() throws Exception {
        String os = System.getProperties().getProperty("os.name");
        if (os.contains("Mac")) {
            return;
        }

        Date today = new Date();
        Date weekStartDate = DateTimeUtil.getWeekStartDate(today);
        //成都数据
        iBankService.bankLogin(Const.Branch.CD.getCode());
        //请求当天数据
        List<Map<String, Object>> cdToday = iBankService.statement(today, today, Const.Branch.CD.getCode());
        //请求本周数据
        List<Map<String, Object>> cdWeek = iBankService.statement(weekStartDate, today, Const.Branch.CD.getCode());
        if (CollectionUtils.isNotEmpty(cdToday)) {
            insertByList(cdToday);
        }

        if (CollectionUtils.isNotEmpty(cdWeek)) {
            insertByList(cdWeek);
        }
        //关闭连接
        iBankService.closeConnection();

        //昆明
        iBankService.bankLogin(Const.Branch.KM.getCode());
        //请求当天数据
        List<Map<String, Object>> kmToday = iBankService.statement(today, today, Const.Branch.KM.getCode());
        //请求本周数据
        List<Map<String, Object>> kmWeek = iBankService.statement(weekStartDate, today, Const.Branch.KM.getCode());
        if (CollectionUtils.isNotEmpty(kmToday)) {
            insertByList(kmToday);
        }

        if (CollectionUtils.isNotEmpty(kmWeek)) {
            insertByList(kmWeek);
        }
        //关闭连接
        iBankService.closeConnection();

    }


    public void queryAlipay() {
            Date today = new Date();
            Date start = new DateTime().plusDays(-3).toDate();
            String jsonStr = AliQueryUtil.getJsonStr(start, today);
            List list = JsonUtil.alipayBalance(jsonStr);
            for (Object o : list) {
                Map detail = (Map) o;
                String time = (String) detail.get("tradeTime");
                String serialNo = (String) detail.get("tradeNo");
                String payer = (String) detail.get("otherAccountFullname");
                String accountNo = (String) detail.get("otherAccountEmail");
                String amountStr = (String) detail.get("tradeAmount");
                BigDecimal amount = new BigDecimal(amountStr);
                String comment = (String) detail.get("transMemo");
                PeriodPayment periodPayment = periodPaymentMapper.selectBySerialNo(serialNo);
                if (periodPayment == null&&amount.compareTo(BigDecimal.ZERO)>0) {
                    PeriodPayment newPayment = assemblePeriodPayment(time, serialNo, payer, accountNo, comment, amount, Const.PaymentPlatform.alipay.getCode());
                    periodPaymentMapper.insertSelective(newPayment);
                }
            }
    }

    private void insertByList(List data) {
        for (Object o : data) {
            Map map = (Map) o;
            String time = (String) map.get("交易时间");
            String accountNo = (String) map.get("交易方账号");
            String payer = (String) map.get("交易方姓名");
            BigDecimal amount = (BigDecimal) map.get("交易金额");
            String serialNo = (String) map.get("交易流水号");
            String comment = (String) map.get("备注");
            PeriodPayment periodPayment = periodPaymentMapper.selectBySerialNo(serialNo);
            if (periodPayment == null&&amount.compareTo(BigDecimal.ZERO)>0) {
                PeriodPayment newPayment = assemblePeriodPayment(time, serialNo, payer, accountNo, comment, amount, Const.PaymentPlatform.pingan.getCode());
                periodPaymentMapper.insertSelective(newPayment);
            }
        }
    }


    private PeriodPayment assemblePeriodPayment(String time,String serialNo,String payer,String accountNo,String comment,
                                                BigDecimal amount,Integer platformCode) {
                Account account = accountMapper.selectByAccNo(accountNo);
                PeriodPayment newPayment = new PeriodPayment();
                Date payTime = DateTimeUtil.strToDate(time, "yyyy-MM-dd HH:mm:ss");
                if (account == null) {
                    //如果未知交款人
                    newPayment.setPayment(amount);
                    //付款账号
                    newPayment.setAccountNumber(accountNo);
                    //付款平台
                    newPayment.setPaymentPlatform(platformCode);
                    //平台流水号
                    newPayment.setPlatformNumber(serialNo);
                    //支付状态默认为未确认
                    newPayment.setPlatformStatus(Const.PlatformStatus.UNCONFIRMED.getCode());
                    //备注：添加人：系统导入
                    newPayment.setComment( "添加人：系统导入");
                    if (payer.contains("支付宝")) {
                        //付款人变为格式comment+支付宝
                        //支付宝姓名
                        String zfbName = comment.substring(0, comment.indexOf("支付宝转账"));
                        payer = zfbName + "-支付宝转入";
                    }
                    //付款人
                    newPayment.setPayer(payer);
                    //付款对应日期
                    newPayment.setPayTime(payTime);
                    //付款时间
                    newPayment.setCreateTime(payTime);

                } else {
                    Driver driver = driverMapper.selectByPrimaryKey(account.getDriverId());
                    CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
                    Date weekStartDate = new Date();
                    if (coModel.getModelType() == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
                        weekStartDate = DateTimeUtil.getWeekStartDate(payTime);
                    }
                    //平安银行当日数据，只能是起始和结束日期都为当日，否则没有当日数据
                    // 司机id
                    newPayment.setDriverId(account.getDriverId());
                    //车辆id
                    newPayment.setCarId(driver.getCarId());
                    //付款金额
                    newPayment.setPayment(amount);
                    //付款人
                    newPayment.setPayer(payer);
                    //付款账号
                    newPayment.setAccountNumber(accountNo);
                    //付款平台
                    newPayment.setPaymentPlatform(platformCode);
                    //平台流水号
                    newPayment.setPlatformNumber(serialNo);
                    //支付状态默认为正常
                    newPayment.setPlatformStatus(Const.PlatformStatus.PAID_NORMAL.getCode());
                    //备注：添加人：系统导入
                    newPayment.setComment("添加人:系统导入");
                    //付款对应日期
                    newPayment.setPayTime(weekStartDate);
                    //付款时间
                    newPayment.setCreateTime(payTime);

                }
        return newPayment;
    }

}


