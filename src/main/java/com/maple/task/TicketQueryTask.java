package com.maple.task;

import com.maple.dao.CarMapper;
import com.maple.dao.TicketMapper;
import com.maple.pojo.Car;
import com.maple.pojo.Ticket;
import com.maple.util.WeiCheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Maple.Ran on 2017/10/18.
 */
@Component
public class TicketQueryTask {
    private static final Logger logger = LoggerFactory.getLogger(TicketQueryTask.class);

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private TicketMapper ticketMapper;



    @Scheduled(cron = "0 0 6 1/1 * ?")
    private void weicheQuery() {
        String os = System.getProperties().getProperty("os.name");
        if (os.contains("Mac")) {
            return;
        }

        List<Car> carList = carMapper.selectCarListForTicket();
        for (Car car : carList) {
            Map ticketMap;
            try {
                String vin = car.getVin().substring(car.getVin().length() - 6, car.getVin().length());
                String carEngineNumber = car.getEngineNumber();
                String engineNum = carEngineNumber.substring(carEngineNumber.length() - 6, carEngineNumber.length());
                ticketMap = WeiCheUtil.getTicket("成都", car.getPlateNumber(), vin, engineNum);
            } catch (Exception e) {
                continue;
            }
            String ticketScore = (String) ticketMap.get("ticketScore");
            String ticketMoney = (String) ticketMap.get("ticketMoney");
            String ticketTimes = (String) ticketMap.get("ticketTimes");

            Ticket newTicket = new Ticket();
            newTicket.setCarId(car.getId());
            newTicket.setPlateNum(car.getPlateNumber());
            newTicket.setMoney(Integer.parseInt(ticketMoney));
            newTicket.setScore(Integer.parseInt(ticketScore));
            newTicket.setTicketTimes(Integer.parseInt(ticketTimes));
            ticketMapper.insertSelective(newTicket);
        }
        //删除半个月以前的数据
        ticketMapper.deleteBeforeHalfMonth();

        logger.info("成功完成违章查询");
    }
}
