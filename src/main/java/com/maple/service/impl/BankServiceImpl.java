package com.maple.service.impl;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.AccountMapper;
import com.maple.jo.FinishOrder;
import com.maple.service.IBankService;
import com.maple.util.BigDecimalUtil;
import com.maple.util.DateTimeUtil;
import com.maple.util.JsonUtil;
import com.maple.util.PropertiesUtil;
import com.maple.vo.PingAnBalanceListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Maple.Ran on 2017/6/19.
 */
@Service("iBankService")
public class BankServiceImpl implements IBankService{
    @Autowired
    private static AccountMapper accountMapper;


    private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

    private static String LOGINURL = PropertiesUtil.getProperty("bank.pingan.login");
    private static String DOWNLOADURL = PropertiesUtil.getProperty("bank.pingan.download");
    private static String OTHER_BANK_BALANCE = PropertiesUtil.getProperty("bank.pingan.otherbankbalance");
    private static String CASH_CONCENTRATION = PropertiesUtil.getProperty("bank.pingan.cashconcentration");
    private static String REFRESH_APPLY = PropertiesUtil.getProperty("bank.pingan.refresh.apply");
    private static String REFRESH_RESULT = PropertiesUtil.getProperty("bank.pingan.refresh.result");
    private static WebClient webClient = null;
    private DecimalFormat decimalFormat = new DecimalFormat("0,000.00");
    private static final String PINGAN_CHENGDU_ACCOUNT = "6230580000148117729";
    private static final String PINGAN_CHENGDU_USERNAME = "18584050216a";
    private static final String PINGAN_CHENGDU_PASSWORD = "jiandandemima1";
    private static final String PINGAN_KUNMING_ACCOUNT = "6230582000068274151";
    private static final String PINGAN_KUNMING_USERNAME = "513221198207290212";
    private static final String PINGAN_KUNMING_PASSWORD = "abc831328";



    public ServerResponse pingAnStatement(Integer branch, Date startDate, Date endDate) throws Exception {
        try {
            bankLogin(branch);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("抓取数据失败,请刷新页面");
        }finally {
            //释放内存
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
            System.gc();
        }

        return ServerResponse.createBySuccess(statement(webClient, startDate, endDate,branch));
    }

    public ServerResponse<Map> pingAnQueryOtherBankBalance(Integer branch) {
        Map data = Maps.newHashMap();
        try {
            bankLogin(branch);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("抓取数据失败,请刷新页面");
        }
        List<PingAnBalanceListVo> pingAnBalanceListVos;
        Map resultMap = null;
        try {
            resultMap = queryOtherBankBalance(1);

            //将第一页转换为对象列表
            pingAnBalanceListVos = assemblePingAnBalanceListVo(resultMap);

            Integer accCount = (Integer) resultMap.get("accCount");
            data.put("totalAccountNum", accCount);

            //计算出总页数
            Double totalPage = Math.ceil(accCount.doubleValue() / 20d);
            //
            for (int i =2; i<=totalPage; i++) {
                Map map = queryOtherBankBalance(i);
                pingAnBalanceListVos.addAll(assemblePingAnBalanceListVo(map));
            }
            //本周(星期二为起始日)资金归集结果键值对
            Map map = queryCashConcentrationResult(null);
            int failAccountNum = 0;
            for (PingAnBalanceListVo pingAnBalanceListVo : pingAnBalanceListVos) {
                String acctNo = pingAnBalanceListVo.getAcctNo();
                BigDecimal amount = (BigDecimal) map.get(acctNo);
                if (amount != null) {
                    pingAnBalanceListVo.setAmount(amount);
                } else {
                    pingAnBalanceListVo.setAmount(BigDecimal.ZERO);
                    failAccountNum++;
                }
            }
            data.put("failAccountNum", failAccountNum);
        } catch (IOException | InterruptedException | ParseException e) {
            return ServerResponse.createByErrorMessage("数据处理错误");
        }finally {
            //释放内存
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
            System.gc();
        }
//        Collections.sort(pingAnBalanceListVos);
        data.put("data", pingAnBalanceListVos);

        return ServerResponse.createBySuccess(data);
    }

