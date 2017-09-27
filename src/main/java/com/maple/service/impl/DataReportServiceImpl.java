package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maple.dao.FinishOrderMapper;
import com.maple.jo.FinishOrder;
import com.maple.service.IDataReportService;
import com.maple.util.DateTimeUtil;
import com.maple.vo.FinishOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/24.
 */
@Service("iDataReportService")
public class DataReportServiceImpl implements IDataReportService {
    @Autowired
    private FinishOrderMapper finishOrderMapper;

    public Map finishOrderReport(String name, String phone, String startTime, String endTime, int pageStart, int pageSize) {
        Map resultMap = Maps.newHashMap();
        int pageNum = pageStart / pageSize + 1;
        PageHelper.startPage(pageNum, pageSize);
        List<FinishOrder> finishOrderList = finishOrderMapper.selectAll();
        List<FinishOrderVo> finishOrderVoList = Lists.newArrayList();
        for (FinishOrder finishOrder : finishOrderList) {
            FinishOrderVo finishOrderVo = assembleFinishOrderVo(finishOrder);
            finishOrderVoList.add(finishOrderVo);
        }
        PageInfo pageInfo = new PageInfo(finishOrderList);
        pageInfo.setList(finishOrderVoList);

        resultMap.put("recordsTotal", pageInfo.getTotal());
        resultMap.put("recordsFiltered", pageInfo.getTotal());
        resultMap.put("data", pageInfo.getList());
        return resultMap;
    }

    private FinishOrderVo assembleFinishOrderVo(FinishOrder finishOrder) {
        FinishOrderVo finishOrderVo = new FinishOrderVo();
        finishOrderVo.setId(finishOrder.getId());
        finishOrderVo.setDriverId(finishOrder.getDriverId());
        finishOrderVo.setDate(DateTimeUtil.dateToStr(finishOrder.getDate(), "yyyy-MM-dd"));
        finishOrderVo.setName(finishOrder.getName());
        finishOrderVo.setJoinModelName(finishOrder.getJoinModelName());
        finishOrderVo.setCheckLevelName(finishOrder.getCheckLevelName());
        finishOrderVo.setPhone(finishOrder.getPhone());
        finishOrderVo.setFinishFlowfee(finishOrder.getFinishFlowfee());
        finishOrderVo.setFinishFinishCnt(finishOrder.getFinishFinishCnt());
        finishOrderVo.setFinishOnlineTime(finishOrder.getFinishOnlineTime());
        finishOrderVo.setFinishServeTime(finishOrder.getFinishServeTime());
        finishOrderVo.setFinishWorkDistance(finishOrder.getFinishWorkDistance());
        finishOrderVo.setFinishServeDistance(finishOrder.getFinishServeDistance());
        finishOrderVo.setFinishFeeTime(finishOrder.getFinishFeeTime());
        return finishOrderVo;
    }
}
