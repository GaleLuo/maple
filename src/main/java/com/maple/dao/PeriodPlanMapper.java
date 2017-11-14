package com.maple.dao;

import com.maple.pojo.PeriodPlan;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PeriodPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodPlan record);

    int insertSelective(PeriodPlan record);

    PeriodPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodPlan record);

    int updateByPrimaryKey(PeriodPlan record);

    List<PeriodPlan> selectByCoModelId(Integer coModelId);

    PeriodPlan selectByDateAndCoModel(@Param("startDate") Date startDate, @Param("coModelId") Integer coModelId);
}