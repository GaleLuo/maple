package com.maple.service.impl;

import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.CoModelMapper;
import com.maple.dao.DriverMapper;
import com.maple.dao.PeriodPlanMapper;
import com.maple.pojo.CoModel;
import com.maple.pojo.PeriodPlan;
import com.maple.service.ICoModelService;
import com.maple.util.DateTimeUtil;
import com.maple.vo.CoModelDetailVo;
import com.maple.vo.PlanDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
