package com.maple.task;

import com.maple.common.Const;
import com.maple.dao.CarMapper;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPlanMapper;
import com.maple.pojo.Car;
import com.maple.pojo.Driver;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public void addDriver() {
        Workbook sheets = null;
        try {
            sheets = new XSSFWorkbook(new FileInputStream(new File(LINUX_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = sheets.getSheetAt(0);
        for (int i = 314; i<sheet.getLastRowNum();i++) {
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
//
//            String comIns = row.getCell(19).getStringCellValue();
//            double comInsAmount = row.getCell(20).getNumericCellValue();
//            Date comInsExpireDate = row.getCell(21).getDateCellValue();
//            String comInsNo= row.getCell(22).getStringCellValue();



            Date startDate = row.getCell(27).getDateCellValue();
            Date endDate = row.getCell(28).getDateCellValue();
            Double downAmount = row.getCell(32).getNumericCellValue();
            Double payment = row.getCell(33).getNumericCellValue();
            Double month = row.getCell(34).getNumericCellValue();
            String modelType = row.getCell(35).getStringCellValue();
            Double totalAmount = row.getCell(36).getNumericCellValue();
            Date pickDate = row.getCell(42).getDateCellValue();
         }


    }

    private Integer insertCar(String carName, String plateNo, String vin, String engineNo, Date pickDate) {
        Car car = carMapper.selectByVin(vin);
        if (car == null) {
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
        } else {
            List<Driver> drivers = driverMapper.selectDriverListByCarId(car.getId());

        }

    }

    private Integer inserCoModel(String modelType, Date startDate, Date endDate, Double downAmount,
                                 Double payment, Double month, Double totalAmount) {

    }

    private Integer insertDriver(String driverName,String phone,String idNo) {
        return null;
    }
}
