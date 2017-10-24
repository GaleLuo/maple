package com.maple.service.impl;

import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPlanMapper;
import com.maple.dao.TicketMapper;
import com.maple.pojo.CoModel;
import com.maple.pojo.Driver;
import com.maple.pojo.PeriodPlan;
import com.maple.pojo.Ticket;
import com.maple.service.ICoModelService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.vo.CoModelDetailVo;
import com.maple.vo.PlanDetailVo;
import com.maple.vo.coModelSummaryVo;
import com.sun.java.swing.ui.ToggleActionPropertyChangeListener;
import com.sun.tools.javadoc.Start;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/4.
 */
@Service("iCoModelService")
public class CoModelServiceImpl implements ICoModelService {
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private PeriodPlanMapper periodPlanMapper;


    public ServerResponse save(Integer userId, CoModel coModel) {
        List<Integer> carIdList = driverMapper.selectCarIdListByUserId(userId);
        if (carIdList.contains(coModel.getCarId())) {
            int result = coModelMapper.insert(coModel);
            if (result > 0) {
                return ServerResponse.createBySuccess(coModel.getId());
            }
        }
        return ServerResponse.createByErrorMessage("新增合作模式失败");
    }


    public ServerResponse<CoModelDetailVo> detail(Integer userId, Integer carId) {
        if (userId != null) {
            List<Integer> carIdList = driverMapper.selectCarIdListByUserId(userId);
            if (carIdList.contains(carId)) {
                CoModel coModel = coModelMapper.selectByCarId(carId);
                if (coModel != null) {
                    return ServerResponse.createBySuccess(assembleCoModelDetailVo(coModel));
                }
                return ServerResponse.createByErrorMessage("查询失败");
            }
        }
        CoModel coModel = coModelMapper.selectByCarId(carId);
        if (coModel != null) {
            return ServerResponse.createBySuccess(assembleCoModelDetailVo(coModel));
        }
        return ServerResponse.createByErrorMessage("查询失败");
    }

