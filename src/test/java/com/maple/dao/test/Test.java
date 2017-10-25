package com.maple.dao.test;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maple.common.Const;
import com.maple.dao.*;
import com.maple.jo.FinishOrder;
import com.maple.jo.TicketJo;
import com.maple.pojo.*;
import com.maple.service.impl.*;
import com.maple.test.TestBase;
import com.maple.util.*;
import com.maple.vo.AccountVo;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.aspectj.weaver.ast.Var;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/5/31.
 */
@Component
public class Test extends TestBase {
    @Autowired
    private PeriodPaymentMapper periodPaymentMapper;
    @Autowired
    private CoModelMapper coModelMapper;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private FinishOrderMapper finishOrderMapper;
    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private PeriodPaymentServiceImpl periodPaymentService;
    @Autowired
    private CoModelServiceImpl coModelService;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private PeriodPlanMapper periodPlanMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;

    @org.junit.Test
    public void Task1Test() throws Exception {
        Date today = new Date();
        List<Map<String, Object>> maps = CrawlerUtil.bankStatement(new DateTime("2017-06-25").toDate(), today);
        for (Map map : maps) {
            String time = (String) map.get("交易时间");
            String accountNo = (String) map.get("交易方账号");
            String name = (String) map.get("交易方姓名");
            BigDecimal amount = (BigDecimal) map.get("交易金额");
            String serialNo = (String) map.get("交易流水号");
            Account account = accountMapper.selectByAccNo(accountNo);
            PeriodPayment newPayment = new PeriodPayment();
            if (account == null) {
                //如果未知交款人

            } else {
                //todo
                Driver driver = driverMapper.selectByPrimaryKey(account.getDriverId());
                //平安银行当日数据，只能是起始和结束日期都为当日，否则没有当日数据
                // 司机id
                newPayment.setDriverId(account.getDriverId());
                //车辆id
                newPayment.setCarId(driver.getCarId());
                //付款金额
                newPayment.setPayment(amount);
                //付款人
                newPayment.setPayer(name);
                //付款时间
                newPayment.setCreateTime(new DateTime(time).toDate());
                //支付状态默认为正常
                newPayment.setPlatformStatus(Const.PlatformStatus.PAID_NORMAL.getCode());

            }


        }

    }
    @org.junit.Test
    public void Test2() throws Exception {
        Ticket ticket = ticketMapper.selectByCarId(100001);
    }

    @org.junit.Test
    public void Task2Test() throws IOException, ParseException {
        Car car = carMapper.selectByPrimaryKey(100401);
        Map ticketMap;
            String vin = car.getVin().substring(car.getVin().length() - 6, car.getVin().length());
            String carEngineNumber = car.getEngineNumber();
            String engineNum = carEngineNumber.substring(carEngineNumber.length() - 6, carEngineNumber.length());
            ticketMap = WeiCheUtil.getTicket("成都", car.getPlateNumber(), vin, engineNum);

        String ticketScore = (String) ticketMap.get("ticketScore");
        String ticketMoney = (String) ticketMap.get("ticketMoney");
        String ticketTimes = (String) ticketMap.get("ticketTimes");
        System.out.println("扣分: " + ticketScore+"罚金: " + ticketMoney);

        Ticket newTicket = new Ticket();
        newTicket.setCarId(car.getId());
        newTicket.setPlateNum(car.getPlateNumber());
        newTicket.setMoney(Integer.parseInt(ticketMoney));
        newTicket.setScore(Integer.parseInt(ticketScore));
        newTicket.setTicketTimes(Integer.parseInt(ticketTimes));
        Ticket ticketResult = ticketMapper.selectByCarId(car.getId());

        if (ticketResult == null) {
            ticketMapper.insert(newTicket);
        } else {
            newTicket.setId(ticketResult.getId());
            Integer newScore = newTicket.getScore() - ticketResult.getScore();
            Integer newMoney = newTicket.getMoney() - ticketResult.getMoney();
            if ( newScore> 0) {
                System.out.println(ticketResult.getPlateNum()+ "-新增分数:"+newScore);
            }
            if ( newMoney> 0) {
                System.out.println(ticketResult.getPlateNum()+ "-新增罚款:"+newMoney);
            }
            ticketMapper.updateByPrimaryKeySelective(newTicket);
        }
    }

