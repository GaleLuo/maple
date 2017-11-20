package com.maple.dao.test;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
import com.maple.dao.*;
import com.maple.jo.FinishOrder;
import com.maple.pojo.*;
import com.maple.service.IBankService;
import com.maple.service.impl.*;
import com.maple.task.PaymentQueryTask;
import com.maple.test.TestBase;
import com.maple.util.*;
import com.maple.vo.PingAnBalanceListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private PaymentQueryTask paymentQueryTask;
    @Autowired
    private IBankService iBankService;

    private static final int CCB = 15;
    private static final int CMB = 50;
    private static final int PINGAN = 21;

    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    @org.junit.Test
    public void pinganBankQuery() throws Exception {
        Date today = new Date();
        Date weekStartDate = DateTimeUtil.getWeekStartDate(today);
        //成都数据
        iBankService.bankLogin(Const.Branch.CD.getCode());
        //请求当天数据
        List<Map<String, Object>> cdToday = iBankService.statement(today, today, Const.Branch.CD.getCode());
        //请求本周数据
        List<Map<String, Object>> cdWeek = iBankService.statement(weekStartDate, today, Const.Branch.CD.getCode());
        if (CollectionUtils.isNotEmpty(cdToday)) {
            insertByList(cdToday);
        }

        if (CollectionUtils.isNotEmpty(cdWeek)) {
            insertByList(cdWeek);
        }
        //关闭连接
        iBankService.closeConnection();

    }

    private void insertByList(List data) {
        for (Object o : data) {
            Map map = (Map) o;
            String time = (String) map.get("交易时间");
            String accountNo = (String) map.get("交易方账号");
            String payer = (String) map.get("交易方姓名");
            BigDecimal amount = (BigDecimal) map.get("交易金额");
            String serialNo = (String) map.get("交易流水号");
            String comment = (String) map.get("备注");
            PeriodPayment periodPayment = periodPaymentMapper.selectBySerialNo(serialNo);
            if (periodPayment == null&&amount.compareTo(BigDecimal.ZERO)>0) {
                PeriodPayment newPayment = assemblePeriodPayment(time, serialNo, payer, accountNo, comment, amount, Const.PaymentPlatform.pingan.getCode());
                periodPaymentMapper.insertSelective(newPayment);
            }
        }
    }

    private PeriodPayment assemblePeriodPayment(String time,String serialNo,String payer,String accountNo,String comment,
                                                BigDecimal amount,Integer platformCode) {
        Account account = accountMapper.selectByAccNo(accountNo);
        PeriodPayment newPayment = new PeriodPayment();
        Date payTime = DateTimeUtil.strToDate(time, "yyyy-MM-dd HH:mm:ss");
        if (account == null) {
            //如果未知交款人
            newPayment.setPayment(amount);
            //付款账号
            newPayment.setAccountNumber(accountNo);
            //付款平台
            newPayment.setPaymentPlatform(platformCode);
            //平台流水号
            newPayment.setPlatformNumber(serialNo);
            //支付状态默认为未确认
            newPayment.setPlatformStatus(Const.PlatformStatus.UNCONFIRMED.getCode());
            //备注：添加人：系统导入
            newPayment.setComment( "添加人：系统导入");
            if (payer.contains("支付宝")) {
                //付款人变为格式comment+支付宝
                //支付宝姓名
                String zfbName = comment.substring(0, comment.indexOf("支付宝转账"));
                payer = zfbName + "-支付宝转入";
            }
            //付款人
            newPayment.setPayer(payer);
            //付款对应日期
            newPayment.setPayTime(payTime);
            //付款时间
            newPayment.setCreateTime(payTime);

        } else {
            Driver driver = driverMapper.selectByPrimaryKey(account.getDriverId());
            CoModel coModel = coModelMapper.selectByPrimaryKey(driver.getCoModelId());
            if (coModel.getModelType() == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
                payTime = DateTimeUtil.getWeekStartDate(payTime);
            }
            //平安银行当日数据，只能是起始和结束日期都为当日，否则没有当日数据
            // 司机id
            newPayment.setDriverId(account.getDriverId());
            //车辆id
            newPayment.setCarId(driver.getCarId());
            //付款金额
            newPayment.setPayment(amount);
            //付款人
            newPayment.setPayer(payer);
            //付款账号
            newPayment.setAccountNumber(accountNo);
            //付款平台
            newPayment.setPaymentPlatform(platformCode);
            //平台流水号
            newPayment.setPlatformNumber(serialNo);
            //支付状态默认为正常
            newPayment.setPlatformStatus(Const.PlatformStatus.PAID_NORMAL.getCode());
            //备注：添加人：系统导入
            newPayment.setComment("添加人:系统导入");
            //付款对应日期
            newPayment.setPayTime(payTime);
            //付款时间
            newPayment.setCreateTime(payTime);

        }
        return newPayment;
    }

    @org.junit.Test
    public void Test3() throws IOException, InterruptedException, ParseException {
        ServerResponse response = iBankService.pingAnQueryOtherBankBalance(0);
        List<PingAnBalanceListVo> data = (List<PingAnBalanceListVo>) response.getData();
        for (PingAnBalanceListVo pingAnBalanceListVo : data) {
            System.out.println(pingAnBalanceListVo.getAcctName()+"-"+pingAnBalanceListVo.getAcctNo()+"-"+pingAnBalanceListVo.getAcctOpenBranchName()+"-"+pingAnBalanceListVo.getBalance()+"-"+pingAnBalanceListVo.getUpdateTime()+"成功归集金额:"+pingAnBalanceListVo.getAmount());
        }
    }

    @org.junit.Test
    public void Test4() throws IOException {
        String a = "林小平支付宝转账";
        int zfbzz = a.indexOf("支付宝转账");
        String substring = a.substring(0, zfbzz);
        System.out.println(substring);
    }

    @org.junit.Test
    public void Test2() throws Exception {
        File slh = new File("/Users/Maple.Ran/Downloads/payment.xls");
        InputStream fileInputStream = new FileInputStream(slh);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        PeriodPayment newPayment = new PeriodPayment();
        Double Total = 0d;

        for (int i = 4; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell plateNum = row.getCell(0);
            Cell driverName = row.getCell(1);
            //根据车牌查找车辆
            Car car = carMapper.selectbyPlateNumber(plateNum.getStringCellValue());
            //根据车id查找司机list
            List<Driver> driverList = driverMapper.selectDriverListByCarId(car.getId());
            Integer driverId = null;
            //双班司机以表格中司机名字为交款人
            for (Driver driver : driverList) {
                if (driver.getName().equals(driverName.getStringCellValue())) {
                    driverId = driver.getId();
                }
            }
            //设置车辆id
            newPayment.setCarId(car.getId());
            //设置司机id
            newPayment.setDriverId(driverId);
            //付款人
            newPayment.setPayer(driverName.getStringCellValue());
            //付款状态为正常
            newPayment.setPlatformStatus(Const.PlatformStatus.PAID_NORMAL.getCode());
            //备注为EXCEL
            newPayment.setComment("添加人:EXCEL");

            //第一个司机交费时间
            DateTime startDate = new DateTime("2016-6-21");
            for (int j =2;j<row.getLastCellNum();j++) {
                Cell amount = row.getCell(j);
                Comment comment = amount.getCellComment();
                CellStyle cellStyle = amount.getCellStyle();
                short fgc = cellStyle.getFillForegroundColor();
                String commentString = comment==null?"":comment.getString().toString();
                switch (fgc) {
                    case CCB:
                        newPayment.setPaymentPlatform(Const.PaymentPlatform.ccb.getCode());
                        break;
                    case PINGAN:
                        //平安银行跳过
                        continue;
                    case CMB:
                        newPayment.setPaymentPlatform(Const.PaymentPlatform.cmb.getCode());
                        break;
                }


                if (commentString.contains("支")) {
                    newPayment.setPaymentPlatform(Const.PaymentPlatform.alipay.getCode());
                } else if (commentString.contains("微")) {
                    newPayment.setPaymentPlatform(Const.PaymentPlatform.wechat.getCode());
                }
                newPayment.setPayTime(startDate.toDate());
                newPayment.setCreateTime(startDate.toDate());
                newPayment.setPayment(BigDecimal.valueOf(amount.getNumericCellValue()));

                if (amount.getNumericCellValue() != 0) {
                    periodPaymentMapper.insertSelective(newPayment);
                }

                startDate = startDate.plusWeeks(1);
            }
        }
        System.out.println(Total);
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
    }

    @org.junit.Test
    public void Test() throws Exception {

        List<Object> guyuData = iBankService.getGuyuData(FinishOrder.class, "2017-05-21", "2017-05-21");
        for (Object object : guyuData) {
            FinishOrder finishOrder = (FinishOrder) object;
            finishOrderMapper.insertSelective(finishOrder);
        }
        System.out.println(guyuData.size());
    }

    @org.junit.Test
    public void test() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/Users/Maple.Ran/IdeaProjects/maple/src/main/resources/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://auth.alipay.com/login/index.htm");

        driver.findElement(By.xpath("//*[@id=\"J-loginMethod-tabs\"]/li[2]")).click();
        Thread.sleep(500);

        driver.findElement(By.id("J-input-user")).clear();

        WebElement username = driver.findElement(By.id("J-input-user"));
        slowInput(username,"shilihe001@163.com");
        Thread.sleep(600);
