package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ResponseCode;
import com.maple.common.ServerResponse;
import com.maple.dao.*;
import com.maple.pojo.*;
import com.maple.service.IDriverService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.vo.AccountVo;
import com.maple.vo.DriverListVo;
import com.maple.vo.DriverVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/5/26.
 */
@Service("iDriverService")
public class DriverServiceImpl implements IDriverService {

    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private PeriodPlanMapper periodPlanMapper;

    public ServerResponse saveDriver(Integer userId, Driver driver) {
        if (driver.getId() != null) {
            Driver resultDriver = driverMapper.selectByDriverIdAndUserId(userId, driver.getId());
            if (resultDriver == null) {
                return ServerResponse.createByErrorMessage("不能修改非本账户下属的司机信息");
            }
            driver.setCarId(null);
            driver.setUserId(null);
            driver.setDriverStatus(null);
            driver.setPeriodsStatus(null);
            int result = driverMapper.updateByPrimaryKeySelective(driver);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("更新司机信息成功");
            }
        } else {
            driver.setUserId(userId);
            driver.setDriverStatus(Const.DriverStatus.POTENTIAL_DRIVER.getCode());
            Integer i = driverMapper.checkId(driver.getIdNumber());
            if (i != 0){
                return ServerResponse.createByErrorMessage("已存在该司机信息,请检查");
            }
            int result = driverMapper.insert(driver);
            if (result > 0) {
                return ServerResponse.createBySuccess(driver.getId());
            }
        }
        return ServerResponse.createByErrorMessage("保存司机信息失败");
    }

    //前台设置司机状态
    public ServerResponse setDriverStatus(Integer userId, String driverIds,Integer status) {

        if (driverIds == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<String> driverIdList = Splitter.on(",").splitToList(driverIds);
        if (CollectionUtils.isNotEmpty(driverIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int result = driverMapper.setDriverStatusByUserIdAndDriverIds(userId, driverIdList, status);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("更新状态失败");
        }
        return ServerResponse.createBySuccessMessage("更新状态成功");
    }
    //后台设置司机状态
    public ServerResponse manageDriverStatus(String driverIds,Integer status) {

        if ( status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<String> driverIdList = Splitter.on(",").splitToList(driverIds);
        if (CollectionUtils.isNotEmpty(driverIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int result = driverMapper.setDriverStatusByUserIdAndDriverIds(null, driverIdList, status);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("更新状态失败");
        }
        return ServerResponse.createBySuccessMessage("更新状态成功");
    }

    public ServerResponse addAccount(Integer driverId, Integer platformCode, String account) {
        if (StringUtils.isNotBlank(account)||platformCode==null||driverId==null) {
            ServerResponse.createByErrorMessage("数据不完整");
        }
        Account newAccount = new Account();
        newAccount.setDriverId(driverId);
        newAccount.setPlatform(platformCode);
        newAccount.setAccount(account);
        int result = accountMapper.insert(newAccount);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("新增账户信息失败");
        }
        return ServerResponse.createBySuccessMessage("新增账户信息成功");
    }

    public ServerResponse getAccountList(Integer driverId) {
        List<Account> accountList = accountMapper.selectListByDriverId(driverId);
        List<AccountVo> accountVoList = Lists.newArrayList();
        for (Account account : accountList) {
            AccountVo accountVo = assembleAccountVo(account);
            accountVoList.add(accountVo);
        }
        if (accountList.size() == 0) {
            return ServerResponse.createByErrorMessage("未查到相关数据");
        }
        return ServerResponse.createBySuccess(accountVoList);
    }

    private AccountVo assembleAccountVo(Account account) {
        AccountVo accountVo = new AccountVo();
        accountVo.setAccount(account.getAccount());
        accountVo.setCreateTime(DateTimeUtil.dateToStr(account.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        accountVo.setDriverId(account.getDriverId());
        accountVo.setPlatform(Const.PaymentPlatform.codeOf(account.getPlatform()).getDesc());
        accountVo.setId(account.getId());
        return accountVo;
    }

    public ServerResponse delete(Integer userId, String driverIds,Integer pageNum,Integer pageSize) {
        List<String> driverIdList = Splitter.on(",").splitToList(driverIds);
        if (CollectionUtils.isNotEmpty(driverIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //逻辑删除,设置状态为5
        driverMapper.deleteDeletableDriverByUserIdAndDriverIds(userId, driverIdList);
        return ServerResponse.createBySuccess();
    }

    public ServerResponse list(Integer userId,String plateNum, String driverName, String phoneNum, Integer driverStatus, Integer coModelType, String orderBy, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(driverName)) {
            driverName = new StringBuilder("%").append(driverName).append("%").toString();
        } else {
            driverName = null;
        }
        if (StringUtils.isNotBlank(plateNum)) {
            plateNum = new StringBuilder("%").append(StringUtils.upperCase(plateNum)).append("%").toString();
        } else {
            plateNum = null;
        }
        phoneNum = StringUtils.isBlank(phoneNum) ? null : phoneNum;


        List<DriverListVo> driverListVoList = Lists.newArrayList();

        List<Driver> driverList = driverMapper.selectByParams(plateNum,driverName,phoneNum,driverStatus,coModelType,orderBy);
        //如果结果为空则返回空的分页数据
        if (CollectionUtils.isEmpty(driverList)) {
            PageInfo pageInfo = new PageInfo(driverList);
            return ServerResponse.createBySuccess(pageInfo);
        }
        for (Driver driver : driverList) {
            DriverListVo driverListVo = assembleDriverListVo(driver);
            driverListVoList.add(driverListVo);
        }
        PageInfo pageInfo = new PageInfo(driverList);
        pageInfo.setList(driverListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse search(Integer userId, String createDateTop, String createDateBut,String name, String status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(name)) {
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }
        List<Integer> statusList=Lists.newArrayList();
        if (StringUtils.isNotBlank(status)) {
            List<String> statusStringList = Splitter.on(",").splitToList(status);
            for (String statusStr : statusStringList) {
                Integer statusInt = Integer.valueOf(statusStr);
                statusList.add(statusInt);
            }
        }
        Date createDateT=StringUtils.isNotBlank(createDateTop) ? DateTimeUtil.strToDate(createDateTop) : null;
        Date createDateB=StringUtils.isNotBlank(createDateBut) ? DateTimeUtil.strToDate(createDateBut) : null;

        List<DriverListVo> driverListVoList = Lists.newArrayList();
        List<Driver> driverList = driverMapper.selectByUserIdCreateDateNameStatusList(userId, createDateT, createDateB, name, statusList);
        for (Driver driver : driverList) {
            DriverListVo driverListVo = assembleDriverListVo(driver);
            driverListVoList.add(driverListVo);
        }
        PageInfo pageInfo = new PageInfo(driverList);
        pageInfo.setList(driverListVoList);
        return ServerResponse.createBySuccess(driverListVoList);
    }

    public ServerResponse detail(Integer userId, Integer driverId) {
        Driver driver = driverMapper.selectByDriverIdAndUserId(userId,driverId);
        if (driver != null) {
            return ServerResponse.createBySuccess(assembleDriverVo(driver));
        }
        return ServerResponse.createByErrorMessage("获取信息失败");
    }

    private DriverListVo assembleDriverListVo(Driver driver) {
        DriverListVo driverListVo = new DriverListVo();
        if (driver.getCarId() != null) {
            Car car = carMapper.selectByPrimaryKey(driver.getCarId());
            driverListVo.setCarName(car.getName());
            driverListVo.setPlateNum(car.getPlateNumber());
        } else {
            driverListVo.setCarName("未绑定");
            driverListVo.setPlateNum("没有记录");
        }
        CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());

        Ticket ticket = ticketMapper.selectByCarId(driver.getCarId());
        if (ticket == null) {
            driverListVo.setTicket("暂未查询");
        } else {
            driverListVo.setTicket(ticket.getScore().toString()+" -"+ticket.getMoney().toString());
        }
        driverListVo.setPeriodStartDate(DateTimeUtil.dateToStr(coModel.getPeriodStartDate(),"yyyy年MM月dd日"));
        driverListVo.setPhoneNum(driver.getPersonalPhone());
        driverListVo.setDriverId(driver.getId());
        driverListVo.setDriverName(driver.getName());
        driverListVo.setCoModelType(Const.CoModel.codeOf(coModel.getModelType()).getDesc());
        driverListVo.setDriverStatus(Const.DriverStatus.codeOf(driver.getDriverStatus()).getDesc());
        return driverListVo;
    }

    private DriverVo assembleDriverVo(Driver driver) {
        DriverVo driverVo = new DriverVo();
        Car car = carMapper.selectByPrimaryKey(driver.getCarId());
        CoModel coModel = coModelMapper.selectByCarId(driver.getCarId());
        BigDecimal totalReceived = periodPaymentMapper.findTotalReceivedByCarId(driver.getCarId());
        totalReceived = totalReceived == null ? BigDecimal.ZERO : totalReceived;
        Integer overdueNum = periodPaymentMapper.findOverdueByCarId(driver.getCarId());
        Ticket ticket = ticketMapper.selectByCarId(driver.getCarId());
        if (ticket == null) {
            driverVo.setTicket("未查询");
            driverVo.setTicketUpdateTime("未查询");
        } else {
            driverVo.setTicket(ticket.getScore().toString()+" - "+ticket.getMoney().toString());
            driverVo.setTicketUpdateTime(DateTimeUtil.dateToStr(ticket.getUpdateTime()));
        }
        double periodPercentage = (totalReceived.doubleValue() + coModel.getDownAmount().doubleValue())/coModel.getTotalAmount().doubleValue();

        BigDecimal receivableByNow = BigDecimal.ZERO;
        List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(coModel.getId());
        long totalWeekNum = (coModel.getPeriodEndDate().getTime() - coModel.getPeriodStartDate().getTime()) / (3600 * 1000 * 24 * 7);
        //如果是月供
        if (Const.CoModel.HIRE_PURCHASE_WEEK.getCode() == coModel.getModelType()) {
            //开始月份至现在的交费周期 加一是第一个月即需要交费
            int months = Months.monthsBetween(new DateTime(coModel.getPeriodStartDate()), new DateTime()).getMonths()+1;
            //月供只有一种模式，所以直接相乘
            receivableByNow = BigDecimalUtil.mul(periodPlanList.get(0).getAmount().doubleValue(), months);
        } else if (Const.CoModel.HIRE_PURCHASE_MONTH.getCode() == coModel.getModelType()){//如果是周供
            for (PeriodPlan periodPlan : periodPlanList) {
                if (periodPlan.getEndDate().before(new Date())) {
                    //如果已经过结束日期，则全部加上
                    int weeks = Weeks.weeksBetween(new DateTime(periodPlan.getStartDate()), new DateTime(periodPlan.getEndDate())).getWeeks();
                    //如果是最后一周之后，需多加一周收款
                    if (new Date().before(coModel.getPeriodEndDate())) {
                        weeks=weeks+1;
                    }
                    receivableByNow = BigDecimalUtil.mul(periodPlan.getAmount().doubleValue(), weeks).add(receivableByNow);//加上之前的数额
                } else if (periodPlan.getStartDate().before(new Date()) && periodPlan.getEndDate().after(new Date())) {
                    //如果是周期期间，则算出周数+1
                    int weeks = Weeks.weeksBetween(new DateTime(periodPlan.getStartDate()), new DateTime()).getWeeks()+1;
                    receivableByNow = BigDecimalUtil.mul(periodPlan.getAmount().doubleValue(), weeks).add(receivableByNow);
                }
            }
        }


        BigDecimal overdueAmount = BigDecimalUtil.sub(receivableByNow.doubleValue(),totalReceived.doubleValue());

        BigDecimal v1=BigDecimalUtil.sub(coModel.getTotalAmount().doubleValue(),coModel.getDownAmount().doubleValue());//总额减去首付得到余款

        BigDecimal balance = BigDecimalUtil.sub(v1.doubleValue(), totalReceived.doubleValue());

        driverVo.setDriverStatus(Const.DriverStatus.codeOf(driver.getDriverStatus()).getDesc());
        driverVo.setDriverName(driver.getName());
        driverVo.setPhoneNum(driver.getPersonalPhone());
        driverVo.setIdNum(driver.getIdNumber());
        driverVo.setPlateNum(car.getPlateNumber());
        driverVo.setVin(car.getVin());
        driverVo.setCarName(car.getName());
        driverVo.setStartDate(DateTimeUtil.dateToStr(coModel.getPeriodStartDate(),"yyyy年MM月dd日"));
        driverVo.setEndDate(DateTimeUtil.dateToStr(coModel.getPeriodEndDate(),"yyyy年MM月dd日"));
        driverVo.setCoModelType(Const.CoModel.codeOf(coModel.getModelType()).getDesc());
        driverVo.setPeriodPercentage(Math.round(periodPercentage*100)+"%");
        driverVo.setTotalAmount(coModel.getTotalAmount());
        driverVo.setDownAmount(coModel.getDownAmount());
        driverVo.setPeriodNum((int) totalWeekNum);
        driverVo.setOverdueNum(overdueNum);
        driverVo.setOverdueAmount(overdueAmount);
        driverVo.setBalance(balance);

        return driverVo;
    }

}