    @org.junit.Test
    public void driverTest() throws Exception {
        List<Car> carList = carMapper.selectCarListForTicket();
        Car car = carList.get(139);
        System.out.println(car.getEngineNumber());
        System.out.println(car.getPlateNumber());
        System.out.println(car.getVin());

    }

    @org.junit.Test
    public void Test() throws Exception {

        List<Object> guyuData = CrawlerUtil.getGuyuData(FinishOrder.class, "2017-05-21", "2017-05-21");
        for (Object object : guyuData) {
            FinishOrder finishOrder = (FinishOrder) object;
            finishOrderMapper.insertSelective(finishOrder);
        }
        System.out.println(guyuData.size());
    }

    @org.junit.Test
    public void test() throws Exception {

        String GUYU = "https://wave.xiaojukeji.com/v2/dc/finishorder/driver?org_id=2367&start_time=2017-05-20&end_time=2017-05-20&order_type=2&size=100&page=3";
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

        webClient.getCookieManager().addCookie(new Cookie(DOAMIN, "wave_session", "197bbe766439ab6e7ad103513e6e71d553b69fff"));

        try {
            page = webClient.getPage(url);
            System.out.println(page.asText());
            Date expires = webClient.getCookieManager().getCookie("wave_session").getExpires();
            System.out.println(DateTimeUtil.dateToStr(expires));

        } catch (FailingHttpStatusCodeException e) {
            System.out.println("Session已过期,需要重新登录");
        }


    }

    @org.junit.Test
    public void pageTest() throws IOException {
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Mobile/15A372 MicroMessenger/6.5.9 NetType/WIFI Language/zh_CN";
        String origin = "https://wx.wcar.net.cn";
        String referer = "https://wx.wcar.net.cn/wechat-weiche.php";
        String wx = "https://wx.wcar.net.cn/front/do-weiche.php";
        HashMap<String, String> Headers = Maps.newHashMap();
        //设置header
        Headers.put("User-Agent", userAgent);
        Headers.put("Origin", origin);
        Headers.put("Referer", referer);
        URL url = new URL(wx);
        WebClient webClient = new WebClient();
        WebRequest request = new WebRequest(url);
        request.setHttpMethod(HttpMethod.POST);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //设置cookies
        webClient.getCookieManager().addCookie(new Cookie("wx.wcar.net.cn","Hm_lpvt_d2abe42fa456dc1fead1fb628c78e264","1507220182"));
        webClient.getCookieManager().addCookie(new Cookie("wx.wcar.net.cn","Hm_lvt_d2abe42fa456dc1fead1fb628c78e264","1505279007,1507218255"));
        webClient.getCookieManager().addCookie(new Cookie("wx.wcar.net.cn","WEICHE_wx_wcar_net_cn","nr37urb1onu41an3s6lg21gq30"));
        request.setAdditionalHeaders(Headers);
        List<NameValuePair> params = Lists.newArrayList();
//        params.add(new NameValuePair("limit_push", "0"));
//        params.add(new NameValuePair("violation_push", "0"));
        params.add(new NameValuePair("action", "violations"));
        params.add(new NameValuePair("vid", "11292279"));
        params.add(new NameValuePair("page", "0"));
        request.setRequestParameters(params);

        HtmlPage page = webClient.getPage(request);
        String result = page.asText();
        System.out.println(result);
    }