    //todo 添加本周已扣刷新
    public ServerResponse refreshPinganBalance(Integer branch,String agreementNo) {

        PingAnBalanceListVo pingAnBalanceListVo = new PingAnBalanceListVo();
        try {
            WebRequest webRequest = new WebRequest(new URL(REFRESH_APPLY), HttpMethod.POST);
            List<NameValuePair> reqParams = Lists.newArrayList();
            reqParams.add(new NameValuePair("channelType", "d"));
            reqParams.add(new NameValuePair("responseDataType", "JSON"));
            reqParams.add(new NameValuePair("agreementNo", agreementNo));
            webRequest.setRequestParameters(reqParams);
            Page page = webClient.getPage(webRequest);

            Thread.sleep(1000);

            InputStream inputStream = page.getWebResponse().getContentAsStream();
            Map applyResult = JsonUtil.pingAnBalance(inputStream);
            String errCode = (String) applyResult.get("errCode");
            String oldChannelSeqNo = (String) applyResult.get("oldChannelSeqNo");
            //如果需要登录,则重新登陆
            if (errCode.equals("400")) {
                try {
                    bankLogin(branch);
                } catch (Exception e) {
                    return ServerResponse.createByErrorMessage("抓取数据失败,请刷新页面");
                }
                refreshPinganBalance(branch, agreementNo);
            }

            Thread.sleep(8 * 1000);

            webRequest = new WebRequest(new URL(REFRESH_RESULT), HttpMethod.POST);
            reqParams = Lists.newArrayList();
            reqParams.add(new NameValuePair("channelType", "d"));
            reqParams.add(new NameValuePair("responseDataType", "JSON"));
            reqParams.add(new NameValuePair("oldChannelSeqNo", oldChannelSeqNo));
            webRequest.setRequestParameters(reqParams);
            page = webClient.getPage(webRequest);

            Thread.sleep(1000);

            inputStream = page.getWebResponse().getContentAsStream();
            System.out.println(page.getWebResponse().getContentAsString());
            Map result = JsonUtil.pingAnBalance(inputStream);
            Map balanceDtoMap = (Map) result.get("balanceDtoMap");
            String acctNo = null;
            for (Object o : balanceDtoMap.keySet()) {
                acctNo = (String) o;
                Map acctDetail = (Map) balanceDtoMap.get(acctNo);
                String updateTime = (String) acctDetail.get("updateTime");
                List subAccountList = (List) acctDetail.get("subAccountList");
                Map balanceMap = (Map) subAccountList.get(0);
                String balance = (String) balanceMap.get("balance");
                pingAnBalanceListVo.setBalance(new BigDecimal(decimalFormat.parseObject(balance).toString()));
                pingAnBalanceListVo.setUpdateTime(updateTime);
            }

            Map map = queryCashConcentrationResult(acctNo);
            BigDecimal amount = (BigDecimal) map.get(acctNo);
            if (amount != null) {
                pingAnBalanceListVo.setAmount(amount);
            } else {
                pingAnBalanceListVo.setAmount(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("程序运行错误,更新失败");
        }finally {
            //释放内存
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
            System.gc();
        }

        return ServerResponse.createBySuccess(pingAnBalanceListVo);
    }

    public void closeConnection() {
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        webClient.close();
        System.gc();
    }

    private Map queryCashConcentrationResult(String payAcc) throws IOException, InterruptedException, ParseException {
        WebRequest webRequest = new WebRequest(new URL(CASH_CONCENTRATION));
        List<NameValuePair> reqParams = Lists.newArrayList();
        Date startDate = DateTimeUtil.getWeekStartDate(new Date());
        Date endDate = new Date();
        reqParams.add(new NameValuePair("end_date", DateTimeUtil.dateToStr(endDate, "yyyyMMdd")));
        reqParams.add(new NameValuePair("rec_acc", "all"));
        reqParams.add(new NameValuePair("pay_acc", payAcc));
        reqParams.add(new NameValuePair("queryMode", "00"));
        reqParams.add(new NameValuePair("start_date", DateTimeUtil.dateToStr(startDate, "yyyyMMdd")));
        webRequest.setRequestParameters(reqParams);
        Page page = webClient.getPage(webRequest);

        Thread.sleep(1000);

        InputStream inputStream = page.getWebResponse().getContentAsStream();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, BigDecimal> map = new HashMap<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String acctNo = row.getCell(2).getStringCellValue();
            String amountStr = row.getCell(5).getStringCellValue();
            String status = row.getCell(6).getStringCellValue();
            //处理结果为失败则直接跳过
            if (status.equals("失败")) {
                continue;
            }
            //成功则判断map中是否有此账号
            BigDecimal amount = map.get(acctNo);
            if (amount == null) {
                //没有,则新建
                map.put(acctNo, new BigDecimal(decimalFormat.parseObject(amountStr).toString()));
            } else {
                //有,则累加
                amount = BigDecimalUtil.add(amount.doubleValue(), Double.valueOf(decimalFormat.parseObject(amountStr).toString()));
                map.put(acctNo, amount);
            }

        }
        return map;
    }

    private List<PingAnBalanceListVo> assemblePingAnBalanceListVo(Map map) throws ParseException {
        List<PingAnBalanceListVo> list = Lists.newArrayList();
        List agreementList = (List) map.get("agreementList");
        Map balanceDtoMap = (Map) map.get("balanceDtoMap");
        for (Object o : agreementList) {
            PingAnBalanceListVo pingAnBalanceListVo = new PingAnBalanceListVo();
            Map detail = (Map) o;
            String acctName = (String) detail.get("acctName");//账户姓名
            String acctNo = (String) detail.get("acctNo");//账号
            String acctOpenBranchName = (String) detail.get("acctOpenBranchName");//开户行
            String agreementNo = (String) detail.get("agreementNo");//查询详情合约号
            //根据账号查询balanceMap
            Map balanceMap = (Map) balanceDtoMap.get(acctNo);
            //子账户列表
            //判断是否有余额信息
            if (balanceMap != null) {
                String updateTime = (String) balanceMap.get("updateTime");
                List subAccountList = (List) balanceMap.get("subAccountList");
                //根据观察,仅需第一个子账户信息
                Map detailMap = (Map) subAccountList.get(0);
                String balance = (String) detailMap.get("balance");

                pingAnBalanceListVo.setUpdateTime(updateTime);
                pingAnBalanceListVo.setBalance(new BigDecimal(decimalFormat.parseObject(balance).toString()));
            }

            pingAnBalanceListVo.setAcctName(acctName);
            pingAnBalanceListVo.setAcctNo(acctNo);
            pingAnBalanceListVo.setAcctOpenBranchName(acctOpenBranchName);
            pingAnBalanceListVo.setAgreementNo(agreementNo);
            list.add(pingAnBalanceListVo);
        }
        return list;
    }

    private Map queryOtherBankBalance(Integer pageIndex) throws IOException, InterruptedException {
        WebRequest webRequest = new WebRequest(new URL(OTHER_BANK_BALANCE), HttpMethod.POST);
        List<NameValuePair> reqParams = Lists.newArrayList();

        //设置请求参数
        reqParams.add(new NameValuePair("channelType", "d"));
        reqParams.add(new NameValuePair("responseDataType", "JSON"));
        reqParams.add(new NameValuePair("pageIndex", String.valueOf(pageIndex)));
        reqParams.add(new NameValuePair("pageSize", "20"));
        webRequest.setRequestParameters(reqParams);
        //发送请求
        Page resultPage = webClient.getPage(webRequest);

        //等待加载
        Thread.sleep( 1000);

        return JsonUtil.pingAnBalance(resultPage.getWebResponse().getContentAsStream());
    }


    public List<Map<String, Object>> ccbAnalyze(InputStream inputStream) throws IOException {
        List<Map<String, Object>> data = Lists.newArrayList();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 6; i <= sheet.getLastRowNum(); i++) {
            Map<String, Object> paymentMap = new HashMap<>();
            Row row = sheet.getRow(i);
            Cell dateCell = row.getCell(1);
            Cell timeCell = row.getCell(2);
            Cell positionCell = row.getCell(3);
            Cell incomeCell = row.getCell(5);
            Cell accountCell = row.getCell(7);
            Cell nameCell = row.getCell(8);
            Cell commentCell = row.getCell(10);

            String dateTimeStr = dateCell.getStringCellValue() + "-" + timeCell.getStringCellValue();
            String position = positionCell.getStringCellValue();
            BigDecimal income = BigDecimal.valueOf(incomeCell.getNumericCellValue());
            String account = accountCell.getStringCellValue();
            String name = nameCell.getStringCellValue();
            String comment = commentCell.getStringCellValue();

            Date dateTime = DateTimeUtil.strToDate(dateTimeStr, "yyyyMMdd-HH:mm:ss");
            //如果收入金额为0 则跳过
            if (income.equals(BigDecimal.ZERO)) {
                continue;
            }
            paymentMap.put("交易时间", dateTime);
            paymentMap.put("交易地点", position);
            paymentMap.put("交易金额", income);
            paymentMap.put("交易账号", account);
            paymentMap.put("交易方姓名", name);
            paymentMap.put("摘要", comment);


            data.add(paymentMap);


        }
        return data;

    }

