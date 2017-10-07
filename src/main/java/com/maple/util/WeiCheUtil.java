package com.maple.util;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/10/6.
 */
public class WeiCheUtil {
    public static String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Mobile/15A372 MicroMessenger/6.5.9 NetType/WIFI Language/zh_CN";
    public static String origin = "https://wx.wcar.net.cn";
    public static String referer = "https://wx.wcar.net.cn/wechat-weiche.php";
    public static String wx = "https://wx.wcar.net.cn/front/do-weiche.php";

    public static String net(List<NameValuePair> params) throws IOException {
        WebClient webClient = null;
        String result = null;
        try {
        HashMap<String, String> Headers = Maps.newHashMap();
        //设置header
            Headers.put("User-Agent", userAgent);
            Headers.put("Origin", origin);
            Headers.put("Referer", referer);
            Headers.put("Content-Type", "application/x-www-form-urlencoded");
            URL url = new URL(wx);
            webClient = new WebClient();
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
            //**设置编码格式
            request.setEncodingType(FormEncodingType.URL_ENCODED);
            request.setRequestParameters(params);
            //**设置字符集
            request.setCharset(Charsets.UTF_8);
            HtmlPage page = webClient.getPage(request);
            result= page.asText();
        }finally {
            if (webClient != null) {
                webClient.close();
            }
        }
        return result;
    }


    public static Map getTicket(String city, String plateNum, String vin, String engineNum) throws IOException {
        HashMap ticket = Maps.newHashMap();


        WeiCheUtil weiCheUtil = new WeiCheUtil();
        String vid = null;
        Map<String, String> addResult = weiCheUtil.add(city, plateNum, vin, engineNum);

        String addStatus = addResult.get("status");
        vid = addResult.get("vid");

        if (addStatus.equals("vehicle_exists")) {
//            delete(vid);
//            getTicket(city, plateNum, vin, engineNum);
        } else if (addStatus.equals("failed")) {
            //车辆信息有错误
        } else if (addStatus.equals("get session failed")) {

        }
        Map<String, String> checkResult = weiCheUtil.check(vid);
        String checkStatus = checkResult.get("status");
        //如果查询结果为ok 则先删除 然后返回 查询结果
        if (checkStatus.equals("ok")|| checkStatus.equals("empty")) {
            Map<String, String> deleteResult = delete(vid);
            String deleteStatus = deleteResult.get("status");
            if (!deleteStatus.equals("ok")) {
                throw new RuntimeException("删除失败");
            }
            return checkResult;
        }
        delete(vid);
        return null;
    }

    public static Map<String,String> jsonToMap(String json) throws IOException {
        HashMap<String,String> result = Maps.newHashMap();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;

        JsonNode root = null;
        root = mapper.readTree(json);
        String status = root.path("status").asText();
        JsonNode data = root.path("data");
        String vid = data.path("vehicle_id").asText();
        String ticketTimes = data.path("unhandled_violation_count").asText();
        String ticketMoney = data.path("unhandled_violation_fine").asText();
        String ticketScore = data.path("unhandled_violation_points").asText();


        result.put("status", status);
        result.put("vid", vid);
        result.put("ticketTimes", ticketTimes);
        result.put("ticketMoney", ticketMoney);
        result.put("ticketScore", ticketScore);

        return result;

    }

    private Map<String,String> add(String city,String plateNum,String vin,String engineNum) throws IOException {
        List<NameValuePair> params = Lists.newArrayList();
        String licenseNumber = plateNum.substring(1, 7);
        String result = null;
//        String licenseNum = null;
//        try {
//            licenseNum=java.net.URLEncoder.encode(plateNum,   "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        params.add(new NameValuePair("limit_push", "0"));
        params.add(new NameValuePair("violation_push", "0"));
        params.add(new NameValuePair("action", "add"));
        params.add(new NameValuePair("license_number", licenseNumber));
        params.add(new NameValuePair("license_num", plateNum));
        params.add(new NameValuePair("subscribe", "0"));
        params.add(new NameValuePair("cid", "13"));
        params.add(new NameValuePair("engine_num", engineNum));
        params.add(new NameValuePair("body_num", vin));
        params.add(new NameValuePair("city_name", city));

        try {
             result= WeiCheUtil.net(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("add-"+result);
        return jsonToMap(result);
    }

    private Map<String,String> check(String vid) throws IOException {
        List<NameValuePair> params = Lists.newArrayList();
        String result = null;
        params.add(new NameValuePair("action", "violations"));
        params.add(new NameValuePair("vid", vid));
        params.add(new NameValuePair("page", "0"));
        try {
            result= WeiCheUtil.net(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("check-"+result);
        return jsonToMap(result);
    }

    public static Map<String,String> delete(String vid) throws IOException {
        List<NameValuePair> params = Lists.newArrayList();
        String result = null;
        params.add(new NameValuePair("action", "del"));
        params.add(new NameValuePair("vid", vid));
        try {
            result= WeiCheUtil.net(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("delete-"+result);
        return jsonToMap(result);
    }

    private Map<String,String>  list(String vid) throws IOException {
        List<NameValuePair> params = Lists.newArrayList();
        String result = null;
        params.add(new NameValuePair("action", "list"));
        try {
            result= WeiCheUtil.net(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("list-"+result);
        return jsonToMap(result);
    }

}
