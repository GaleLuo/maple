package com.maple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.CarMapper;
import com.maple.dao.FinishOrderMapper;
import com.maple.dao.TicketMapper;
import com.maple.jo.FinishOrder;
import com.maple.service.IDataReportService;
import com.maple.service.IPeriodPaymentService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.vo.FinishOrderVo;
import com.maple.vo.PeriodPaymentGeneralListVo;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/24.
 */
@Service("iDataReportService")
public class DataReportServiceImpl implements IDataReportService {
    @Autowired
    private FinishOrderMapper finishOrderMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private IPeriodPaymentService iPeriodPaymentService;

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

    @Override
    public ServerResponse carData(Integer year,Integer branch) {
        HashMap<Object, Object> data = Maps.newHashMap();
        List<Object> totalList = Lists.newArrayList();
        List<Object> addList = Lists.newArrayList();
        for (int i =1; i<=12;i++) {
            Date nextMonth = new LocalDate(year+"-"+i+"-01").plusMonths(1).toDate();
            Date thisMonth = new LocalDate(year+"-"+i+"-01").toDate();
            Integer total = carMapper.countTotalByYearMonth(nextMonth, branch);
            Integer add = carMapper.countAddByYearMonth(thisMonth, nextMonth, branch);
            totalList.add(total);
            addList.add(add);

        }
        data.put("total", totalList);
        data.put("add", addList);
        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse ticketData() {
        HashMap<Object, Object> data = Maps.newHashMap();
        List<Object> dateList = Lists.newArrayList();
        List<Object> totalList = Lists.newArrayList();
        List<Object> addList = Lists.newArrayList();
        for (int i =14; i>=1; i--) {
            Date date = new LocalDate().minusDays(i).toDate();
            Date lastDate = new LocalDate().minusDays(i+1).toDate();
            dateList.add(date.getDate() + "日");
            Integer todayIndex = ticketMapper.selectTotalIndexByDate(date);
            Integer lastDayIndex = ticketMapper.selectTotalIndexByDate(lastDate);
            Integer addIndex;
            if (todayIndex == 0 || lastDayIndex == 0) {
                addIndex = 0;
            } else {
                addIndex= todayIndex - lastDayIndex;
            }
            totalList.add(todayIndex);
            addList.add(addIndex);
        }
        data.put("date", dateList);
        data.put("total", totalList);
        data.put("add", addList);

        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse paymentData(Long date, Integer coModelType,Integer branch) {
        Date dateD = new Date(date);
        Date endDate = null;
        if (coModelType == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
            endDate = DateTimeUtil.getWeekEndDate(dateD);
        } else if (coModelType == Const.CoModel.HIRE_PURCHASE_MONTH.getCode()) {
            endDate = DateTimeUtil.getMonthEndDate(dateD);
        }
        ServerResponse serverResponse = iPeriodPaymentService.generalList(branch, date, endDate.getTime(), coModelType, 1, 1);
        Map data = (Map) serverResponse.getData();
        List<PeriodPaymentGeneralListVo> dataList = (List<PeriodPaymentGeneralListVo>) data.get("list");
        PeriodPaymentGeneralListVo periodPaymentGeneralListVo = dataList.get(0);
        BigDecimal amountRatio = BigDecimalUtil.div(periodPaymentGeneralListVo.getAmountReceived().doubleValue(),periodPaymentGeneralListVo.getAmountReceivable().doubleValue()).multiply(BigDecimal.valueOf(100));
        BigDecimal numberRatio = BigDecimalUtil.div(periodPaymentGeneralListVo.getDriverNoReceived().doubleValue(),periodPaymentGeneralListVo.getDriverNoReceivable().doubleValue()).multiply(BigDecimal.valueOf(100));
        HashMap<Object, Object> resData = Maps.newHashMap();
        resData.put("amountRatio", amountRatio);
        resData.put("numberRatio", numberRatio);
        return ServerResponse.createBySuccess(resData);
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
