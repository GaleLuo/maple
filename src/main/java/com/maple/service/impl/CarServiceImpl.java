package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.CarMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.ProductMapper;
import com.maple.dao.TicketMapper;
import com.maple.pojo.Car;
import com.maple.pojo.Driver;
import com.maple.pojo.Product;
import com.maple.pojo.Ticket;
import com.maple.service.ICarService;
import com.maple.util.DateTimeUtil;
import com.maple.vo.CarListVo;
import com.maple.vo.DriverCarListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.draw.binding.CTOfficeArtExtensionList;
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

    public ServerResponse update(Integer userId, Car car) {
        if (car.getId() != null) {
            //如果有前台userid数据则判断是否是属于自己的车辆
            if (userId != null) {
                List<Integer> carIdList = driverMapper.selectCarIdListByUserId(userId);
                if (carIdList.contains(car.getId())) {
                    int result = carMapper.updateByPrimaryKeySelective(car);
                    if (result > 0) {
                        return ServerResponse.createBySuccessMessage("更新司机数据成功");
                    }
                }
                //没有,则认为是管理员操作,不判断是否是自己车辆
            } else {
                int result = carMapper.updateByPrimaryKeySelective(car);
                if (result > 0) {
                    return ServerResponse.createBySuccessMessage("更新司机数据成功");
                }
            }
        }
        return ServerResponse.createByErrorMessage("更新司机数据失败");
    }

    public ServerResponse list(Integer userId,String driverName,Integer branch,Integer carStatus, String plateNumber, String carName, String orderBy,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (driverName != null) {
            driverName = new StringBuilder().append("%").append(driverName).append("%").toString();
       }
        if (plateNumber != null) {
            plateNumber = new StringBuilder().append("%").append(plateNumber).append("%").toString();
        }
        if (carName != null) {
            carName = new StringBuilder().append("%").append(carName).append("%").toString();
        }
        List<Integer> carIdList = Lists.newArrayList();

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

    private CarListVo assembleCarListVo(Car car) {
        CarListVo carListVo = new CarListVo();
        Ticket ticket = ticketMapper.selectByCarId(car.getId());
        if (ticket == null) {
            carListVo.setTicket("暂未查询");
        } else {
            carListVo.setTicket(ticket.getScore().toString()+" -"+ ticket.getMoney().toString());
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
            Driver driver = new Driver();
            driver.setName("未绑定");
            DriverCarListVo driverCarListVo = assembleDriverCarListVo(driver);
            driverCarListVoList.add(driverCarListVo);
        }
        carListVo.setDriverCarListVoList(driverCarListVoList);
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