//        获取密码输入框
        driver.findElement(By.id("password_rsainput")).clear();
        WebElement password = driver.findElement(By.id("password_rsainput"));
        slowInput(password,"901901jj");


        WebElement button = driver.findElement(By.id("J-login-btn"));
        Thread.sleep(1000);
        button.click();

        Thread.sleep(500);
        org.openqa.selenium.Cookie ctoken = driver.manage().getCookieNamed("ctoken");
        ((JavascriptExecutor) driver).executeScript("function post(URL, PARAMS) {var temp = document.createElement(\"form\");temp.action = URL;temp.method = \"post\";temp.style.display = \"none\";for (var x in PARAMS) {var opt = document.createElement(\"textarea\");opt.name = PARAMS[x].name;opt.value = PARAMS[x].value;temp.appendChild(opt);}document.body.appendChild(temp);temp.submit();return temp;}post(\"https://mbillexprod.alipay.com/enterprise/fundAccountDetail.json\",[{name:\"queryEntrance\",value:\"1\"},{name:\"billUserId\",value:\"2088721226451734\"},{name:\"showType\",value:\"0\"},{name:\"type\",value:\"\"},{name:\"precisionQueryKey\",value:\"tradeNo\"},{name:\"startDateInput\",value:\"2017-10-18 00:00:00\"},{name:\"endDateInput\",value:\"2017-11-17 00:00:00\"},{name:\"pageSize\",value:\"500\"},{name:\"pageNum\",value:\"1\"},{name:\"total\",value:\"13\"},{name:\"sortTarget\",value:\"tradeTime\"},{name:\"order\",value:\"descend\"},{name:\"sortType\",value:\"0\"},{name:\"_input_charset\",value:\"gbk\"},{name:\"ctoken\",value:\""+ctoken.getValue()+"\"},])");
        System.out.println(driver.findElement(By.xpath("/html/body/pre")).getText());
        driver.close();
