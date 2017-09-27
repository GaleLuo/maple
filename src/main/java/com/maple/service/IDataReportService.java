package com.maple.service;

import java.util.Map;

/**
 * Created by Maple.Ran on 2017/7/24.
 */
public interface IDataReportService {
    Map finishOrderReport(String name, String phone, String startTime, String endTime, int pageStart, int pageSize);
}
