package com.maple.util;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.maple.jo.FinishOrder;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/6/19.
 */
public class CrawlerUtil {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerUtil.class);

    private static String LOGINURL = PropertiesUtil.getProperty("period.bank.login");
    private static String DOWNLOADURL = PropertiesUtil.getProperty("period.bank.download");
    private static WebClient webClient = null;
    private DecimalFormat decimalFormat = new DecimalFormat("0,000.00");


    public static List<Map<String, Object>> bankStatement(Date startDate, Date endDate) throws Exception {
        webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        URL url = new URL(LOGINURL);

        HtmlPage page = webClient.getPage(url);
        page.executeJavaScript("Object.defineProperty(navigator,'platform',{get:function(){return 'Win32';}});");
        CrawlerUtil crawlerUtil = new CrawlerUtil();
        try {
            crawlerUtil.bankLogin(page);
        } catch (Exception e) {
            logger.error("登录银行抓取数据失败!");

            return null;
        }


        return crawlerUtil.statement(webClient, startDate, endDate);
    }

    private boolean bankLogin(HtmlPage page) throws Exception {
        Thread.sleep(7*1000);
        HtmlElement userName = page.getHtmlElementById("userName");
        HtmlElement password = page.getHtmlElementById("pwdObject1-input");
        HtmlElement loginBtn = page.getHtmlElementById("login_btn");
        userName.focus();
        userName.type("18584050216a");
        page.getHtmlElementById("pwdObject1-btn").click();
        page.getHtmlElementById("pa_ui_keyboard_close").click();
        password.focus();
        password.type("jiandandemima1");
        loginBtn.click();
        Thread.sleep(4000);
        String result = page.asText();
        return result.contains("冉伟");
    }

    private List<Map<String, Object>> statement(WebClient webClient, Date startDate, Date endDate) throws Exception {

        //转换日期
        String start = DateTimeUtil.dateToStr(startDate, "yyyyMMdd");
        String end = DateTimeUtil.dateToStr(endDate, "yyyyMMdd");

        //生成POST请求
        WebRequest webRequest = new WebRequest(new URL(DOWNLOADURL), HttpMethod.POST);
        List<NameValuePair> reqParams = Lists.newArrayList();

        //设置请求参数
        reqParams.add(new NameValuePair("pageNum", "1"));
        reqParams.add(new NameValuePair("pageSize", "99999"));
        reqParams.add(new NameValuePair("accNo", "6230580000148117729"));
        reqParams.add(new NameValuePair("currType", "RMB"));
        reqParams.add(new NameValuePair("startDate", start));
        reqParams.add(new NameValuePair("endDate", end));
        webRequest.setRequestParameters(reqParams);
        //发送请求
        Page downloadPage = webClient.getPage(webRequest);

        //等待加载
        Thread.sleep(5 * 1000);
        InputStream in = downloadPage.getWebResponse().getContentAsStream();

        List<Map<String, Object>> data = Lists.newArrayList();
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Map<String, Object> repaymentMap = new HashMap<>();
            Row row = sheet.getRow(i);
            Cell timeCell = row.getCell(0);
//            cell1.setCellType(Cell.CELL_TYPE_STRING);
            Cell nameCell = row.getCell(1);
//            cell2.setCellType(Cell.CELL_TYPE_STRING);
            Cell accountCell = row.getCell(2);
//            cell3.setCellType(Cell.CELL_TYPE_STRING);
            Cell typeCell = row.getCell(3);
//            cell4.setCellType(Cell.CELL_TYPE_STRING);
            Cell amountCell = row.getCell(4);
//            cell5.setCellType(Cell.CELL_TYPE_STRING);
            Cell serialCell = row.getCell(8);
//            cell9.setCellType(Cell.CELL_TYPE_STRING);
            if (typeCell.getStringCellValue().equals("转入")) {
                repaymentMap.put("交易时间", timeCell.getStringCellValue());
                repaymentMap.put("交易方姓名", nameCell.getStringCellValue());
                repaymentMap.put("交易方账号", accountCell.getStringCellValue());
                repaymentMap.put("交易金额", new BigDecimal(amountCell.getStringCellValue()));
                repaymentMap.put("交易流水号", serialCell.getStringCellValue());
                data.add(repaymentMap);
            }
        }
        return data;
    }


    private String getGuyuInputStream(String type, String startTime, String endTime, int pageNum) throws IOException {

        String GUYU = "https://wave.xiaojukeji.com/v2/dc/"+type+"/driver?org_id=2367&start_time="+startTime+"&end_time="+endTime+"&order_type=2&size=100&page="+pageNum;
        String DOAMIN = "wave.xiaojukeji.com";
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        URL url = new URL(GUYU);
        HtmlPage page=null;

        webClient.getCookieManager().addCookie(new Cookie(DOAMIN, "wave_session", PropertiesUtil.getProperty("wave.xiaojukeji.com.session")));

        try {
            page = webClient.getPage(url);
            return page.asText();

        } catch (FailingHttpStatusCodeException e) {
            System.out.println("Session已过期,需要重新登录");
        }
        return null;
    }

    public static List<Object> getGuyuData(Class classType, String startTime, String endTime) throws IOException, ParseException {
        List<HashMap<String, Object>> mapList = Lists.newArrayList();
        List<Object> resultList=Lists.newArrayList();
        CrawlerUtil crawlerUtil = new CrawlerUtil();
        String type = classType.getSimpleName().toLowerCase();
        String  content = crawlerUtil.getGuyuInputStream(type, startTime, endTime, 1);
        mapList.addAll(crawlerUtil.getObjectList(content, classType));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(content);
        JsonNode objectData = root.path("data");
        JsonNode count = objectData.path("count");

        double totalCount = count.getDoubleValue();
        int totalPage = (int)Math.ceil(totalCount / 100);
        while (totalPage > 1) {
            content = crawlerUtil.getGuyuInputStream(type, startTime, endTime, totalPage);
            mapList.addAll(crawlerUtil.getObjectList(content, classType));
            totalPage--;
        }

        if (type.equals("finishorder")) {
            for (HashMap<String, Object> object : mapList) {
                FinishOrder newFinishOrder = crawlerUtil.assembleFinishOrder(object);
                resultList.add(newFinishOrder);
            }
        }

        return resultList;
    }

    private List<HashMap<String,Object>> getObjectList(String  content,Class classType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(content);
        JsonNode objectData = root.path("data");
        JsonNode arrayData = objectData.path("data");
        List<HashMap<String,Object>> list = mapper.readValue(arrayData, List.class);

        return list;
    }

    private FinishOrder assembleFinishOrder(HashMap<String, Object> finishOrder) throws ParseException {
        FinishOrder newFinishOrder = new FinishOrder();
        newFinishOrder.setDriverId(Long.parseLong((String)finishOrder.get("driver_id")));
        newFinishOrder.setPhone((String) finishOrder.get("phone"));
        newFinishOrder.setName((String) finishOrder.get("name"));
        newFinishOrder.setJoinModelName((String) finishOrder.get("join_model_name"));
        newFinishOrder.setCheckLevelName((String) finishOrder.get("check_level_name"));
        newFinishOrder.setDate(DateTimeUtil.strToDate((String)finishOrder.get("date"),"yyyy-MM-dd"));
        newFinishOrder.setFinishFlowfee(new BigDecimal(decimalFormat.parseObject((String)finishOrder.get("finish_flowfee")).toString()));
        newFinishOrder.setFinishFinishCnt(Integer.parseInt((String) finishOrder.get("finish_finish_cnt")));
        newFinishOrder.setFinishServeTime(new BigDecimal(decimalFormat.parseObject((String)finishOrder.get("finish_serve_time")).toString()));
        newFinishOrder.setFinishOnlineTime(new BigDecimal(decimalFormat.parseObject((String)finishOrder.get("finish_online_time")).toString()));
        newFinishOrder.setFinishWorkDistance(new BigDecimal(decimalFormat.parseObject((String)finishOrder.get("finish_work_distance")).toString()));
        newFinishOrder.setFinishFeeTime(new BigDecimal(decimalFormat.parseObject((String)finishOrder.get("finish_fee_time")).toString()));
        newFinishOrder.setFinishServeDistance(new BigDecimal(decimalFormat.parseObject((String)finishOrder.get("finish_serve_distance")).toString()));
        return newFinishOrder;
    }
}