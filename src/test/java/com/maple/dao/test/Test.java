package com.maple.dao.test;

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
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

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

    @org.junit.Test
    public void Task1Test() throws IOException, InterruptedException {
        DateTime dateTime = new DateTime(2017, 10, 1, 0, 0, 0);
        System.out.println(dateTime.getDayOfWeek());
    }

    @org.junit.Test
    public void Task2Test() throws IOException, ParseException {
        String jsonStr = "{\"errno\":\"0\",\"error\":\"\",\"data\":{\"data\":[{\"driver_id\":\"563547272712194\",\"phone\":\"17711393872\",\"name\":\"\\u674e\\u5bb6\\u624d\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,222.84\",\"finish_finish_cnt\":\"67\",\"finish_serve_time\":\"27.37\",\"finish_online_time\":\"35.85\",\"finish_work_distance\":\"882.52\",\"finish_fee_time\":\"21.67\",\"finish_serve_distance\":\"525.64\"},{\"driver_id\":\"564252172951552\",\"phone\":\"17502818042\",\"name\":\"\\u738b\\u4e91\\u5175\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"827.48\",\"finish_finish_cnt\":\"92\",\"finish_serve_time\":\"25.77\",\"finish_online_time\":\"30.67\",\"finish_work_distance\":\"811.66\",\"finish_fee_time\":\"18.38\",\"finish_serve_distance\":\"433.59\"},{\"driver_id\":\"566328098296719\",\"phone\":\"17502843927\",\"name\":\"\\u962e\\u51ef\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,403.88\",\"finish_finish_cnt\":\"165\",\"finish_serve_time\":\"64.61\",\"finish_online_time\":\"64.63\",\"finish_work_distance\":\"1,492.90\",\"finish_fee_time\":\"51.64\",\"finish_serve_distance\":\"1,326.77\"},{\"driver_id\":\"567949977109270\",\"phone\":\"17381825856\",\"name\":\"\\u5b5f\\u7ea2\\u9633\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"368.97\",\"finish_finish_cnt\":\"19\",\"finish_serve_time\":\"5.92\",\"finish_online_time\":\"10.30\",\"finish_work_distance\":\"241.79\",\"finish_fee_time\":\"4.42\",\"finish_serve_distance\":\"158.50\"},{\"driver_id\":\"567949981922996\",\"phone\":\"13568833758\",\"name\":\"\\u90d1\\u548c\\u5e73\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,990.04\",\"finish_finish_cnt\":\"122\",\"finish_serve_time\":\"48.11\",\"finish_online_time\":\"57.80\",\"finish_work_distance\":\"1,200.38\",\"finish_fee_time\":\"36.78\",\"finish_serve_distance\":\"876.53\"},{\"driver_id\":\"567950108226408\",\"phone\":\"15982154365\",\"name\":\"\\u7530\\u4f1f\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,881.71\",\"finish_finish_cnt\":\"168\",\"finish_serve_time\":\"68.09\",\"finish_online_time\":\"84.30\",\"finish_work_distance\":\"1,707.34\",\"finish_fee_time\":\"52.50\",\"finish_serve_distance\":\"1,258.11\"},{\"driver_id\":\"567950191856229\",\"phone\":\"13880858819\",\"name\":\"\\u9093\\u7ee7\\u660e\",\"join_model_name\":\"\\u666e\\u901a\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,526.41\",\"finish_finish_cnt\":\"173\",\"finish_serve_time\":\"76.63\",\"finish_online_time\":\"86.52\",\"finish_work_distance\":\"1,686.91\",\"finish_fee_time\":\"60.65\",\"finish_serve_distance\":\"1,347.83\"},{\"driver_id\":\"563309467865088\",\"phone\":\"18781971612\",\"name\":\"\\u674e\\u6d9b\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,523.67\",\"finish_finish_cnt\":\"108\",\"finish_serve_time\":\"44.13\",\"finish_online_time\":\"53.33\",\"finish_work_distance\":\"1,184.30\",\"finish_fee_time\":\"34.86\",\"finish_serve_distance\":\"884.16\"},{\"driver_id\":\"563570862268418\",\"phone\":\"15982094997\",\"name\":\"\\u90ac\\u56fd\\u5f3a\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"436.02\",\"finish_finish_cnt\":\"37\",\"finish_serve_time\":\"15.05\",\"finish_online_time\":\"20.35\",\"finish_work_distance\":\"392.07\",\"finish_fee_time\":\"10.90\",\"finish_serve_distance\":\"255.47\"},{\"driver_id\":\"563768012378115\",\"phone\":\"17508241661\",\"name\":\"\\u738b\\u6d2a\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,559.26\",\"finish_finish_cnt\":\"122\",\"finish_serve_time\":\"59.46\",\"finish_online_time\":\"67.77\",\"finish_work_distance\":\"1,364.95\",\"finish_fee_time\":\"48.92\",\"finish_serve_distance\":\"1,086.75\"},{\"driver_id\":\"563838062632964\",\"phone\":\"17502854998\",\"name\":\"\\u8d75\\u6cc9\\u8363\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"623.33\",\"finish_finish_cnt\":\"25\",\"finish_serve_time\":\"10.99\",\"finish_online_time\":\"20.63\",\"finish_work_distance\":\"442.51\",\"finish_fee_time\":\"9.41\",\"finish_serve_distance\":\"283.08\"},{\"driver_id\":\"563905818726400\",\"phone\":\"17502888032\",\"name\":\"\\u5218\\u7965\\u6797\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"0.00\",\"finish_finish_cnt\":\"0\",\"finish_serve_time\":\"0.00\",\"finish_online_time\":\"15.03\",\"finish_work_distance\":\"67.67\",\"finish_fee_time\":\"0.00\",\"finish_serve_distance\":\"0.00\"},{\"driver_id\":\"564426981840517\",\"phone\":\"13550265868\",\"name\":\"\\u7f57\\u6da6\\u5229\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,118.18\",\"finish_finish_cnt\":\"139\",\"finish_serve_time\":\"52.40\",\"finish_online_time\":\"75.37\",\"finish_work_distance\":\"1,589.12\",\"finish_fee_time\":\"40.49\",\"finish_serve_distance\":\"1,030.59\"},{\"driver_id\":\"565246901028600\",\"phone\":\"17780021861\",\"name\":\"\\u674e\\u5f6c\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"3,115.91\",\"finish_finish_cnt\":\"180\",\"finish_serve_time\":\"56.12\",\"finish_online_time\":\"77.13\",\"finish_work_distance\":\"2,068.68\",\"finish_fee_time\":\"43.81\",\"finish_serve_distance\":\"1,370.26\"},{\"driver_id\":\"563753887932416\",\"phone\":\"18181993085\",\"name\":\"\\u9ad8\\u5cf0\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,685.86\",\"finish_finish_cnt\":\"100\",\"finish_serve_time\":\"31.04\",\"finish_online_time\":\"43.68\",\"finish_work_distance\":\"1,087.75\",\"finish_fee_time\":\"24.00\",\"finish_serve_distance\":\"780.50\"},{\"driver_id\":\"564120700387328\",\"phone\":\"18116563838\",\"name\":\"\\u90d1\\u52c7\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,712.06\",\"finish_finish_cnt\":\"121\",\"finish_serve_time\":\"40.35\",\"finish_online_time\":\"49.87\",\"finish_work_distance\":\"1,083.06\",\"finish_fee_time\":\"31.81\",\"finish_serve_distance\":\"876.90\"},{\"driver_id\":\"565274257992587\",\"phone\":\"13693462956\",\"name\":\"\\u848b\\u4ed5\\u89c1\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,706.39\",\"finish_finish_cnt\":\"102\",\"finish_serve_time\":\"45.88\",\"finish_online_time\":\"60.20\",\"finish_work_distance\":\"1,554.27\",\"finish_fee_time\":\"37.56\",\"finish_serve_distance\":\"908.43\"},{\"driver_id\":\"565770239549420\",\"phone\":\"13699434315\",\"name\":\"\\u90b1\\u5146\\u52c7\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"601.19\",\"finish_finish_cnt\":\"28\",\"finish_serve_time\":\"10.02\",\"finish_online_time\":\"18.13\",\"finish_work_distance\":\"507.01\",\"finish_fee_time\":\"7.91\",\"finish_serve_distance\":\"274.98\"},{\"driver_id\":\"565885572029510\",\"phone\":\"15208206210\",\"name\":\"\\u674e\\u514b\\u52c7\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"929.27\",\"finish_finish_cnt\":\"57\",\"finish_serve_time\":\"27.06\",\"finish_online_time\":\"33.25\",\"finish_work_distance\":\"582.39\",\"finish_fee_time\":\"21.91\",\"finish_serve_distance\":\"512.50\"},{\"driver_id\":\"566100585484894\",\"phone\":\"18140202795\",\"name\":\"\\u5218\\u5e94\\u5747\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,540.82\",\"finish_finish_cnt\":\"73\",\"finish_serve_time\":\"35.27\",\"finish_online_time\":\"50.27\",\"finish_work_distance\":\"2,155.60\",\"finish_fee_time\":\"31.82\",\"finish_serve_distance\":\"1,192.60\"},{\"driver_id\":\"566270765828824\",\"phone\":\"13340961006\",\"name\":\"\\u90ed\\u5e38\\u8f89\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,783.61\",\"finish_finish_cnt\":\"76\",\"finish_serve_time\":\"32.71\",\"finish_online_time\":\"51.07\",\"finish_work_distance\":\"1,614.40\",\"finish_fee_time\":\"28.46\",\"finish_serve_distance\":\"818.32\"},{\"driver_id\":\"566293516921474\",\"phone\":\"17340122837\",\"name\":\"\\u4faf\\u777f\\u535a\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"A+\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"966.79\",\"finish_finish_cnt\":\"38\",\"finish_serve_time\":\"16.38\",\"finish_online_time\":\"34.57\",\"finish_work_distance\":\"828.10\",\"finish_fee_time\":\"14.20\",\"finish_serve_distance\":\"466.35\"},{\"driver_id\":\"566294091140639\",\"phone\":\"13308065745\",\"name\":\"\\u9648\\u7ecd\\u6167\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"512.92\",\"finish_finish_cnt\":\"32\",\"finish_serve_time\":\"10.09\",\"finish_online_time\":\"14.82\",\"finish_work_distance\":\"624.12\",\"finish_fee_time\":\"7.72\",\"finish_serve_distance\":\"252.29\"},{\"driver_id\":\"567949985812010\",\"phone\":\"17508240579\",\"name\":\"\\u5f20\\u60f3\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"1,838.02\",\"finish_finish_cnt\":\"133\",\"finish_serve_time\":\"44.08\",\"finish_online_time\":\"57.05\",\"finish_work_distance\":\"1,460.18\",\"finish_fee_time\":\"34.51\",\"finish_serve_distance\":\"946.74\"},{\"driver_id\":\"567950010663002\",\"phone\":\"17502871834\",\"name\":\"\\u6b66\\u5b66\\u65fa\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"569.86\",\"finish_finish_cnt\":\"35\",\"finish_serve_time\":\"13.80\",\"finish_online_time\":\"17.38\",\"finish_work_distance\":\"445.52\",\"finish_fee_time\":\"11.12\",\"finish_serve_distance\":\"287.94\"},{\"driver_id\":\"567950030484408\",\"phone\":\"18010634542\",\"name\":\"\\u7f57\\u65b9\\u7ef4\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,258.12\",\"finish_finish_cnt\":\"160\",\"finish_serve_time\":\"69.66\",\"finish_online_time\":\"70.30\",\"finish_work_distance\":\"1,493.37\",\"finish_fee_time\":\"55.70\",\"finish_serve_distance\":\"1,272.28\"},{\"driver_id\":\"565689403638882\",\"phone\":\"17502817802\",\"name\":\"\\u5170\\u5fd7\\u521a\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,643.51\",\"finish_finish_cnt\":\"192\",\"finish_serve_time\":\"70.81\",\"finish_online_time\":\"77.33\",\"finish_work_distance\":\"1,700.00\",\"finish_fee_time\":\"57.26\",\"finish_serve_distance\":\"1,388.63\"},{\"driver_id\":\"565695857166930\",\"phone\":\"13881780445\",\"name\":\"\\u738b\\u5f3a\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"2,372.34\",\"finish_finish_cnt\":\"172\",\"finish_serve_time\":\"63.71\",\"finish_online_time\":\"65.85\",\"finish_work_distance\":\"1,480.51\",\"finish_fee_time\":\"49.44\",\"finish_serve_distance\":\"1,235.52\"},{\"driver_id\":\"565711956676124\",\"phone\":\"18180232446\",\"name\":\"\\u6881\\u7389\\u9e9f\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"0.00\",\"finish_finish_cnt\":\"0\",\"finish_serve_time\":\"0.00\",\"finish_online_time\":\"0.00\",\"finish_work_distance\":\"0.00\",\"finish_fee_time\":\"0.00\",\"finish_serve_distance\":\"0.00\"},{\"driver_id\":\"565715843486741\",\"phone\":\"15328088259\",\"name\":\"\\u53f6\\u4fdd\\u821f\",\"join_model_name\":\"\\u5bf9\\u516c\\u52a0\\u76df\",\"check_level_name\":\"\\u4f18\\u9009\",\"date\":\"2017-06-26\\u81f32017-07-02\",\"finish_flowfee\":\"488.88\",\"finish_finish_cnt\":\"35\",\"finish_serve_time\":\"9.46\",\"finish_online_time\":\"16.97\",\"finish_work_distance\":\"390.52\",\"finish_fee_time\":\"7.48\",\"finish_serve_distance\":\"228.63\"}],\"count\":216,\"order_column\":\"date\",\"order_type\":2,\"start_time\":\"2017-06-26\",\"end_time\":\"2017-07-02\",\"message\":\"\"}}";
        Class classType = FinishOrder.class;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonStr);
        JsonNode objectData = root.path("data");
        JsonNode arrayData = objectData.path("data");
        JsonNode count = objectData.path("count");
        DecimalFormat decimalFormat = new DecimalFormat("0,000.00");
        List<HashMap<String,Object>> list = mapper.readValue(arrayData, List.class);

    }

    @org.junit.Test
    public void driverTest() throws Exception {
        Car car = carMapper.selectByPrimaryKey(100001);
        String vin = car.getVin().substring(11, 17);
        String engineNum = car.getEngineNumber().substring(1, 7);
        System.out.println(vin);
        System.out.println(engineNum);
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
        Thread.sleep(3000);

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
            reqParams.add(new NameValuePair("startDate", "20171013"));
            reqParams.add(new NameValuePair("endDate", "20171013"));

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
    public void Test3() throws InterruptedException {
        List<Car> carList = carMapper.selectCarListForTicket();
//        List<Car> carList = carMapper.selectWhereUnchecked();
        System.out.println("共"+carList.size()+"待查车辆");
        for (int i =34;i<carList.size();i++){
//        Car car = carMapper.selectByPrimaryKey(100046);
            System.out.println("第"+(i+1)+"车辆正在查询");
        Car car = carList.get(i);
            Map ticketMap = null;
            String vid = null;
            try {
            String vin = car.getVin().substring(11, 17);
            String carEngineNumber = car.getEngineNumber();
            String engineNum = carEngineNumber.substring(carEngineNumber.length() - 6, carEngineNumber.length());


                ticketMap = WeiCheUtil.getTicket("成都", car.getPlateNumber(), vin, engineNum);
                vid = (String) ticketMap.get("vid");
            } catch (Exception e) {
                continue;
            }
            String ticketScore = (String) ticketMap.get("ticketScore");
            String ticketMoney = (String) ticketMap.get("ticketMoney");
            String ticketTimes = (String) ticketMap.get("ticketTimes");


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
            Thread.sleep(100);
        }
    }
}

