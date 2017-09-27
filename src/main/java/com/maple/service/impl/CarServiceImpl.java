package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.CarMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.ProductMapper;
import com.maple.pojo.Car;
import com.maple.pojo.Driver;
import com.maple.pojo.Product;
import com.maple.service.ICarService;
import com.maple.util.DateTimeUtil;
import com.maple.vo.CarListVo;
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

    public ServerResponse list(Integer userId, String driverName, String plateNumber, String name, String createDateTop, String createDateBut, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (driverName != null) {
            driverName = new StringBuilder().append("%").append(driverName).append("%").toString();
        }
        if (plateNumber != null) {
            plateNumber = new StringBuilder().append("%").append(plateNumber).append("%").toString();
        }
        if (name != null) {
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }
        List<Integer> carIdList = Lists.newArrayList();
        if (userId != null) {
            //查询出当前用户所拥有的车辆id
            carIdList = driverMapper.selectCarIdListByUserId(userId);
        }
        Date createDateT = StringUtils.isBlank(createDateTop) ? null : DateTimeUtil.strToDate(createDateTop);
        Date createDateB = StringUtils.isBlank(createDateBut) ? null : DateTimeUtil.strToDate(createDateBut);
        carIdList=CollectionUtils.isEmpty(carIdList)?null:carIdList;

        List<Car> carList = carMapper.selectByMultiParam(carIdList, driverName, plateNumber, name, createDateT, createDateB);
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
        carListVo.setId(car.getId());
        carListVo.setPlateNumber(car.getPlateNumber());
        carListVo.setName(car.getName());
        List<Driver> driverList = driverMapper.selectDriverListByCarId(car.getId());
        carListVo.setDriverList(driverList);
        carListVo.setCreateTime(DateTimeUtil.dateToStr(car.getCreateTime()));
        return carListVo;
    }
}
