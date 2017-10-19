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
        List<Car> carList = carMapper.selectCarListForTicket();
//        List<Car> carList = carMapper.selectWhereUnchecked();
        System.out.println("共"+carList.size()+"待查车辆");
        for (int i =0;i<carList.size();i++){
//        Car car = carMapper.selectByPrimaryKey(100046);
            System.out.println("第"+(i+1)+"车辆正在查询"+"车牌号:"+carList.get(i).getPlateNumber());
            Car car = carList.get(i);
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
            System.out.println("扣分: " + ticketScore+"罚金: " + ticketMoney);

            Ticket newTicket = new Ticket();
            newTicket.setCarId(car.getId());
            newTicket.setPlateNum(car.getPlateNumber());
            newTicket.setMoney(Integer.parseInt(ticketMoney));
            newTicket.setScore(Integer.parseInt(ticketScore));
            newTicket.setTicketTimes(Integer.parseInt(ticketTimes));
            Ticket ticketResult = ticketMapper.selectByCarId(car.getId());

            if (ticketResult == null) {
                ticketMapper.insert(newTicket);
            } else {
                newTicket.setId(ticketResult.getId());
                Integer newScore = newTicket.getScore() - ticketResult.getScore();
                Integer newMoney = newTicket.getMoney() - ticketResult.getMoney();
                if ( newScore> 0) {
                    System.out.println(ticketResult.getPlateNum()+ "-新增分数:"+newScore);
                }
                if ( newMoney> 0) {
                    System.out.println(ticketResult.getPlateNum()+ "-新增罚款:"+newMoney);
                }
                ticketMapper.updateByPrimaryKeySelective(newTicket);
            }
        }
        logger.info("成功完成违章查询");
    }
}
