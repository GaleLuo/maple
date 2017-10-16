package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.*;
import com.maple.pojo.*;
import com.maple.service.ICarService;
import com.maple.util.DateTimeUtil;
import com.maple.vo.CarListVo;
import com.maple.vo.CarSummaryVo;
import com.maple.vo.DriverCarListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
@Service("iCarService")
public class CarServiceImpl implements ICarService{
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    @Autowired
    private PeriodPlanMapper periodPlanMapper;


    //返回新增车辆的ID
    public ServerResponse save(Integer productId) {
        if (productId != null) {
            Product product = productMapper.selectByPrimaryKey(productId);
            Car car = new Car();
            car.setName(product.getName());
            int result = carMapper.insert(car);
            if (result > 0) {
                return ServerResponse.createBySuccess(car.getId());
            }
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.createByErrorMessage("添加车辆数据失败");
    }



    public ServerResponse addOrUpdate(Integer userId, Integer id, Integer branch, Integer carStatus, String name, String plateNumber, String engineNumber, String vin, Date pickDate, Date transferDate, Date redeemDate, String gpsNumber, String gpsPhone) {
        if (StringUtils.isEmpty(name)|| StringUtils.isEmpty(plateNumber)||StringUtils.isEmpty(engineNumber)||StringUtils.isEmpty(vin)){
            return ServerResponse.createByErrorMessage("资料不齐，请填写完整");
        }

        //将空字符串置为null
        name = StringUtil.isEmpty(name) ? null : name;
        plateNumber = StringUtil.isEmpty(plateNumber) ? null : plateNumber;
        engineNumber = StringUtil.isEmpty(engineNumber) ? null : engineNumber;
        vin = StringUtil.isEmpty(vin) ? null : vin;
        gpsNumber = StringUtil.isEmpty(gpsNumber) ? null : gpsNumber;
        gpsPhone = StringUtil.isEmpty(gpsPhone) ? null : gpsPhone;


        Car car = new Car();
        car.setId(id);
        car.setBranch(branch);
        car.setCarStatus(carStatus);
        car.setName(name);
        car.setPlateNumber(plateNumber);
        car.setEngineNumber(engineNumber);
        car.setVin(vin);
        car.setPickDate(pickDate);
        car.setTransferDate(transferDate);
        car.setRedeemDate(redeemDate);
        car.setGpsNumber(gpsNumber);
        car.setGpsPhone(gpsPhone);

        if (id != null) {
            //如果有前台userid数据则判断是否是属于自己的车辆
            if (userId != null) {
                List<Integer> carIdList = driverMapper.selectCarIdListByUserId(userId);
                if (carIdList.contains(id)) {
                    int result = carMapper.updateByPrimaryKeySelective(car);
                    if (result > 0) {
                        return ServerResponse.createBySuccessMessage("更新车辆数据成功");
                    }
                }
                //没有,则认为是管理员操作,不判断是否是自己车辆
            } else {
                int result = carMapper.updateByPrimaryKey(car);
                if (result > 0) {
                    return ServerResponse.createBySuccessMessage("更新车辆数据成功");
                }
            }
        } else {

            Car checkExist = carMapper.selectbyPlateNumber(plateNumber);
            if (checkExist!=null&&checkExist.getPlateNumber().equals(plateNumber)) {
                return ServerResponse.createByErrorMessage("车辆已经存在，请检查");
            }

            int result = carMapper.insertSelective(car);
            if (result>0) {
                return ServerResponse.createBySuccessMessage("车辆添加成功");
            }
        }
        return ServerResponse.createByErrorMessage("更新车辆数据失败");
    }

    public ServerResponse list(Integer userId,String driverName,Integer branch,Integer carStatus, String plateNumber, String carName, String orderBy,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotEmpty(driverName)) {
            driverName = new StringBuilder().append("%").append(driverName).append("%").toString();
        } else {
            driverName = null;
        }
        if (StringUtils.isNotEmpty(plateNumber)) {
            plateNumber = new StringBuilder().append("%").append(plateNumber).append("%").toString();
        } else {
            plateNumber = null;
        }
        if (StringUtils.isNotEmpty(carName)) {
            carName = new StringBuilder().append("%").append(carName).append("%").toString();
        } else {
            carName = null;
        }

        List<Car> carList = carMapper.selectByMultiParam(userId,driverName,branch,carStatus, plateNumber, carName,orderBy);
        List<CarListVo> carListVoList = Lists.newArrayList();
        for (Car car : carList) {
            CarListVo carListVo = assembleCarListVo(car);
            carListVoList.add(carListVo);
        }
        PageInfo pageInfo = new PageInfo(carList);
        pageInfo.setList(carListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse summary(Integer carId) {
        Car car = carMapper.selectByPrimaryKey(carId);
        if (car == null) {
            return ServerResponse.createByErrorMessage("查询错误");
        }
        return ServerResponse.createBySuccess(assembleCarSummaryVo(car));
    }

    public ServerResponse bind(Integer driverId, Integer carId) {
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        if (driver != null && carId !=null) {
            CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
            driver.setCarId(carId);
            coModel.setCarId(carId);
            driverMapper.updateByPrimaryKey(driver);
            coModelMapper.updateByPrimaryKey(coModel);
            Car car = carMapper.selectByPrimaryKey(carId);
            car.setCarStatus(Const.CarStatus.NORMAL.getCode());
            carMapper.updateByPrimaryKey(car);
            return ServerResponse.createBySuccess("绑定车辆成功");
        }
        return ServerResponse.createByErrorMessage("绑定车辆失败");
    }

    private CarSummaryVo assembleCarSummaryVo(Car car) {
        CarSummaryVo carSummaryVo = new CarSummaryVo();
        carSummaryVo.setCarId(car.getId());
        carSummaryVo.setBranch(car.getBranch().toString());
        carSummaryVo.setCarName(car.getName());
        carSummaryVo.setCarStatus(car.getCarStatus().toString());
        carSummaryVo.setPlateNum(car.getPlateNumber());
        carSummaryVo.setEngineNum(car.getEngineNumber());
        carSummaryVo.setVin(car.getVin());
        carSummaryVo.setPickDate(DateTimeUtil.dateToStr(car.getPickDate()));
        carSummaryVo.setTransferDate(DateTimeUtil.dateToStr(car.getTransferDate()));
        carSummaryVo.setRedeemDate(DateTimeUtil.dateToStr(car.getRedeemDate()));
        carSummaryVo.setGpsNum(car.getGpsNumber());
        carSummaryVo.setGpsPhoneNum(car.getGpsPhone());

        return carSummaryVo;
    }


    private CarListVo assembleCarListVo(Car car) {
        CarListVo carListVo = new CarListVo();
        Ticket ticket = ticketMapper.selectByCarId(car.getId());
        if (ticket == null) {
            carListVo.setTicket("暂未查询");
        } else {
            carListVo.setTicket(ticket.getScore().toString()+" | "+ ticket.getMoney().toString());
        }

        carListVo.setCarId(car.getId());
        carListVo.setBranch(Const.Branch.codeOf(car.getBranch()).getDesc());
        carListVo.setCarStatus(Const.CarStatus.codeOf(car.getCarStatus()).getDesc());
        carListVo.setPlateNum(car.getPlateNumber());
        carListVo.setCarName(car.getName());
        List<Driver> driverList = driverMapper.selectDriverListByCarId(car.getId());
        List<DriverCarListVo> driverCarListVoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(driverList)) {
            for (Driver driver : driverList) {
                DriverCarListVo driverCarListVo = assembleDriverCarListVo(driver);
                driverCarListVoList.add(driverCarListVo);
            }
        } else {
            DriverCarListVo driverCarListVo = new DriverCarListVo();
            driverCarListVo.setDriverName("未绑定");
            driverCarListVo.setDriverStatus(Const.DriverStatus.UNBOUND.getDesc());
            driverCarListVoList.add(driverCarListVo);
        }
        carListVo.setDriverCarListVoList(driverCarListVoList);
        Date transferDate1 = car.getTransferDate();
        Date redeemDate1 = car.getRedeemDate();

        String transferDate = car.getTransferDate() == null ? "-" : DateTimeUtil.dateToStr(transferDate1, "yyyy 年 MM 月 dd 日");
        String redeemDate = car.getRedeemDate() == null ? "-" : DateTimeUtil.dateToStr(redeemDate1, "yyyy 年 MM 月 dd 日");

            carListVo.setTransferDate(transferDate);

            carListVo.setRedeemDate(redeemDate);
        carListVo.setPickDate(DateTimeUtil.dateToStr(car.getPickDate(),"yyyy 年 MM 月 dd 日"));
        return carListVo;
    }

    private DriverCarListVo assembleDriverCarListVo(Driver driver) {
        DriverCarListVo driverCarListVo = new DriverCarListVo();
        driverCarListVo.setDriverId(driver.getId());
        driverCarListVo.setDriverName(driver.getName());
        driverCarListVo.setDriverStatus(Const.DriverStatus.codeOf(driver.getDriverStatus()).getDesc());
        driverCarListVo.setPhoneNum(driver.getPersonalPhone());
        return driverCarListVo;
    }
}
