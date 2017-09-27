package com.maple.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/9/23.
 */
public class TicketQueryUtil {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY =PropertiesUtil.getProperty("juhe.appKey");

    //1.获取支持城市参数接口
    public static void getRequest1(){
        String result =null;
        String url ="http://v.juhe.cn/wz/citys";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("province","");//默认全部，省份简写，如：ZJ、JS
        params.put("dtype","");//返回数据格式：json或xml或jsonp,默认json
        params.put("format","");//格式选择1或2，默认1
        params.put("callback","");//返回格式选择jsonp时，必须传递
        params.put("key",APPKEY);//你申请的key

        try {
            result =net(url, params, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.请求违章查询接口
    public static String getTicket(String city,String plateNum,String engineNum,String vin){
        String result =null;
        String url ="http://v.juhe.cn/wz/query";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("dtype","json");//返回数据格式：json或xml或jsonp,默认json
        params.put("callback","");//返回格式选择jsonp时，必须传递
        params.put("key",APPKEY);//你申请的key
        params.put("city",city);//城市代码 *
        params.put("hphm",plateNum);//号牌号码 完整7位 ,需要utf8 urlencode*
        params.put("hpzl","02");//号牌类型，默认02
        params.put("engineno",engineNum);//发动机号 (根据城市接口中的参数填写)
        params.put("classno",vin);//车架号 (根据城市接口中的参数填写)

        try {
            result =net(url, params, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //3.接口剩余请求次数查询
    public static String getRequest3(){
        String result =null;
        String url ="http://v.juhe.cn/wz/status";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","json");//返回数据的格式,xml或json，默认json

        try {
            result =net(url, params, "GET");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
