package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.*;
import com.maple.pojo.*;
import com.maple.service.IPeriodPaymentService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Maple.Ran on 2017/6/13.
 */
@Service("iPeriodPaymentService")
public class PeriodPaymentServiceImpl implements IPeriodPaymentService {
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private PeriodPlanMapper periodPlanMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private AccountMapper accountMapper;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(PeriodPaymentServiceImpl.class);


    public ServerResponse addOrUpdate(User user,Boolean addToAccount,Integer paymentId, Integer driverId, BigDecimal payment,String payer, Integer paymentPlatform, String platformNum,Long payTime,String comment){

        if (payment == null || paymentPlatform == null || payTime==null) {
            return ServerResponse.createByErrorMessage("请表单填写完整");
        }
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        PeriodPayment newPeriodPayment = new PeriodPayment();
        if (driver != null) {
            newPeriodPayment.setCarId(driver.getCarId());
            newPeriodPayment.setDriverId(driverId);
        }
        newPeriodPayment.setPayment(payment);
        newPeriodPayment.setPaymentPlatform(paymentPlatform);
        newPeriodPayment.setPlatformNumber(platformNum);
        newPeriodPayment.setPayer(payer);
        Date payDate = new Date(payTime);

        newPeriodPayment.setPayTime(payDate);
        newPeriodPayment.setPlatformStatus(Const.PlatformStatus.UNCONFIRMED.getCode());
        if (paymentId == null) {
            comment = comment+"   添加人:" + user.getName();
        }
        newPeriodPayment.setComment(comment);
        int i;
        if (paymentId == null) {
            i = periodPaymentMapper.insert(newPeriodPayment);
        } else {
            newPeriodPayment.setId(paymentId);
            i = periodPaymentMapper.updateByPrimaryKeySelective(newPeriodPayment);
        }
        String msg = "";
        //添加常用账户
        if (addToAccount) {
            Account account = accountMapper.selectByAccNo(platformNum);
            if (account != null) {
                msg = "，常用账户添加失败，该账户已存在";
            } else {
                account = new Account();
                //平台代码
                account.setPlatform(paymentPlatform);
                //平台账号
                account.setAccount(platformNum);
                //平台用户名
                account.setName(payer);
                //司机id
                account.setDriverId(driverId);
                accountMapper.insert(account);
                msg = "，司机常用添加成功";
            }
        }


        if (i > 0) {
            return ServerResponse.createBySuccessMessage("添加数据成功"+msg);
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    /*

     */

    public ServerResponse paymentList(Integer branch,Long startTime, Long endTime, String driverName, Integer coModelType, String payer,Integer platformStatus, Integer paymentPlatform,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        //如果起止日期为空 则返回错误状态
        if (startTime == null || endTime == null) {
            return ServerResponse.createByErrorMessage("请求数据错误");
        }
        Date startDate = new DateTime(startTime).millisOfDay().withMinimumValue().toDate();
        Date endDate = new DateTime(endTime).millisOfDay().withMaximumValue().toDate();

        if (StringUtils.isNotEmpty(driverName)) {
            driverName = new StringBuilder().append("%").append(driverName).append("%").toString();
        } else {
            driverName = null;
        }

        if (StringUtils.isNotEmpty(payer)) {
            payer = new StringBuilder().append("%").append(payer).append("%").toString();
        } else {
            payer = null;
        }


        List<PeriodPayment> periodPaymentList = periodPaymentMapper.selectListByParams(startDate, endDate, driverName, coModelType, payer, platformStatus, paymentPlatform,branch);
        List<PaymentListVo> paymentListVoList = Lists.newArrayList();

        for (PeriodPayment periodPayment : periodPaymentList) {
            PaymentListVo paymentListVo = assemblePaymentListVo(periodPayment);
            paymentListVoList.add(paymentListVo);
        }
        PageInfo pageInfo = new PageInfo(periodPaymentList);
        pageInfo.setList(paymentListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse updatePaymentStatus(Integer[] paymentIdArray, Integer paymentStatus) {
        System.out.println(paymentIdArray);
        if (paymentStatus == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        int result = periodPaymentMapper.updateByPaymentStatus(paymentIdArray, paymentStatus);
        if (result > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse getPaymentMethod() {
        List<Map> methodList = Lists.newArrayList();
        Const.PaymentPlatform[] values = Const.PaymentPlatform.values();
        for (Const.PaymentPlatform value : values) {
            Map<String,String> method = Maps.newHashMap();
            method.put("desc", value.getDesc());
            method.put("code", Integer.toString(value.getCode()));
            methodList.add(method);
        }
        return ServerResponse.createBySuccess(methodList);
    }

    private PaymentListVo assemblePaymentListVo(PeriodPayment periodPayment) {
        PaymentListVo paymentListVo = new PaymentListVo();
        paymentListVo.setId(periodPayment.getId());
        paymentListVo.setPaymentStatus(Const.PlatformStatus.codeOf(periodPayment.getPlatformStatus()).getDesc());
        paymentListVo.setPaymentAmount(periodPayment.getPayment());
        paymentListVo.setDriverId(periodPayment.getDriverId());
        paymentListVo.setPlatformNum(periodPayment.getPlatformNumber());
        paymentListVo.setPaymentPlatformCode(periodPayment.getPaymentPlatform().toString());
        paymentListVo.setPaymentPlatform(Const.PaymentPlatform.codeOf(periodPayment.getPaymentPlatform()).getDesc());
        if (periodPayment.getPayer() == null) {
            paymentListVo.setPayer("-");
        } else {
            paymentListVo.setPayer(periodPayment.getPayer());
        }

        //如果不知道付款人，则显示 -
        Driver driver = driverMapper.selectByPrimaryKey(periodPayment.getDriverId());
        if (driver != null) {
            paymentListVo.setDriverName(driver.getName());
        } else {
            paymentListVo.setDriverName("-");
        }

        paymentListVo.setComment(periodPayment.getComment());
        paymentListVo.setAddTime(DateTimeUtil.dateToStr(periodPayment.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        //如果不知道付款人、付款周期，则显示 -
        if (periodPayment.getPayTime() != null) {
            paymentListVo.setPeriodTime(DateTimeUtil.dateToStr(periodPayment.getPayTime(), "yyyy-MM-dd"));
        } else {
            paymentListVo.setPeriodTime("-");
        }

        return paymentListVo;
    }


    public ServerResponse<PageInfo> list(Integer branch,String date, String driverName, Integer coModelType,Integer payStatus, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Set confirmed = Sets.newHashSet(Const.PlatformStatus.PAID_NORMAL.getCode(), Const.PlatformStatus.PAID_OVERDUE.getCode());
        Set unconfirmed = Sets.newHashSet(Const.PlatformStatus.UNCONFIRMED.getCode());


        if (StringUtils.isNotBlank(driverName)) {
            driverName = new StringBuilder().append("%").append(driverName).append("%").toString();
        } else {
            driverName = null;
        }
        Date dateTime = DateTimeUtil.webStrToDate(date);

        Date startDate=null;
        Date endDate=null;
        if (coModelType == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
            startDate= DateTimeUtil.getWeekStartDate(dateTime);
            endDate= DateTimeUtil.getWeekEndDate(dateTime);
        } else if (coModelType == Const.CoModel.HIRE_PURCHASE_MONTH.getCode()) {
            startDate=DateTimeUtil.getMonthStartDate(dateTime);
            endDate = DateTimeUtil.getMonthEndDate(dateTime);
        }
        List<Driver> driverList;
        List<PeriodPaymentListVo> periodPaymentListVoList = Lists.newArrayList();
        //如果没有付款详情参数则查询所有司机信息,否则只查询有付款记录的
        if (payStatus==1) {
            driverList = driverMapper.selectDriverReceivable(startDate, endDate, coModelType, driverName,branch);
        } else if (payStatus == 0) {
            driverList = driverMapper.selectDriverReceivedPartly(startDate,endDate,coModelType,driverName,branch);
        } else {
            driverList = driverMapper.selectDriverNotReceived(startDate, endDate, coModelType, driverName,branch);
        }

        for (Driver driver : driverList) {
            Integer driverId = driver.getId();
            PeriodPayment periodPayment = periodPaymentMapper.selectWeekSumByParams(driverId, startDate, endDate,confirmed);
            //如果结果为空,则没有付款记录
            if (periodPayment == null) {
                periodPayment = new PeriodPayment();
                periodPayment.setCarId(driver.getCarId());
                periodPayment.setPayment(BigDecimal.ZERO);
                periodPayment.setDriverId(driverId);
            }
            //查出未确认的款项
            PeriodPayment unconfirmedPeriodPayment = periodPaymentMapper.selectWeekSumByParams(driverId, startDate, endDate,unconfirmed);

            PeriodPaymentListVo periodPaymentListVo = assemblePeriodPaymentListVo(periodPayment,startDate,endDate);
            //直接设置vo中的未确认款项数值
            if (unconfirmedPeriodPayment != null) {
                periodPaymentListVo.setUnconfirmedPayment(unconfirmedPeriodPayment.getPayment());
            } else {
                periodPaymentListVo.setUnconfirmedPayment(BigDecimal.ZERO);
            }
            periodPaymentListVoList.add(periodPaymentListVo);

        }
        PageInfo pageInfo = new PageInfo(driverList);
        pageInfo.setList(periodPaymentListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse generalList(Integer branch,Long start,Long end,Integer coModelType, int pageNum, int pageSize) {
        Map<String, Object> data = Maps.newHashMap();
        List<PeriodPaymentGeneralListVo> periodPaymentGeneralListVoList = Lists.newArrayList();
        if (start == null && end == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        } else if (start == null) {
            start = end;
        } else if (end == null) {
            end = start;
        }
        if (coModelType.equals(Const.CoModel.HIRE_PURCHASE_WEEK.getCode())){
        //周租回款总览
        List<Date> weekStartDateList = DateTimeUtil.getWeekStartDateList(DateTimeUtil.getWeekStartDate(new Date(start)),DateTimeUtil.getWeekStartDate(new Date(end)));
            for (Date startDate : weekStartDateList) {
                Date endDate = DateTimeUtil.getWeekEndDate(startDate);
                PeriodPaymentGeneralListVo periodPaymentGeneralListVo = assemblePeriodPaymentGeneralListVo(startDate, endDate, coModelType,branch);
                periodPaymentGeneralListVoList.add(periodPaymentGeneralListVo);
            }
        } else if (coModelType.equals(Const.CoModel.HIRE_PURCHASE_MONTH.getCode())) {
            //月租回款总览
            Date startDate = new DateTime(start).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
            Date endDate = new DateTime(end).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();//
            DateTime startMonth = new DateTime(startDate);
            DateTime endMonth = new DateTime(endDate);
            while (!endMonth.isEqual(startMonth)) {//重复执行相应次数
                endMonth = endMonth.minusMonths(1);
                endDate = new DateTime(startDate).plusMonths(1).minusSeconds(1).toDate();//得到当月最大的时间
                PeriodPaymentGeneralListVo periodPaymentGeneralListVo = assemblePeriodPaymentGeneralListVo(startDate, endDate, coModelType,branch);
                periodPaymentGeneralListVoList.add(periodPaymentGeneralListVo);
                startDate = new DateTime(startDate).plusMonths(1).toDate();
            }
            //如果选择当月，增加当月，截止当天
            DateTime now = new DateTime();
            DateTime endDateTime = new DateTime(end);
            if (endDateTime.getMonthOfYear() == now.getMonthOfYear()) {
                //当月第一天
                Date monthStartDate = endDateTime.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
                PeriodPaymentGeneralListVo periodPaymentGeneralListVo = assemblePeriodPaymentGeneralListVo(monthStartDate, now.toDate(), coModelType,branch);
                periodPaymentGeneralListVoList.add(periodPaymentGeneralListVo);
            }


        }
        int times = pageSize < periodPaymentGeneralListVoList.size() ? pageSize : periodPaymentGeneralListVoList.size();
        int limit = pageNum * times > periodPaymentGeneralListVoList.size() ? periodPaymentGeneralListVoList.size() : pageNum * times;


        data.put("total", periodPaymentGeneralListVoList.size());
        data.put("pageSize", pageSize);
        data.put("currentPage", pageNum);
        data.put("list", periodPaymentGeneralListVoList.subList((pageNum-1)*times, limit));


        return ServerResponse.createBySuccess(data);
    }

    public ServerResponse currentPaymentDetail(Integer driverId, String date) {
        //判断是月供还是周供
        Integer carId = driverMapper.selectCarIdByDriverId(driverId);
        if (carId == null) {
            return ServerResponse.createByErrorMessage("数据错误");
        }

        if (StringUtils.isBlank(date)) {
            return ServerResponse.createByErrorMessage("时间格式错误");
        }
        Date dateTime = DateTimeUtil.webStrToDate(date);

        CoModel coModel = coModelMapper.selectByCarId(carId);
        Integer coModelType = coModel.getModelType();
        Date startDate=null;
        Date endDate=null;
        if (Const.CoModel.HIRE_PURCHASE_WEEK.getCode() == coModelType) {
            startDate = DateTimeUtil.getWeekStartDate(dateTime);
            endDate = DateTimeUtil.getWeekEndDate(dateTime);
        }

        List<PeriodPayment> periodPaymentList = periodPaymentMapper.selectListByDriverId(driverId, startDate, endDate);
        List<PaymentDetailVo> paymentDetailVoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(periodPaymentList)) {
            for (PeriodPayment periodPayment : periodPaymentList) {
                PaymentDetailVo paymentDetailVo = assemblePaymentDetailVo(periodPayment);
                paymentDetailVoList.add(paymentDetailVo);
            }
        }
        return ServerResponse.createBySuccess(paymentDetailVoList);
    }

    public ServerResponse driverTotalPayment(Integer driverId) {
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        Integer carId = driver.getCarId();
        CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
        //
        List<DriverTotalPaymentListVo> DriverTotalPaymentListVoList = Lists.newArrayList();

        if (coModel.getModelType().equals(Const.CoModel.HIRE_PURCHASE_WEEK.getCode())) {
            List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(coModel.getId());
            for (PeriodPlan periodPlan : periodPlanList) {
                List<Date> weekStartDateList = DateTimeUtil.getWeekStartDateList(periodPlan.getStartDate(), periodPlan.getEndDate());       //整个合同的起始日和结束日得出 每周起始日的列表
                Date startDate;//每周期的起始日
                Date endDate;//每周期的结束日
                for (Date date : weekStartDateList) {
                    startDate = date;
                    endDate = DateTimeUtil.getWeekEndDate(date);
                    DriverTotalPaymentListVo driverPeriodGeneralListVo = assembleDriverTotalPaymentListVo(carId, startDate, endDate);
                    String status = driverPeriodGeneralListVo.getReceivedAmount().compareTo(periodPlan.getAmount()) >= 0 ? "当期结清" : "当期未结清";
                    driverPeriodGeneralListVo.setStatus(status);
                    driverPeriodGeneralListVo.setDueAmount(periodPlan.getAmount());
                    DriverTotalPaymentListVoList.add(driverPeriodGeneralListVo);
                }
            }

        }else if (coModel.getModelType().equals(Const.CoModel.HIRE_PURCHASE_MONTH.getCode())){
            List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(coModel.getId());
            PeriodPlan periodPlan = periodPlanList.get(0);//月租只有一种情况所以直接取第一个


            Date startDate = periodPlan.getStartDate();//合作起始日
            Date endDate = periodPlan.getEndDate();//合作结束日

            DateTime start = new DateTime(startDate);
            DateTime end = new DateTime(endDate);
            int months = Months.monthsBetween(start, end).getMonths();
            for (int i =0;i<months;i++) {
                DateTime periodStartDate = start.plusMonths(i);
                DateTime periodEndDate = start.plusMonths(i+1).minusSeconds(1);//每周期结束日
                DriverTotalPaymentListVo driverTotalPaymentListVo = assembleDriverTotalPaymentListVo(carId, periodStartDate.toDate(), periodEndDate.toDate());
                String status = driverTotalPaymentListVo.getReceivedAmount().compareTo(periodPlan.getAmount()) >= 0 ? "当期结清" : "当期未结清";
                driverTotalPaymentListVo.setStatus(status);
                driverTotalPaymentListVo.setDueAmount(periodPlan.getAmount());

                DriverTotalPaymentListVoList.add(driverTotalPaymentListVo);
            }

        }//返回司机个人缴费总览月租

        return ServerResponse.createBySuccess(DriverTotalPaymentListVoList);
    }

    private DriverTotalPaymentListVo assembleDriverTotalPaymentListVo(Integer carId, Date startDate, Date endDate) {
        DriverTotalPaymentListVo driverPeriodGeneralListVo = new DriverTotalPaymentListVo();
        driverPeriodGeneralListVo.setDueDate(DateTimeUtil.dateToStr(startDate, "yyyy-MM-dd"));
        List<PeriodPayment> periodPaymentList = periodPaymentMapper.selectListByCarId(carId, startDate, endDate);

        List<PaymentDetailVo> paymentDetailVoList = Lists.newArrayList();
        BigDecimal receivedAmount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(periodPaymentList)) {
            for (PeriodPayment periodPayment : periodPaymentList) {
                receivedAmount = receivedAmount.add(periodPayment.getPayment());
                PaymentDetailVo paymentDetailVo = assemblePaymentDetailVo(periodPayment);
                paymentDetailVoList.add(paymentDetailVo);
            }
        } else {
            //如果为空，前端不好渲染，所以添加一个带有付款时间的空对象
            PaymentDetailVo paymentDetailVo = new PaymentDetailVo();
            paymentDetailVo.setPayment(BigDecimal.ZERO);
            paymentDetailVo.setPayTime(DateTimeUtil.dateToStr(startDate));
            paymentDetailVo.setPaymentPlatform("-");
            paymentDetailVo.setComment("-");
            paymentDetailVoList.add(paymentDetailVo);
        }
        driverPeriodGeneralListVo.setReceivedAmount(receivedAmount);
        driverPeriodGeneralListVo.setPaymentDetailVoList(paymentDetailVoList);
        return driverPeriodGeneralListVo;
    }

    private PaymentDetailVo assemblePaymentDetailVo(PeriodPayment periodPayment) {
        PaymentDetailVo paymentDetailVo = new PaymentDetailVo();
        Driver driver = driverMapper.selectByPrimaryKey(periodPayment.getDriverId());

        paymentDetailVo.setId(periodPayment.getId());
        paymentDetailVo.setPayer(driver.getName());
        paymentDetailVo.setPaymentStatus(Const.PlatformStatus.codeOf(periodPayment.getPlatformStatus()).getDesc());
        paymentDetailVo.setPayment(periodPayment.getPayment());
        paymentDetailVo.setPaymentPlatformCode(periodPayment.getPaymentPlatform());
        paymentDetailVo.setPaymentPlatform(Const.PaymentPlatform.codeOf(periodPayment.getPaymentPlatform()).getDesc());
        paymentDetailVo.setPayTime(DateTimeUtil.dateToStr(periodPayment.getPayTime()));
        paymentDetailVo.setComment(periodPayment.getComment());
        paymentDetailVo.setUpdateTime(DateTimeUtil.dateToStr(periodPayment.getUpdateTime()));
        return paymentDetailVo;
    }

    private PeriodPaymentListVo assemblePeriodPaymentListVo(PeriodPayment periodPayment,Date startDate,Date endDate) {
        PeriodPaymentListVo periodPaymentListVo = new PeriodPaymentListVo();
        periodPaymentListVo.setDriverId(periodPayment.getDriverId());
        Car car = carMapper.selectByPrimaryKey(periodPayment.getCarId());
        Driver driver = driverMapper.selectByPrimaryKey(periodPayment.getDriverId());

        BigDecimal driverDueAmount = getDriverDueAmount(periodPayment.getDriverId(), startDate, endDate);
        periodPaymentListVo.setDueAmount(driverDueAmount);
        periodPaymentListVo.setCarId(periodPayment.getCarId());
        periodPaymentListVo.setDriverName(driver.getName());
        periodPaymentListVo.setPhoneNum(driver.getPersonalPhone());
        periodPaymentListVo.setPlateNum(car.getPlateNumber());
        periodPaymentListVo.setCarName(car.getName());
        periodPaymentListVo.setConfirmedPayment(periodPayment.getPayment());
        return periodPaymentListVo;
    }


    private PeriodPaymentGeneralListVo assemblePeriodPaymentGeneralListVo(Date startDate,Date endDate,Integer coModelType,Integer branch) {
        PeriodPaymentGeneralListVo periodPaymentGeneralListVo = new PeriodPaymentGeneralListVo();
        if (Const.CoModel.HIRE_PURCHASE_WEEK.getCode() == coModelType) {
            String year = DateTimeUtil.getYearWeek(startDate).toString().substring(0, 4);
            String week = DateTimeUtil.getYearWeek(startDate).toString().substring(4, 6);
            week = week.equals("00") ? "52" : week;
            periodPaymentGeneralListVo.setDate(year + " 年" + " 第" + week + "周");
        } else {
            periodPaymentGeneralListVo.setDate(DateTimeUtil.dateToStr(startDate,"yyyy 年 MM 月"));
        }
        periodPaymentGeneralListVo.setStartDate(DateTimeUtil.dateToStr(startDate,"yyyy-MM-dd"));
        periodPaymentGeneralListVo.setEndDate(DateTimeUtil.dateToStr(endDate,"yyyy-MM-dd"));


        BigDecimal amountReceivable = coModelMapper.findAmountReceivable(startDate, endDate,coModelType,branch);
//        BigDecimal amountReceivable = BigDecimal.ZERO;
//        List<Driver> driverList = driverMapper.selectDriverReceivableGroupByCarId(startDate, endDate, coModelType, null);
//        for (Driver driver : driverList) {
//            BigDecimal driverDueAmount = getDriverDueAmount(driver.getId(), startDate, endDate);
//            amountReceivable = amountReceivable.add(driverDueAmount);
//        }

        BigDecimal amountReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, null,branch);
        BigDecimal wechatReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.wechat.getCode(),branch);
        BigDecimal alipayReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.alipay.getCode(),branch);
        BigDecimal pinganReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.pingan.getCode(),branch);
        BigDecimal ccbReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.ccb.getCode(),branch);
        BigDecimal cmbReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.cmb.getCode(),branch);
        BigDecimal posReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.pos.getCode(),branch);
        Integer driverNoReceivable = driverMapper.selectDriverReceivable(startDate, endDate, coModelType, null,branch).size();
        Integer driverNoReceived = driverMapper.selectDriverReceived(startDate, endDate, coModelType,null,branch).size();
        amountReceivable = amountReceivable == null ? BigDecimal.ZERO : amountReceivable;
        amountReceived = amountReceived == null ? BigDecimal.ZERO : amountReceived;
        periodPaymentGeneralListVo.setAmountReceivable(amountReceivable);
        periodPaymentGeneralListVo.setAmountReceived(amountReceived);
        periodPaymentGeneralListVo.setDifference(BigDecimalUtil.sub(amountReceivable.doubleValue(), amountReceived.doubleValue()));
        periodPaymentGeneralListVo.setWechatReceived(wechatReceived);
        periodPaymentGeneralListVo.setAlipayReceived(alipayReceived);
        periodPaymentGeneralListVo.setPosReceived(posReceived);
        periodPaymentGeneralListVo.setPinganReceived(pinganReceived);
        periodPaymentGeneralListVo.setCcbReceived(ccbReceived);
        periodPaymentGeneralListVo.setCmbReceived(cmbReceived);
        periodPaymentGeneralListVo.setDriverNoReceivable(driverNoReceivable);
        periodPaymentGeneralListVo.setDriverNoReceived(driverNoReceived);


        return periodPaymentGeneralListVo;
    }

    private BigDecimal getDriverDueAmount(Integer driverId, Date startDate, Date endDate) {
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
        List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(driver.getCoModelId());
        BigDecimal totalAmount= coModel.getTotalAmount();
        BigDecimal finalAmount = coModel.getFinalAmount();
        BigDecimal downAmount = coModel.getDownAmount();
        //应收
        BigDecimal receivable = totalAmount.subtract(finalAmount).subtract(downAmount);

        //已收
        BigDecimal received = periodPaymentMapper.findTotalReceivedByCarId(driver.getCarId());

        //差额
        BigDecimal dueAmount = receivable.subtract(received);

        if (CollectionUtils.isNotEmpty(periodPlanList)) {
            for (PeriodPlan periodPlan : periodPlanList) {
                Date periodStartDate = periodPlan.getStartDate();
                Date periodEndDate = periodPlan.getEndDate();
                if (startDate.compareTo(periodStartDate)>=0 && startDate.compareTo(periodEndDate)<=0) {
                    BigDecimal amount = periodPlan.getAmount();
                    //如果差额小于应收额则收差额
                    dueAmount = dueAmount.compareTo(amount) <= 0 ? dueAmount : amount;

                    if (coModel.getPeriodEndDate().getTime() <= endDate.getTime()) {
                        dueAmount = dueAmount.add(coModel.getFinalAmount());
                    }

                }
            }
        }
        return dueAmount;
    }

}
