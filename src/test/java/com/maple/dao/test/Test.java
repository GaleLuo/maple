package com.maple.dao.test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.maple.common.Const;
import com.maple.common.ServerResponse;
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
import org.eclipse.jetty.util.DateCache;
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


    @org.junit.Test
    public void Task1Test() throws IOException, InterruptedException {
        String t = "1503894370812";
        Long aLong = Long.parseLong(t);
        Date date = new Date(aLong);
        System.out.println(date);
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
        Date strToDate = DateTimeUtil.strToDate("2017/2/10", "yyyy/MM/dd");
        System.out.println(strToDate);

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
    public void pageTest() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2017091208700001", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDPnBS68scX1tzNH4Cm6Ce/9A+koLCmIhDn+GCH49jaeJhq2ZMembW82C4HJzMif8Y2LpPrqq3Wt9QZTYbdKf1gUbfuCVsP69qYfR48PzjnzqDnpFIa96pCdSOj4KenHxblTYRxf6k6FggbPOC/IIu/LQKclZsRTVhLoJ3HS3u+5sJ3mrqyiyxy7xG000S0H8fbaIDkzgL7cZ3iBnhwMLuIpQS2yR1cNtvpRIRgKar4x6ZOKShsj7iiMwd9bMAJp4RRsAljjh6EBLJ2fCHe28gPXRdglC7CpKU//UiBFKaVmuf75arfnyQnrwaRKiZl5BykZ9ZxcIB8kdetWdICfq3rAgMBAAECggEADAmoyg45rRAwTVR+SS883sxv+8O1emyPPmQQdNCpSkGj1M440ZVoDEMcqB2FwYJXI90fhpKm8cRG8BCmuTwuDN+wIoU4Wnv6eM0Ia8XertI0Ujoc6KNjo+bW4jGshuwuj60m+M3GKjAM2Ed3YJ2qfLNHa3zhOOebLwdpHp0p16++6CNY3Y1pocuFQjX3ceGEA2w32RKqmxI61z7qjNF4LakurIaJSaZXIF1Y09DJeClQgwWoiFZFCVPXO/AMqmQyEGVjVzXY6xqToueEojmexoK1hPirGnWcQAK5ChiC6OddWrGX52RoTmRTboajigIsJWqC5D1JthrYvKLHKZssgQKBgQDtAkDw9nuTw6/vggSuWWEzZ19z8a5o3LvVxie2deKTukAyBluWQ3kDlfehrV4uIJmlseelEDU2Dw7to/PgjjIy586t19XkyTFOCPfndl3oZfV78HJyZQFNgbOn4T+p7xJga63zBLsWBV9jRS3H9ELw28Yh1Q+4Mhp+dECdJLwMWQKBgQDgPsPknxLikt/NvI9rP7t88iIpKE9SDa1Mlyu0ypttY5cxKM0Rl/VN5zWhaE+2cquIIgnDF3LwjBNbE7oLgvINvGBMJT1WOtwZrw461GYXtBVqalNQXZcIraZBceq/HXzKQRAzNSS/zE5v55okMaYlWBUE/4hDezQO4hdVcloz4wKBgQDJ1Pt8uEpwgVrX+b8GwnK2FbgBwrSl9CO5Xq/+/9DdwOReNW/VPMxXM2TYOA1V9skhg6kitfS6foRV9yL9/dnd4+3ruTwbQwJL0/NZF0HNYNZ2n0K3DDDZ0jQuKPHQQxlBJzHRHBw+GoEqxoTAHNDM0DugIBLs5y4Mte84Y0oIsQKBgQCFmT9SROeWb1TBGRcFGYpVDVkG1kozo4xc9i+G3bKuAEiCdEGAutzd1eMgz6m2FClXyJeyUJUc9QdPGribxR76ygFBPGqHpjedYasDp6lPc+3SYZhTZ/7kn7hOg1UNGb9QLGU/bOkBD/KnPv+Zkq7eztRvDuKa+ZrxRwgJ00AkawKBgEWfh1Phu+Bz6ZqCx+PFxTnEGYGGv1nnPtHsKmqU2Mu3iSvuCKnDzUGezm4mYgnvwdm589zzV3tplQ1lDeJ+tVGY+Kt03ZhNbw0hGCFLEORVpU5TZ0fj1yOP93B+6hfV6TSn55KqXcYEAhDCSkEUyltODF3d+jUfx3jhW9ObTPT+", "json", "UTF-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApZ6Eps/+MJ9yPuIbcvjK9OsR6rusOQw6cjD+gdE1XAycvmq90g0J2L5aaDWJdSkdC1H42HHp9wNr3lEJHjeIAHRFXmGe2WgMnrHneDhw8VzJSOULC79ALWYspJHkB0V6UoPksONrRrCa8+cCNiJX3nIcrtvMdO1uyY/+scHyuBa4DpzWZUrWiXBfLU9h8tDKqect29xn1Pdo0fLmT6tpBWTUxzrHja00ibysqYbFISdykWXDXcD93ZM7t7WaadbFvGX+VFExhjS5KA0WB3Pukkz3Y5xwC/iRju8P3ZbcLV07KSv9mcQm+fDqx38uPyM6qUNS4BS6dzzho5LSfVJuVwIDAQAB", "RSA2");
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"bill_type\":\"trade\"," +
                "    \"bill_date\":\"2017-09-11\"}"); //设置业务参数
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        System.out.print(response.getBody());
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
        userName.type("510602196910260980");
        page.getHtmlElementById("pwdObject1-btn").click();
        page.getHtmlElementById("pa_ui_keyboard_close").click();
        password.focus();
        password.type("guoyouju840");
        loginBtn.click();
        Thread.sleep(4000);
        String result = page.asText();
        System.out.println(result);
        if (result.contains("郭佑菊")) {
            System.out.println("登录成功!");
            WebRequest webRequest = new WebRequest(new URL(DOWNLOAD), HttpMethod.POST);
            List<NameValuePair> reqParams = Lists.newArrayList();
            reqParams.add(new NameValuePair("pageNum", "1"));
            reqParams.add(new NameValuePair("pageSize", "99999"));
            reqParams.add(new NameValuePair("accNo", "6230583000001164831"));
            reqParams.add(new NameValuePair("currType", "RMB"));
            reqParams.add(new NameValuePair("startDate", "20170709"));
            reqParams.add(new NameValuePair("endDate", "20170712"));

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
                    //结束日期最接近的星期二
                    endDate = DateTimeUtil.getWeekStartDate(endDate);
                    if (secondPP.getNumericCellValue() != 0) {
                        secondPlan = new PeriodPlan();
                        secondPlan.setAmount(new BigDecimal(secondPP.getNumericCellValue() / 4));
                        Date secondStartDate = endDate;//第二个周期起始时间为第一周期结束时间
                        secondPlan.setStartDate(secondStartDate);//赋值
                        Date secondEndDate = new DateTime(secondStartDate).plusMonths((int) secondPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
                        secondEndDate = DateTimeUtil.getWeekStartDate(secondEndDate);//最近的星期二
                        if (thirdPP.getNumericCellValue() != 0) {
                            thirdPlan = new PeriodPlan();
                            thirdPlan.setAmount(new BigDecimal(thirdPP.getNumericCellValue() / 4));
                            Date thirdStartDate = secondEndDate;
                            thirdPlan.setStartDate(thirdStartDate);
                            Date thirdEndDate = new DateTime(thirdStartDate).plusMonths((int) thirdPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
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
                        //结束日期最接近的星期二
                        endDate = DateTimeUtil.getWeekStartDate(endDate);
                        if (secondPP.getNumericCellValue() != 0) {
                            secondPlan = new PeriodPlan();
                            secondPlan.setAmount(new BigDecimal(secondPP.getNumericCellValue() / 4));
                            Date secondStartDate = endDate;//第二个周期起始时间为第一周期结束时间
                            secondPlan.setStartDate(secondStartDate);//赋值
                            Date secondEndDate = new DateTime(secondStartDate).plusMonths((int) secondPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
                            secondEndDate = DateTimeUtil.getWeekStartDate(secondEndDate);//最近的星期二
                            if (thirdPP.getNumericCellValue() != 0) {
                                thirdPlan = new PeriodPlan();
                                thirdPlan.setAmount(new BigDecimal(thirdPP.getNumericCellValue() / 4));
                                Date thirdStartDate = secondEndDate;
                                thirdPlan.setStartDate(thirdStartDate);
                                Date thirdEndDate = new DateTime(thirdStartDate).plusMonths((int) thirdPPN.getNumericCellValue()).toDate();//第二周结束日期为加上相应月数
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
        Driver driver = driverMapper.selectByPrimaryKey(61222);
        Car car = carMapper.selectByPrimaryKey(driver.getCarId());

        String ticket = TicketQueryUtil.getTicket("SC_CD", car.getPlateNumber(), car.getEngineNumber(), car.getVin());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(ticket);
        JsonNode resultcodeNode = root.get("resultcode");
        Integer resultCode = mapper.readValue(resultcodeNode, Integer.class);
        JsonNode result = root.get("result");

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
    public void Test3() {

    }
}