//          截图
//        File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        FileUtils.copyFile(screenShot,new File("/User/"));

    }

    private void slowInput(WebElement element, String string) {
        for (int i =0; i<string.length();i++) {
            element.sendKeys(string.substring(i,i+1));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        final String BANK = "http://www.ccb.com/cn/jump/personal_loginbank.html";
        final String LOGIN = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_10?SERVLET_NAME=B2CMainPlat_10&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        URL url = new URL(LOGIN);

        HtmlPage page=null;

        page = webClient.getPage(url);
//        page.executeJavaScript("Object.defineProperty(navigator,'platform',{get:function(){return 'Win32';}});");
        webClient.waitForBackgroundJavaScript(1000);

        HtmlElement userName = page.getHtmlElementById("USERID");
        HtmlElement password = page.getHtmlElementById("LOGPASS");
        HtmlElement loginButton = page.getHtmlElementById("loginButton");

        HtmlElement fjm = page.getHtmlElementById("PT_CONFIRM_PWD");
        HtmlImage fujiama = page.getHtmlElementById("fujiama");
        ImageReader imageReader = fujiama.getImageReader();
        BufferedImage read = imageReader.read(0);
        JFrame f2 = new JFrame();
        JLabel l = new JLabel();
        l.setIcon(new ImageIcon(read));
        f2.getContentPane().add(l);
        f2.setSize(100, 100);
        f2.setTitle("验证码");
        f2.setVisible(true);

        String fjmStr = JOptionPane.showInputDialog("请输入验证码：");
        f2.setVisible(false);
        fjm.focus();
        fjm.type(fjmStr);

        userName.focus();
        userName.type("13548130157j");
        password.focus();
        password.type("901901kk");
        loginButton.focus();

        ScriptResult scriptResult = page.executeJavaScript("document.forms[0].submit();");
        HtmlPage newPage = (HtmlPage) scriptResult.getNewPage();

//        ScriptResult result = page.executeJavaScript("document.forms[0].submit()");
//        HtmlPage newPage = (HtmlPage) result.getNewPage();
        webClient.waitForBackgroundJavaScript(8000);
//        HtmlForm htmlForm = newPage.getForms().get(0);
//        String skey = htmlForm.getInputByName("SKEY").getValueAttribute();
//        String mingxi = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_10?SERVLET_NAME=B2CMainPlat_10&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=310203&SKEY="+skey+"&USERID=510603199102166195&BRANCHID=510000000&ACC_NO=6217003810054854834&STR_USERID=510603199102166195&FLAG_CARD=0&BANK_NAME=510000000&ACC_SIGN=6217003810054854834&SEND_USERID=undefined";

//        WebRequest webRequest = new WebRequest(new URL(mingxi), HttpMethod.POST);

//        HtmlPage mingxiPage = webClient.getPage(webRequest);
        Thread.sleep(3 * 1000);
//        System.out.println(mingxiPage.asText());
        System.out.println(newPage.asXml());
        System.exit(0);
//

//            InputStream in = mingxiPage.getWebResponse().getContentAsStream();
//            FileOutputStream fos = new FileOutputStream(new File("/Users/Maple.Ran/Downloads/jh.xls"));
//            IOUtils.copy(in, fos);
//            fos.close();
//
//        } else {
//            System.out.println("登录失败!");
//        }

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
//
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
//                SendSmsResponse sendSmsResponse = SmsUtil.sendSms("17780683073", "冉冉", "川A10929", "22", "3333");
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
        List<Driver> driverList = driverMapper.selectDriverListByStatus(Const.DriverStatus.NORMAL_DRIVER.getCode());
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
    public void accountInsert() throws IOException {
        File slh = new File("/Users/Maple.Ran/Downloads/accountInfo.xls");
        InputStream fileInputStream = new FileInputStream(slh);

        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String acctName = row.getCell(1).getStringCellValue();
            String acctNo = row.getCell(2).getStringCellValue();
            List<Driver> driverList = driverMapper.selectbydriverName(acctName);
            if (driverList.size() == 0 || driverList.size() > 1) {
                System.out.println("无此人或重名:" + acctName + "-" + acctNo);
                continue;
            }
            Driver driver = driverList.get(0);
            Account account = new Account();
            account.setAccount(acctNo);
            account.setDriverId(driver.getId());
            account.setPlatform(Const.PaymentPlatform.pingan.getCode());
            account.setName(acctName);
            accountMapper.insert(account);
        }
    }
}