    public ServerResponse update(CoModel coModel) {
        int result = coModelMapper.updateByPrimaryKeySelective(coModel);
        if (result > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    public ServerResponse summary(Integer coModelId) {
        CoModel coModel = coModelMapper.selectByPrimaryKey(coModelId);
        if (coModel != null) {
            return ServerResponse.createBySuccess(assembleCoModelSummaryVo(coModel));
        }
        return ServerResponse.createByErrorMessage("查询失败");
    }

    public ServerResponse addOrUpdate(Integer driverId,Integer coModelId,Integer carId,Long periodStartDate,Long periodEndDate, Integer modelType, BigDecimal totalAmount, BigDecimal downAmount, BigDecimal finalAmount, Long periodPlanStartDate, Integer periodNum, String comment) {
        if (modelType == null) {
            return ServerResponse.createByErrorMessage("请选择合作模式");
        }
        if (!modelType.equals(Const.CoModel.FULL_PAYMENT.getCode())&&periodPlanStartDate==null) {
            return ServerResponse.createByErrorMessage("请选择付款起始日期");
        } else if (modelType.equals(Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) && new DateTime(periodPlanStartDate).getDayOfWeek() != 2) {
            return ServerResponse.createByErrorMessage("请选择星期二为付款起始日");
        }
        //前端传入的数据始是上午8点，后端改为当天0点
        Date periodPlanStartDateValue = new DateTime(periodPlanStartDate).millisOfDay().withMinimumValue().toDate();

        CoModel coModel = new CoModel();
        BigDecimal amount = BigDecimal.ZERO;
        PeriodPlan periodPlan = new PeriodPlan();
        comment = StringUtils.isNotEmpty(comment) ? comment : null;
        Date periodStart= new Date(periodStartDate);
        Date periodEnd = new Date(periodEndDate);
        coModel.setId(coModelId);
        coModel.setCarId(carId);
        coModel.setPeriodStartDate(periodStart);
        coModel.setPeriodEndDate(periodEnd);
        coModel.setDeadline(periodEnd);//todo 后期加入前端真实数据
        coModel.setModelType(modelType);
        coModel.setTotalAmount(totalAmount);
        coModel.setDownAmount(downAmount);
        coModel.setFinalAmount(finalAmount);
        coModel.setComment(comment);
        coModel.setPeriodNum(periodNum);

        if (coModelId == null) {
            coModelMapper.insertSelective(coModel);
        } else {
            coModelMapper.updateByPrimaryKeySelective(coModel);
        }

        if (driverId != null) {
            Driver driver = driverMapper.selectByPrimaryKey(driverId);
            if (driver != null) {
                Driver driver1 = new Driver();
                driver1.setId(driver.getId());
                driver1.setCoModelId(coModel.getId());
                driverMapper.updateByPrimaryKeySelective(driver1);
            }
        }

        periodPlan.setCoModelId(coModel.getId());
        if (Const.CoModel.HIRE_PURCHASE_WEEK.getCode() == modelType) {
            amount = (totalAmount.subtract(downAmount).subtract(finalAmount));
            amount = BigDecimalUtil.div(amount.doubleValue(), periodNum);
            periodPlan.setAmount(amount.divide(BigDecimal.valueOf(4)));
            periodPlan.setStartDate(periodPlanStartDateValue);
            Date periodPlanEndDate = DateTimeUtil.getWeekStartDate(new DateTime(periodPlanStartDateValue).plusMonths(periodNum).toDate());
            periodPlan.setEndDate(periodPlanEndDate);

        } else if (Const.CoModel.HIRE_PURCHASE_MONTH.getCode() == modelType || Const.CoModel.RENT.getCode() == modelType) {
            amount = (totalAmount.subtract(downAmount).subtract(finalAmount));
            amount = BigDecimalUtil.div(amount.doubleValue(), periodNum);
            periodPlan.setAmount(amount);
            periodPlan.setStartDate(periodPlanStartDateValue);
            Date periodPlanEndDate = new DateTime(periodPlanStartDateValue).plusMonths(periodNum).toDate();
            periodPlan.setEndDate(periodPlanEndDate);
        } else {
            //全款则直接返回成功
            return ServerResponse.createBySuccess("操作成功", coModel.getId());
        }
        List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(coModelId);
        if (CollectionUtils.isEmpty(periodPlanList)) {
            periodPlanMapper.insert(periodPlan);
        } else {
            PeriodPlan periodPlan1 = periodPlanList.get(0);
            periodPlan.setId(periodPlan1.getId());
            periodPlanMapper.updateByPrimaryKeySelective(periodPlan);
        }

        return ServerResponse.createBySuccess("操作成功", coModel.getId());

    }

    private coModelSummaryVo assembleCoModelSummaryVo(CoModel coModel) {
        coModelSummaryVo coModelSummaryVo = new coModelSummaryVo();
        List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(coModel.getId());
        if (CollectionUtils.isNotEmpty(periodPlanList)) {
            PeriodPlan periodPlan = periodPlanList.get(0);
            coModelSummaryVo.setPeriodPlanStartDate(DateTimeUtil.dateToStr(periodPlan.getStartDate()));
        }
        coModelSummaryVo.setCoModelId(coModel.getId());
        coModelSummaryVo.setModelType(coModel.getModelType().toString());
        coModelSummaryVo.setTotalAmount(coModel.getTotalAmount());
        coModelSummaryVo.setDownAmount(coModel.getDownAmount());
        coModelSummaryVo.setFinalAmount(coModel.getFinalAmount());
        coModelSummaryVo.setPeriodNum(coModel.getPeriodNum());
        String coModelStartDate = DateTimeUtil.dateToStr(coModel.getPeriodStartDate());
        String coModelEndDate = DateTimeUtil.dateToStr(coModel.getPeriodEndDate());
        String[] starEndDateArray = {coModelStartDate,coModelEndDate};
        coModelSummaryVo.setPeriodDateArray(starEndDateArray);
        coModelSummaryVo.setComment(coModel.getComment());

        return coModelSummaryVo;

    }


    private CoModelDetailVo assembleCoModelDetailVo(CoModel coModel) {
        CoModelDetailVo coModelDetailVo = new CoModelDetailVo();
        coModelDetailVo.setId(coModel.getId());
        coModelDetailVo.setCarId(coModel.getCarId());
        coModelDetailVo.setModelTypeCode(coModel.getModelType());
        coModelDetailVo.setModelTypeDesc(Const.CoModel.codeOf(coModel.getModelType()).getDesc());
        coModelDetailVo.setDownAmount(coModel.getDownAmount());
        coModelDetailVo.setTotalAmount(coModel.getTotalAmount());
        List<PlanDetailVo> planDetailVoList = Lists.newArrayList();
        List<PeriodPlan> periodPlanList = periodPlanMapper.selectByCoModelId(coModel.getId());//查询出分期计划列表
        for (PeriodPlan periodPlan : periodPlanList) {
            PlanDetailVo planDetailVo = assemblePeriodDetailVo(periodPlan);
            planDetailVoList.add(planDetailVo);
        }

        coModelDetailVo.setPlanDetailVoList(planDetailVoList);
        coModelDetailVo.setFinalAmount(coModel.getFinalAmount());
        coModelDetailVo.setManagementFee(coModel.getManagementFee());
        coModelDetailVo.setDeadline(DateTimeUtil.dateToStr(coModel.getDeadline()));
        coModelDetailVo.setComment(coModel.getComment());
        return coModelDetailVo;
    }

    private PlanDetailVo assemblePeriodDetailVo(PeriodPlan periodPlan) {
        PlanDetailVo planDetailVo = new PlanDetailVo();
        planDetailVo.setPlanAmount(periodPlan.getAmount());//分期计划付款金额
        planDetailVo.setPlanStartDate(DateTimeUtil.dateToStr(periodPlan.getStartDate(),"yyyy-MM-dd hh:mm:ss"));//分期起始日
        planDetailVo.setPlanEndDate(DateTimeUtil.dateToStr(periodPlan.getEndDate(),"yyyy-MM-dd hh:mm:ss"));//分期结束日
        return planDetailVo;
    }
}
