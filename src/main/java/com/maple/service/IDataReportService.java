package com.maple.service;

import com.maple.common.ServerResponse;

import java.util.Date;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/24.
 */
public interface IDataReportService {
    Map finishOrderReport(String name, String phone, String startTime, String endTime, int pageStart, int pageSize);

    ServerResponse carData(Integer year,Integer branch);

    ServerResponse ticketData();

    ServerResponse paymentData(Date date, Integer coModelType,Integer branch);

}
