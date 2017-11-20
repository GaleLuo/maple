package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.vo.PingAnBalanceListVo;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/11/4.
 */
public interface IBankService {

    ServerResponse<Map> pingAnQueryOtherBankBalance(Integer branch);

    List<Object> getGuyuData(Class classType, String startTime, String endTime) throws IOException, ParseException;

    List<Map<String, Object>> ccbAnalyze(InputStream inputStream) throws IOException;

    ServerResponse refreshPinganBalance(Integer branch, String agreementNo);

    void closeConnection();

    boolean bankLogin(Integer branch) throws Exception;

    List<Map<String, Object>> statement(Date startDate, Date endDate, Integer branch) throws Exception;
}