    private boolean bankLogin(Integer branch) throws Exception {
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
        Thread.sleep(4 * 1000);
        String un="";
        String pwd="";
        if (branch == Const.Branch.CD.getCode()) {
            un = PINGAN_CHENGDU_USERNAME;
            pwd = PINGAN_CHENGDU_PASSWORD;
        } else if (branch == Const.Branch.KM.getCode()) {
            un = PINGAN_KUNMING_USERNAME;
            pwd = PINGAN_KUNMING_PASSWORD;
        }

        HtmlElement userName = page.getHtmlElementById("userName");
        HtmlElement password = page.getHtmlElementById("pwdObject1-input");
        HtmlElement loginBtn = page.getHtmlElementById("login_btn");
        userName.focus();
        userName.type(un);
        page.getHtmlElementById("pwdObject1-btn").click();
        page.getHtmlElementById("pa_ui_keyboard_close").click();
        password.focus();
        password.type(pwd);
        loginBtn.click();
        Thread.sleep(1500);
        String result = page.asText();
        return result.contains("上次登录时间");
    }

    private List<Map<String, Object>> statement(WebClient webClient, Date startDate, Date endDate,Integer branch) throws Exception {

        //转换日期
        String start = DateTimeUtil.dateToStr(startDate, "yyyyMMdd");
        String end = DateTimeUtil.dateToStr(endDate, "yyyyMMdd");

        //生成POST请求
        WebRequest webRequest = new WebRequest(new URL(DOWNLOADURL), HttpMethod.POST);
        List<NameValuePair> reqParams = Lists.newArrayList();
        String accountNo = "";
        if (branch == Const.Branch.CD.getCode()) {
            accountNo = PINGAN_CHENGDU_ACCOUNT;
        } else if (branch == Const.Branch.KM.getCode()){
            accountNo = PINGAN_KUNMING_ACCOUNT;
        }
        //设置请求参数
        reqParams.add(new NameValuePair("pageNum", "1"));
        reqParams.add(new NameValuePair("pageSize", "99999"));
        reqParams.add(new NameValuePair("accNo", accountNo));
        reqParams.add(new NameValuePair("currType", "RMB"));
        reqParams.add(new NameValuePair("startDate", start));
        reqParams.add(new NameValuePair("endDate", end));
        webRequest.setRequestParameters(reqParams);
        //发送请求
        Page downloadPage = webClient.getPage(webRequest);

        //等待加载
        Thread.sleep(3 * 1000);
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
            Cell commentCell = row.getCell(7);
            Cell serialCell = row.getCell(8);
//            cell9.setCellType(Cell.CELL_TYPE_STRING);
            if (typeCell.getStringCellValue().equals("转入")) {
                repaymentMap.put("交易时间", timeCell.getStringCellValue());
                repaymentMap.put("交易方姓名", nameCell.getStringCellValue());
                repaymentMap.put("交易方账号", accountCell.getStringCellValue());
                repaymentMap.put("交易金额", new BigDecimal(amountCell.getStringCellValue()));
                repaymentMap.put("备注", commentCell.getStringCellValue());
                repaymentMap.put("交易流水号", serialCell.getStringCellValue());
                data.add(repaymentMap);
            }
        }
        in.close();
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

    public List<Object> getGuyuData(Class classType, String startTime, String endTime) throws IOException, ParseException {
        List<HashMap<String, Object>> mapList = Lists.newArrayList();
        List<Object> resultList=Lists.newArrayList();
        String type = classType.getSimpleName().toLowerCase();
        String  content = getGuyuInputStream(type, startTime, endTime, 1);
        mapList.addAll(getObjectList(content, classType));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(content);
        JsonNode objectData = root.path("data");
        JsonNode count = objectData.path("count");

        double totalCount = count.getDoubleValue();
        int totalPage = (int)Math.ceil(totalCount / 100);
        while (totalPage > 1) {
            content = getGuyuInputStream(type, startTime, endTime, totalPage);
            mapList.addAll(getObjectList(content, classType));
            totalPage--;
        }

        if (type.equals("finishorder")) {
            for (HashMap<String, Object> object : mapList) {
                FinishOrder newFinishOrder = assembleFinishOrder(object);
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