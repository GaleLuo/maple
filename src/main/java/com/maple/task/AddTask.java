package com.maple.task;

import com.maple.common.Const;
import com.maple.dao.CarMapper;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPlanMapper;
import com.maple.pojo.Car;
import com.maple.pojo.CoModel;
import com.maple.pojo.Driver;
import com.maple.pojo.PeriodPlan;
import com.maple.util.DateTimeUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/11/22.
 */
@Component
public class AddTask {
    private final static String MAC_PATH = "/Users/Maple.Ran/Downloads/2017-时利和司机详细信息表（最新）.xlsx";
    private final static String LINUX_PATH = "/data/excel/2017-时利和司机详细信息表（最新）.xlsx";


    @Autowired
    private CarMapper carMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private PeriodPlanMapper periodPlanMapper;

    @Scheduled(cron = "0 10 19 * * ?")
    public void addDriver() {
        Workbook sheets = null;
        try {
            sheets = new XSSFWorkbook(new FileInputStream(new File(LINUX_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = sheets.getSheetAt(0);
        int startRow = 0;
        for (int i =310;i<=sheet.getLastRowNum();i++) {
            Row row = sheet.getRow(i);
            String idNo = row.getCell(6).getStringCellValue();
            Driver driver = driverMapper.selectByDriverIdNumber(idNo);
            if (driver == null) {
                startRow = i;
                break;
            }
        }

        if (startRow == 0) {
            return;
        }

        System.out.println("---------------------"+"开始行:"+startRow+"-------------------");

        for (int i = startRow; i<=sheet.getLastRowNum();i++) {
            Row row = sheet.getRow(i);
            String driverName = row.getCell(1).getStringCellValue();
            String phone = row.getCell(2).getStringCellValue();
            String carName = row.getCell(4).getStringCellValue();
            String plateNo = row.getCell(5).getStringCellValue();
            String idNo = row.getCell(6).getStringCellValue();
            String vin = row.getCell(11).getStringCellValue();
            String engineNo = row.getCell(12).getStringCellValue();
//            String manIns = row.getCell(15).getStringCellValue();
//            double manInsAmount = row.getCell(16).getNumericCellValue();
//            Date manInsExpireDate = row.getCell(17).getDateCellValue();
//            String manInsNo= row.getCell(18).getStringCellValue();
//            String comIns = row.getCell(19).getStringCellValue();
//            double comInsAmount = row.getCell(20).getNumericCellValue();
//            Date comInsExpireDate = row.getCell(21).getDateCellValue();
//            String comInsNo= row.getCell(22).getStringCellValue();
            Date startDate = row.getCell(27).getDateCellValue();
            System.out.println(startDate);
            Date endDate = row.getCell(28).getDateCellValue();
            Double downAmount = row.getCell(32).getNumericCellValue();
            Double payment = row.getCell(33).getNumericCellValue();
            Double month = row.getCell(34).getNumericCellValue();
            String modelType = row.getCell(35).getStringCellValue();
            Double totalAmount = row.getCell(36).getNumericCellValue();
            Date pickDate = row.getCell(42).getDateCellValue();

            Integer carId = insertCar(carName, plateNo, vin, engineNo, pickDate);
            if (carId == null) {
                continue;
            }
            Integer coModelId = insertCoModel(modelType, startDate, endDate, downAmount, payment, month, totalAmount);
            insertDriver(driverName, phone, idNo, carId, coModelId);
        }

    }

    private Integer insertCar(String carName, String plateNo, String vin, String engineNo, Date pickDate) {
        Car car = carMapper.selectByVin(vin);
        if (car == null) {//车辆不存在
            //新建车辆
            car = new Car();
            car.setName(carName);
            car.setPlateNumber(plateNo);
            car.setVin(vin);
            car.setEngineNumber(engineNo);
            car.setPickDate(pickDate);
            car.setCarStatus(Const.CarStatus.NORMAL.getCode());
            car.setBranch(Const.Branch.CD.getCode());
            carMapper.insert(car);
            return car.getId();
            //车辆存在
        } else {
            //直接返回车辆id
            List<Driver> drivers = driverMapper.selectDriverListByCarId(car.getId());
            //如果有正常运营的司机则不添加该司机
            for (Driver driver : drivers) {
                if (driver.getDriverStatus() == Const.DriverStatus.NORMAL_DRIVER.getCode()) {
                    return null;
                }
            }
            return car.getId();
        }
    }

    private Integer insertCoModel(String modelType, Date startDate, Date endDate, Double downAmount,
                                 Double payment, Double month, Double totalAmount) {
        CoModel coModel = new CoModel();
        coModel.setDownAmount(BigDecimal.valueOf(downAmount));
        coModel.setPeriodNum(month.intValue());
        coModel.setTotalAmount(BigDecimal.valueOf(totalAmount));
        coModel.setPeriodStartDate(startDate);
        coModel.setPeriodEndDate(endDate);
        coModel.setFinalAmount(BigDecimal.ZERO);
        if (modelType.contains("周")) {
            coModel.setModelType(Const.CoModel.HIRE_PURCHASE_WEEK.getCode());
        } else if (modelType.contains("月")) {
            coModel.setModelType(Const.CoModel.HIRE_PURCHASE_MONTH.getCode());
        } else if (modelType.contains("全")) {
            coModel.setModelType(Const.CoModel.FULL_PAYMENT.getCode());
        }
        System.out.println(coModel.getModelType());
        coModelMapper.insert(coModel);

        if (modelType.contains("全")) {
            return coModel.getId();
        }
        insertPeriodPlan(coModel.getId(), modelType, payment, startDate, month);
        return coModel.getId();
    }

    private Integer insertPeriodPlan(Integer coModelId,String modelType, Double payment, Date startDate, Double month) {
        PeriodPlan periodPlan = new PeriodPlan();
        periodPlan.setCoModelId(coModelId);
        if (modelType.contains("周")) {
            payment = payment / 4;
        }
        periodPlan.setAmount(new BigDecimal(payment));
        //计算最近的星期二
        int dayOfWeek = new DateTime(startDate).getDayOfWeek();
        startDate = DateTimeUtil.getWeekStartDate(startDate);
        if (dayOfWeek > 5 & dayOfWeek < 2) {
            startDate.setTime(startDate.getTime() + 3600 * 1000 * 24 * 14); //如果是星期5-星期一，则下下个星期二为起始日
        } else {
            //星期二到星期五则下个星期二
            startDate.setTime(startDate.getTime() + 3600 * 1000 * 24 * 7);
        }
        periodPlan.setStartDate(startDate);
        Date endDate = new DateTime(startDate).plusMonths(month.intValue()).toDate();
        //最近一个星期二为结束日期
        periodPlan.setEndDate(DateTimeUtil.getWeekStartDate(endDate));
        return periodPlanMapper.insert(periodPlan);
    }

    private Integer insertDriver(String driverName,String phone,String idNo,Integer carId,Integer coModelId) {
        Driver driver = new Driver();
        driver.setName(driverName);
        driver.setPersonalPhone(phone);
        driver.setIdNumber(idNo);
        driver.setCarId(carId);
        driver.setUserId(10);
        driver.setDriverStatus(Const.DriverStatus.NORMAL_DRIVER.getCode());
        driver.setCoModelId(coModelId);
        driver.setPeriodsStatus(1);
        driverMapper.insert(driver);
        return driver.getId();
    }

}
