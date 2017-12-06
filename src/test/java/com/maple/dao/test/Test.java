package com.maple.dao.test;

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
import com.maple.task.AddTask;
import com.maple.task.PaymentQueryTask;
import com.maple.test.TestBase;
import com.maple.util.DateTimeUtil;
import com.maple.util.WeiCheUtil;
import com.maple.vo.PingAnBalanceListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
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
    public void Test3() throws Exception {
        List<Map<String, Object>> data = Lists.newArrayList();
        FileInputStream in = new FileInputStream(new File("/Users/Maple.Ran/Downloads/交易明细 (2).xls"));
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
        insertByList(data);

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
            if (periodPayment == null && amount.compareTo(BigDecimal.ZERO) > 0) {
                PeriodPayment newPayment = assemblePeriodPayment(time, serialNo, payer, accountNo, comment, amount, Const.PaymentPlatform.pingan.getCode());
                periodPaymentMapper.insertSelective(newPayment);
            }
        }
    }

    private PeriodPayment assemblePeriodPayment(String time, String serialNo, String payer,
                                                String accountNo, String comment,
                                                BigDecimal amount, Integer platformCode) {
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
            newPayment.setComment(comment + "-系统导入");
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
            Date weekStartDate = new Date();
            if (coModel.getModelType() == Const.CoModel.HIRE_PURCHASE_WEEK.getCode()) {
                weekStartDate = DateTimeUtil.getWeekStartDate(payTime);
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
            newPayment.setComment(comment + "-系统导入");
            //付款对应日期
            newPayment.setPayTime(weekStartDate);
            //付款时间
            newPayment.setCreateTime(payTime);

        }
        return newPayment;
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
        File slh = new File("/Users/Maple.Ran/Downloads/12-6-payment.xls");
        InputStream fileInputStream = new FileInputStream(slh);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        PeriodPayment newPayment = new PeriodPayment();
        Double total = 0d;

        for (int i = 5; i <= sheet.getLastRowNum(); i++) {
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
            DateTime startDate = new DateTime("2017-10-03");
            for (int j =86;j<row.getLastCellNum();j++) {
                Cell amount = row.getCell(j);
                if (amount == null) {
                    continue;
                }
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
//                    periodPaymentMapper.insertSelective(newPayment);
                    System.out.println("------------"+amount.getNumericCellValue()+"-------------");
                    total = total + amount.getNumericCellValue();
                }

                startDate = startDate.plusWeeks(1);
            }
        }
        System.out.println("-------------------"+total+"----------------------");
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
        String os = System.getProperties().getProperty("os.name");
        System.out.println(os);
    }

    @org.junit.Test
    public void test2 () throws IOException {
        System.out.println(Math.pow(2,127)-1);
    }


    @org.junit.Test
    public void accountInsert() throws IOException {

    }
}

