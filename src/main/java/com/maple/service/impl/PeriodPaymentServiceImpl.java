package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.*;
import com.maple.pojo.*;
import com.maple.service.IPeriodPaymentService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.vo.*;
import com.mysql.jdbc.log.Log4JLogger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(PeriodPaymentServiceImpl.class);


    public ServerResponse addOrUpdate(User user, Integer driverId, BigDecimal payment, Integer paymentPlatform, String platformNum,String payTime){

        if (driverId == null || payment == null || paymentPlatform == null || StringUtils.isBlank(payTime)) {
            return ServerResponse.createByErrorMessage("请表单填写完整");
        }
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        PeriodPayment newPeriodPayment = new PeriodPayment();
        newPeriodPayment.setDriverId(driverId);
        newPeriodPayment.setCarId(driver.getCarId());
        newPeriodPayment.setPayment(payment);
        newPeriodPayment.setPaymentPlatform(paymentPlatform);
        newPeriodPayment.setPlatformNumber(platformNum);
        Date payDate = DateTimeUtil.webStrToDate(payTime);

        newPeriodPayment.setPayTime(payDate);
        newPeriodPayment.setPlatformStatus(Const.PlatformStatus.UNCONFIRMED.getCode());
        String comment = "添加人:" + user.getName();
        newPeriodPayment.setComment(comment);
        logger.error("添加人:{},添加时间:{}",user.getName(),new Date());
        int i = periodPaymentMapper.insert(newPeriodPayment);
        if (i > 0) {
            return ServerResponse.createBySuccessMessage("添加数据成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }


    public ServerResponse<PageInfo> list(String date, String driverName, Integer coModelType,Integer payStatus, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
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
            driverList = driverMapper.selectDriverReceivable(startDate, endDate, coModelType, driverName);
        } else if (payStatus == 0) {
            driverList = driverMapper.selectDriverReceivedPartly(startDate,endDate,coModelType,driverName);
        } else {
            driverList = driverMapper.selectDriverNotReceived(startDate, endDate, coModelType, driverName);
        }

        for (Driver driver : driverList) {
            Integer driverId = driver.getId();
            PeriodPayment periodPayment = periodPaymentMapper.selectWeekSumByParams(driverId, startDate, endDate);
            //如果结果为空,则没有付款记录
            if (periodPayment == null) {
                periodPayment = new PeriodPayment();
                periodPayment.setCarId(driver.getCarId());
                periodPayment.setDriverId(driverId);
            }
            PeriodPaymentListVo periodPaymentListVo = assemblePeriodPaymentListVo(periodPayment,startDate,endDate);
            periodPaymentListVoList.add(periodPaymentListVo);

        }
        PageInfo pageInfo = new PageInfo(driverList);
        pageInfo.setList(periodPaymentListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse generalList(Long start,Long end,Integer coModelType, int pageNum, int pageSize) {
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
                PeriodPaymentGeneralListVo periodPaymentGeneralListVo = assemblePeriodPaymentGeneralListVo(startDate, endDate, coModelType);
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
                PeriodPaymentGeneralListVo periodPaymentGeneralListVo = assemblePeriodPaymentGeneralListVo(startDate, endDate, coModelType);
                periodPaymentGeneralListVoList.add(periodPaymentGeneralListVo);
                startDate = new DateTime(startDate).plusMonths(1).toDate();
            }
            //如果选择当月，增加当月，截止当天
            DateTime now = new DateTime();
            DateTime endDateTime = new DateTime(end);
            if (endDateTime.getMonthOfYear() == now.getMonthOfYear()) {
                PeriodPaymentGeneralListVo periodPaymentGeneralListVo = assemblePeriodPaymentGeneralListVo(endDateTime.toDate(), now.toDate(), coModelType);
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

            while (!end.isEqual(start)) {
                DateTime periodEndDate = start.plusMonths(1).minusSeconds(1);//每周期结束日
                DriverTotalPaymentListVo driverTotalPaymentListVo = assembleDriverTotalPaymentListVo(carId, start.toDate(), periodEndDate.toDate());
                String status = driverTotalPaymentListVo.getReceivedAmount().compareTo(periodPlan.getAmount()) >= 0 ? "当期结清" : "当期未结清";
                driverTotalPaymentListVo.setStatus(status);
                driverTotalPaymentListVo.setDueAmount(periodPlan.getAmount());

                DriverTotalPaymentListVoList.add(driverTotalPaymentListVo);
                start = start.plusMonths(1);
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
        BigDecimal payment = periodPayment.getPayment() == null ? BigDecimal.ZERO : periodPayment.getPayment();
        periodPaymentListVo.setPayment(payment);
        return periodPaymentListVo;
    }


    private PeriodPaymentGeneralListVo assemblePeriodPaymentGeneralListVo(Date startDate,Date endDate,Integer coModelType) {
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


        BigDecimal amountReceivable = coModelMapper.findAmountReceivable(startDate, endDate,coModelType);
//        BigDecimal amountReceivable = BigDecimal.ZERO;
//        List<Driver> driverList = driverMapper.selectDriverReceivableGroupByCarId(startDate, endDate, coModelType, null);
//        for (Driver driver : driverList) {
//            BigDecimal driverDueAmount = getDriverDueAmount(driver.getId(), startDate, endDate);
//            amountReceivable = amountReceivable.add(driverDueAmount);
//        }

        BigDecimal amountReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, null);
        BigDecimal wechatReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.wechat.getCode());
        BigDecimal alipayReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.alipay.getCode());
        BigDecimal bankReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.bank.getCode());
        BigDecimal cashReceived = periodPaymentMapper.findAmountReceived(startDate, endDate, coModelType, Const.PaymentPlatform.cash.getCode());
        Integer driverNoReceivable = driverMapper.selectDriverReceivable(startDate, endDate, coModelType, null).size();
        Integer driverNoReceived = driverMapper.selectDriverReceived(startDate, endDate, coModelType,null).size();
        amountReceivable = amountReceivable == null ? BigDecimal.ZERO : amountReceivable;
        amountReceived = amountReceived == null ? BigDecimal.ZERO : amountReceived;
        periodPaymentGeneralListVo.setAmountReceivable(amountReceivable);
        periodPaymentGeneralListVo.setAmountReceived(amountReceived);
        periodPaymentGeneralListVo.setDifference(BigDecimalUtil.sub(amountReceivable.doubleValue(), amountReceived.doubleValue()));
        periodPaymentGeneralListVo.setWechatReceived(wechatReceived);
        periodPaymentGeneralListVo.setAlipayReceived(alipayReceived);
        periodPaymentGeneralListVo.setBankReceived(bankReceived);
        periodPaymentGeneralListVo.setCashReceived(cashReceived);
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
                if (periodStartDate.compareTo(startDate) <= 0 || periodEndDate.compareTo(endDate) >= 0) {
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