    @org.junit.Test
    public void WebTest() throws Exception {
        final String BANK = "https://bank.pingan.com.cn/ibp/bank/index.html#home/home/index";
        final String DOWNLOAD = "https://bank.pingan.com.cn/ibp/ibp4pc/work/transfer/downloadTransferDetail.do";
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        URL url = new URL(BANK);


        HtmlPage page=null;

        page = webClient.getPage(url);
        page.executeJavaScript("Object.defineProperty(navigator,'platform',{get:function(){return 'Win32';}});");
        Thread.sleep(8000);

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
        System.out.println(result);
        if (result.contains("冉伟")) {
            System.out.println("登录成功!");
            WebRequest webRequest = new WebRequest(new URL(DOWNLOAD), HttpMethod.POST);
            List<NameValuePair> reqParams = Lists.newArrayList();
            reqParams.add(new NameValuePair("pageNum", "1"));
            reqParams.add(new NameValuePair("pageSize", "99999"));
            reqParams.add(new NameValuePair("accNo", "6230580000148117729"));
            reqParams.add(new NameValuePair("currType", "RMB"));
            reqParams.add(new NameValuePair("startDate", "20170813"));
            reqParams.add(new NameValuePair("endDate", "20171025"));

            webRequest.setRequestParameters(reqParams);

            Page downloadPage = webClient.getPage(webRequest);


            Thread.sleep(3 * 1000);

            InputStream in = downloadPage.getWebResponse().getContentAsStream();
            FileOutputStream fos = new FileOutputStream(new File("/Users/Maple.Ran/Downloads/new.xls"));
            IOUtils.copy(in, fos);
            fos.close();

        } else {
            System.out.println("登录失败!");
        }

    }

