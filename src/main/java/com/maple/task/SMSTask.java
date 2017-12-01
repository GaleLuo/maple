package com.maple.task;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.maple.common.Const;
import com.maple.dao.CarMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.TicketMapper;
import com.maple.pojo.Car;
import com.maple.pojo.Driver;
import com.maple.pojo.Ticket;
import com.maple.util.SmsUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/10/29.
 */
@Component
public class SMSTask {
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private TicketMapper ticketMapper;

//    @Scheduled(cron = "0 0 8 ? * MON")
    private void smsSend() {
        String os = System.getProperties().getProperty("os.name");
        if (os.contains("Mac")) {
            return;
        }

        //将所有正常运营的司机电话状态设为待定
        driverMapper.updateNormalDriverPhoneStatusToUnconfirmed();

        List<Driver> driverList = driverMapper.selectDriverListByStatusAndBranch(Const.DriverStatus.NORMAL_DRIVER.getCode(),Const.Branch.CD.getCode());
        for (int i = 0; i<driverList.size();i++) {
            Driver driver = driverList.get(i);
            Car car = carMapper.selectByPrimaryKey(driver.getCarId());
            Ticket ticket = ticketMapper.selectByCarId(driver.getCarId());
            try {
                SmsUtil.sendSms(driver.getPersonalPhone(), driver.getName(), car.getPlateNumber(), ticket.getScore().toString(), ticket.getMoney().toString());
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

//    @Scheduled(cron = "0 0 * * * ? ")
    private void querySms() {
        //设置为每个星期一早上8点
        DateTime dateTime = new DateTime().withDayOfWeek(1).millisOfDay().withMinimumValue().withHourOfDay(8);
        Date date = dateTime.toDate();
        List<Driver> driverList = driverMapper.selectDriverListByPhoneStatusNotNormal();
        Driver newDriver = new Driver();
        for (Driver driver : driverList) {
            try {
                QuerySendDetailsResponse querySendDetailsResponse = SmsUtil.querySendDetails(driver.getPersonalPhone(), date);
                for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {

                    Long sendStatus = smsSendDetailDTO.getSendStatus();
                    newDriver.setId(driver.getId());
                    if (sendStatus == 3) {//已发送
                        newDriver.setPhoneStatus(1);//正常
                    } else if (sendStatus == 1) {//已发送,待收
                        newDriver.setPhoneStatus(2);
                    } else if (sendStatus == 2) {//发送失败
                        newDriver.setPhoneStatus(0);//失效
                    }
                    driverMapper.updateByPrimaryKeySelective(newDriver);
                }
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
}