    @org.junit.Test
    public void dataImport() throws IOException {

        File slh = new File("/Users/Maple.Ran/Downloads/SLHDriverDetailinfo.xls");
        InputStream fileInputStream = new FileInputStream(slh);

        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell driverName = row.getCell(0);
            Cell driverPhone = row.getCell(1);
            Cell carName = row.getCell(3);
            Cell plateNum = row.getCell(4);//车牌号
            Cell driverStatus = row.getCell(5);//司机状态
            Cell idNum = row.getCell(8);//身份证号码
            Cell driverLicenceFileNumber = row.getCell(9);//驾照档案号
            Cell vin = row.getCell(11);//车架号
            Cell engineNum = row.getCell(12);//发动机号
            Cell modelType = row.getCell(15);//合作模式

//            Cell ec = row.getCell(22);//紧急联系人
//            Cell ecp = row.getCell(23);//紧急联系人电话号码
//            Cell sec = row.getCell(24);//第二紧急联系人
//            Cell secp = row.getCell(25);//第二紧急联系人电话号码


            Cell downAmount = row.getCell(27);//首付金额
            Cell finalAmount = row.getCell(28);//尾款金额

            Cell firstPP = row.getCell(29);//第一周期金额
            Cell firstPPN = row.getCell(30);//第一周期月数
            //todo 动态查询应交金额，

            Cell secondPP = row.getCell(31);//第一周期金额
            Cell secondPPN = row.getCell(32);//第一周期月数

            Cell thirdPP = row.getCell(33);//第一周期金额
            Cell thirdPPN = row.getCell(34);//第一周期月数

            Cell totalAmount = row.getCell(35);
            Cell pickDateCell = row.getCell(36);
            Cell carStatus = row.getCell(38);
            Cell endDateCell = row.getCell(39);//合作结束日期

            Integer periodNum = 0;

            //查询是否已经存在该车辆
            Car car = carMapper.selectByVin(vin.getStringCellValue());

            CoModel coModel = new CoModel();
            Date startDate = pickDateCell.getDateCellValue();
            Date endDate;
            if (car == null) {
                car = new Car();
                car.setName(carName.getStringCellValue());
                car.setVin(vin.getStringCellValue());
                car.setPlateNumber(plateNum.getStringCellValue());
                car.setEngineNumber(engineNum.getStringCellValue());
                car.setPickDate(startDate);
                car.setCarStatus((int) carStatus.getNumericCellValue());
                car.setBranch(Const.Branch.CD.getCode());
                carMapper.insert(car);
                PeriodPlan firstPlan = new PeriodPlan();
                PeriodPlan secondPlan = null;
                PeriodPlan thirdPlan = null;
            //todo 如果已有车辆,则判断司机是否正常,如果非正常,则新建合作模式和还款计划
                coModel.setCarId(car.getId());
                if (modelType.getStringCellValue().equals("周")) {
                    coModel.setModelType(Const.CoModel.HIRE_PURCHASE_WEEK.getCode());
                    //计算未来最近的星期二
                    int dayOfWeek = new DateTime(startDate).getDayOfWeek();
                    startDate = DateTimeUtil.getWeekStartDate(startDate);
                    if (dayOfWeek > 5 & dayOfWeek < 2) {
                        startDate.setTime(startDate.getTime() + 3600 * 1000 * 24 * 14); //如果是星期5-星期一，则下下个星期二为起始日
                    } else {
                        //星期二到星期五则下个星期二
                        startDate.setTime(startDate.getTime() + 3600 * 1000 * 24 * 7);
                    }
                    coModel.setPeriodStartDate(startDate);
                    firstPlan.setAmount(new BigDecimal(firstPP.getNumericCellValue() / 4));
                    firstPlan.setStartDate(startDate);
                    endDate = new DateTime(startDate).plusMonths((int) firstPPN.getNumericCellValue()).toDate();
                    periodNum = +(int)firstPPN.getNumericCellValue();
                    //结束日期最接近的星期二
                    endDate = DateTimeUtil.getWeekStartDate(endDate);
                    if (secondPP.getNumericCellValue() != 0) {
                        secondPlan = new PeriodPlan();
                        secondPlan.setAmount(new BigDecimal(secondPP.getNumericCellValue() / 4));
                        Date secondStartDate = endDate;//第二个周期起始时间为第一周期结束时间
                        secondPlan.setStartDate(secondStartDate);//赋值
                        Date secondEndDate = new DateTime(secondStartDate).plusMonths((int) secondPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
                        periodNum = +(int) secondPPN.getNumericCellValue();
                        secondEndDate = DateTimeUtil.getWeekStartDate(secondEndDate);//最近的星期二
                        if (thirdPP.getNumericCellValue() != 0) {
                            thirdPlan = new PeriodPlan();
                            thirdPlan.setAmount(new BigDecimal(thirdPP.getNumericCellValue() / 4));
                            Date thirdStartDate = secondEndDate;
                            thirdPlan.setStartDate(thirdStartDate);
                            Date thirdEndDate = new DateTime(thirdStartDate).plusMonths((int) thirdPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
                            periodNum = +(int) thirdPPN.getNumericCellValue();
                            thirdEndDate = DateTimeUtil.getWeekStartDate(thirdEndDate);//最近的星期二
                            thirdPlan.setEndDate(thirdEndDate);
                            secondEndDate.setTime(secondEndDate.getTime() - 1000);
                        }
                        secondPlan.setEndDate(secondEndDate);//判断完第三周期之后赋值

                        endDate.setTime(endDate.getTime() - 1000);//同时第一周期结束日期减一秒,防止重复计算应缴额

                    }
                    firstPlan.setEndDate(endDate);//等判断完第二周期后设置结束日期

                } else if (modelType.getStringCellValue().equals("月")) {
                    firstPlan.setAmount(new BigDecimal(firstPP.getNumericCellValue()));
                    firstPlan.setStartDate(startDate);
                    firstPlan.setEndDate(new DateTime(startDate).plusMonths((int) firstPPN.getNumericCellValue()).toDate());
                    periodNum = +(int)firstPPN.getNumericCellValue();
                    coModel.setModelType(Const.CoModel.HIRE_PURCHASE_MONTH.getCode());
                    coModel.setPeriodStartDate(startDate);
                    DateTime dateTime = new DateTime(startDate);
                    endDate = dateTime.plusMonths((int) firstPPN.getNumericCellValue()).toDate();
                    coModel.setPeriodEndDate(endDate);
                } else if (modelType.getStringCellValue().equals("全款")) {
                    coModel.setModelType(Const.CoModel.FULL_PAYMENT.getCode());
                    coModel.setPeriodStartDate(startDate);
                }
                coModel.setPeriodEndDate(endDateCell.getDateCellValue());
                coModel.setDownAmount(new BigDecimal(downAmount.getNumericCellValue()));
                coModel.setTotalAmount(new BigDecimal(totalAmount.getNumericCellValue()));
                coModel.setFinalAmount(new BigDecimal(finalAmount.getNumericCellValue()));
                coModel.setPeriodNum(periodNum);
                coModelMapper.insert(coModel);
                //非全款添加还款计划
                if (!modelType.getStringCellValue().equals("全款")) {
                    firstPlan.setCoModelId(coModel.getId());
                    periodPlanMapper.insert(firstPlan);
                    if (secondPlan != null) {
                        Date secondPlanStartDate = secondPlan.getStartDate();
                        secondPlanStartDate.setTime(secondPlanStartDate.getTime() + 1000);
                        secondPlan.setCoModelId(coModel.getId());
                        periodPlanMapper.insert(secondPlan);
                    }
                    if (thirdPlan != null) {
                        Date thirdPlanStartDate = thirdPlan.getStartDate();
                        thirdPlanStartDate.setTime(thirdPlanStartDate.getTime() + 1000);
                        thirdPlan.setCoModelId(coModel.getId());
                        periodPlanMapper.insert(thirdPlan);
                    }

                }
            } else {
                //如果已经有车辆
                List<Driver> drivers = driverMapper.selectDriverListByCarId(car.getId());
                Driver driver = drivers.get(0);//判断是否是已经合作结束 的司机，是则重新添加新的合作模式
                if (driver.getDriverStatus() == Const.DriverStatus.TERMINATED_DRIVER.getCode()) {
                    PeriodPlan firstPlan = new PeriodPlan();
                    PeriodPlan secondPlan = null;
                    PeriodPlan thirdPlan = null;

                    coModel.setCarId(car.getId());
                    if (modelType.getStringCellValue().equals("周")) {
                        coModel.setModelType(Const.CoModel.HIRE_PURCHASE_WEEK.getCode());
                        //计算未来最近的星期二
                        int dayOfWeek = new DateTime(startDate).getDayOfWeek();
                        startDate = DateTimeUtil.getWeekStartDate(startDate);
                        if (dayOfWeek > 5 & dayOfWeek < 2) {
                            startDate.setTime(startDate.getTime() + 3600 * 1000 * 24 * 14); //如果是星期5-星期一，则下下个星期二为起始日
                        } else {
                            //星期二到星期五则下个星期二
                            startDate.setTime(startDate.getTime() + 3600 * 1000 * 24 * 7);
                        }
                        coModel.setPeriodStartDate(startDate);
                        firstPlan.setAmount(new BigDecimal(firstPP.getNumericCellValue() / 4));
                        firstPlan.setStartDate(startDate);
                        endDate = new DateTime(startDate).plusMonths((int) firstPPN.getNumericCellValue()).toDate();
                        periodNum = +(int)firstPPN.getNumericCellValue();

                        //结束日期最接近的星期二
                        endDate = DateTimeUtil.getWeekStartDate(endDate);
                        if (secondPP.getNumericCellValue() != 0) {
                            secondPlan = new PeriodPlan();
                            secondPlan.setAmount(new BigDecimal(secondPP.getNumericCellValue() / 4));
                            Date secondStartDate = endDate;//第二个周期起始时间为第一周期结束时间
                            secondPlan.setStartDate(secondStartDate);//赋值
                            Date secondEndDate = new DateTime(secondStartDate).plusMonths((int) secondPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
                            periodNum = +(int)secondPPN.getNumericCellValue();

                            secondEndDate = DateTimeUtil.getWeekStartDate(secondEndDate);//最近的星期二
                            if (thirdPP.getNumericCellValue() != 0) {
                                thirdPlan = new PeriodPlan();
                                thirdPlan.setAmount(new BigDecimal(thirdPP.getNumericCellValue() / 4));
                                Date thirdStartDate = secondEndDate;
                                thirdPlan.setStartDate(thirdStartDate);
                                Date thirdEndDate = new DateTime(thirdStartDate).plusMonths((int) thirdPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
                                periodNum = +(int)thirdPPN.getNumericCellValue();
                                thirdEndDate = DateTimeUtil.getWeekStartDate(thirdEndDate);//最近的星期二
                                thirdPlan.setEndDate(thirdEndDate);
                                secondEndDate.setTime(secondEndDate.getTime() - 1000);
                            }
                            secondPlan.setEndDate(secondEndDate);//判断完第三周期之后赋值

                            endDate.setTime(endDate.getTime() - 1000);//同时第一周期结束日期减一秒,防止重复计算应缴额

                        }
                        firstPlan.setEndDate(endDate);//等判断完第二周期后设置结束日期

                    } else if (modelType.getStringCellValue().equals("月")) {
                        firstPlan.setAmount(new BigDecimal(firstPP.getNumericCellValue()));
                        firstPlan.setStartDate(startDate);
                        firstPlan.setEndDate(new DateTime(startDate).plusMonths((int) firstPPN.getNumericCellValue()).toDate());
                        periodNum = +(int)firstPPN.getNumericCellValue();
                        coModel.setModelType(Const.CoModel.HIRE_PURCHASE_MONTH.getCode());
                        coModel.setPeriodStartDate(startDate);
                        DateTime dateTime = new DateTime(startDate);
                        endDate = dateTime.plusMonths((int) firstPPN.getNumericCellValue()).toDate();
                        coModel.setPeriodEndDate(endDate);
                    } else if (modelType.getStringCellValue().equals("全款")) {
                        coModel.setModelType(Const.CoModel.FULL_PAYMENT.getCode());
                        coModel.setPeriodStartDate(startDate);
                    }
                    coModel.setPeriodEndDate(endDateCell.getDateCellValue());
                    coModel.setDownAmount(new BigDecimal(downAmount.getNumericCellValue()));
                    coModel.setTotalAmount(new BigDecimal(totalAmount.getNumericCellValue()));
                    coModel.setFinalAmount(new BigDecimal(finalAmount.getNumericCellValue()));
                    coModel.setPeriodNum(periodNum);
                    coModelMapper.insert(coModel);
                    //非全款添加还款计划
                    if (!modelType.getStringCellValue().equals("全款")) {
                        firstPlan.setCoModelId(coModel.getId());
                        periodPlanMapper.insert(firstPlan);
                        if (secondPlan != null) {
                            Date secondPlanStartDate = secondPlan.getStartDate();
                            secondPlanStartDate.setTime(secondPlanStartDate.getTime() + 1000);
                            secondPlan.setCoModelId(coModel.getId());
                            periodPlanMapper.insert(secondPlan);
                        }
                        if (thirdPlan != null) {
                            Date thirdPlanStartDate = thirdPlan.getStartDate();
                            thirdPlanStartDate.setTime(thirdPlanStartDate.getTime() + 1000);
                            thirdPlan.setCoModelId(coModel.getId());
                            periodPlanMapper.insert(thirdPlan);
                        }
                    }
                } else {
                    coModel.setId(driver.getCoModelId());
                }
            }

            Driver driver = new Driver();
            driver.setName(driverName.getStringCellValue());
            driver.setDriverLicenceFileNumber(driverLicenceFileNumber.getStringCellValue());
            driver.setCarId(car.getId());
            driver.setPersonalPhone(driverPhone.getStringCellValue());
            driver.setUserId(10);
            driver.setIdNumber(idNum.getStringCellValue());
            driver.setDriverStatus((int) driverStatus.getNumericCellValue());
            driver.setCoModelId(coModel.getId());
            driver.setPeriodsStatus(1);
            driverMapper.insert(driver);


        }


    }

    @org.junit.Test
    public void test2 () throws IOException {
//        File json = new File("/Users/Maple.Ran/Downloads/ticket.json");
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode root = mapper.readTree(json);
//        JsonNode resultcodeNode = root.get("resultcode");
//        Integer resultCode = mapper.readValue(resultcodeNode, Integer.class);
//        JsonNode result = root.get("result");
//
//        JsonNode lists = result.get("lists");
//        List<TicketJo> ticketJoList = mapper.readValue(lists, new TypeReference<List<TicketJo>>() {
//        });


        List<Car> carList = carMapper.selectCarListForTicket();
        Car car = carList.get(0);

        String ticket = TicketQueryUtil.getTicket("SC_CD", car.getPlateNumber(), car.getEngineNumber(), car.getVin());
        System.out.println(ticket);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(ticket);
        JsonNode resultcodeNode = root.get("resultcode");
        Integer resultCode = mapper.readValue(resultcodeNode, Integer.class);
        JsonNode result = root.get("result");
        /*
        错误信息:
        {
            "resultcode":"209",
            "reason":"内部异常：其他信息错误",
            "result":[

            ],
            "error_code":203609
        }
         */
        JsonNode lists = result.get("lists");
        List<TicketJo> ticketJoList = mapper.readValue(lists, new TypeReference<List<TicketJo>>() {
        });
        Integer fen = 0;
        Integer money = 0;
        for (TicketJo ticketJo1 : ticketJoList) {
            fen += ticketJo1.getFen();
            money += ticketJo1.getMoney();
        }
        Ticket newTicket = new Ticket();
        newTicket.setCarId(car.getId());
        newTicket.setPlateNum(car.getPlateNumber());
        newTicket.setMoney(money);
        newTicket.setScore(fen);
        newTicket.setTicketTimes(ticketJoList.size());

        Ticket ticketResult = ticketMapper.selectByCarId(car.getId());

        if (ticketResult == null) {
            ticketMapper.insert(newTicket);
        } else {
            newTicket.setId(ticketResult.getId());
            ticketMapper.updateByPrimaryKeySelective(newTicket);
        }

    }

    @org.junit.Test
    public void sendSms() throws InterruptedException {
        List<Driver> driverList = driverMapper.selectDriverListByStatus(Const.DriverStatus.NORMAL_DRIVER.getCode());
        for (int i = 0; i<driverList.size();i++) {
            Driver driver = driverList.get(i);
            Car car = carMapper.selectByPrimaryKey(driver.getCarId());
            Ticket ticket = ticketMapper.selectByCarId(driver.getCarId());
            try {
                System.out.println("第"+(i+1)+"条短信"+"开始发送:"+driver.getName());
                SendSmsResponse sendSmsResponse = SmsUtil.sendSms(driver.getPersonalPhone(), driver.getName(), car.getPlateNumber(), ticket.getScore().toString(), ticket.getMoney().toString());
                System.out.println("短信接口返回的数据----------------");
                System.out.println("Code=" + sendSmsResponse.getCode());
                System.out.println("Message=" + sendSmsResponse.getMessage());
                System.out.println("RequestId=" + sendSmsResponse.getRequestId());
                System.out.println("BizId=" + sendSmsResponse.getBizId());
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }


    }
    @org.junit.Test
    public void querySms() throws InterruptedException {
//        List<Driver> driverList = driverMapper.selectDriverListByStatus(Const.DriverStatus.NORMAL_DRIVER.getCode());
        List<Driver> driverList = driverMapper.selectDriverListByPhoneStatus(Const.phoneStatus.uncheck);
        Driver newDriver = new Driver();

        for (Driver driver : driverList) {
            try {
                QuerySendDetailsResponse querySendDetailsResponse = SmsUtil.querySendDetails(driver.getPersonalPhone(), new LocalDate(2017,10,19).toDate());
                int i = 0;
                for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {

                    Long sendStatus = smsSendDetailDTO.getSendStatus();
                    newDriver.setId(driver.getId());
                    if (sendStatus == 3) {//已发送
                        newDriver.setPhoneStatus(1);//正常
                        System.out.println("正常");
                    } else if (sendStatus == 1) {//已发送,待收
                        System.out.println("待定");
                        newDriver.setPhoneStatus(2);
                    } else if (sendStatus == 2) {//发送失败
                        System.out.println("失效");
                        newDriver.setPhoneStatus(0);//失效
                    }
                    driverMapper.updateByPrimaryKeySelective(newDriver);

                }
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

    @org.junit.Test
    public void testFormapper() {

    }
}

