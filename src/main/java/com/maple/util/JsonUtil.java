package com.maple.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.tools.javac.code.Scope;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maple.Ran on 2017/11/4.
 */
public class JsonUtil {

    public static Map pingAnBalance(InputStream inputStream) throws IOException {
        //数据
        HashMap<Object, Object> data = Maps.newHashMap();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(inputStream);
        JsonNode responseBody = root.path("responseBody");
        JsonNode errCode = root.path("errCode");
        //账户数量
        JsonNode accCount = responseBody.path("accCount");
        //合约List的json对象
        JsonNode agreementJsonList = responseBody.path("agreementList");
        JsonNode oldChannelSeqNo = responseBody.path("oldChannelSeqNo");
        //余额Map的Json对象
        JsonNode balanceDtoJsonMap = responseBody.path("balanceDtoMap");
        if (!accCount.isMissingNode()) {
            Integer accCountValue = Integer.valueOf(accCount.getTextValue());
            data.put("accCount", accCountValue);
        }
        if (!agreementJsonList.isMissingNode()) {
            List agreementList = mapper.readValue(agreementJsonList, List.class);
            data.put("agreementList", agreementList);
        }
        //判断此节点是否为空,不为空则添加
        if (!balanceDtoJsonMap.isMissingNode()) {
            Map balanceDtoMap = mapper.readValue(balanceDtoJsonMap, Map.class);
            data.put("balanceDtoMap", balanceDtoMap);
        }
        data.put("errCode", errCode.getTextValue());
        if (!oldChannelSeqNo.isMissingNode()) {
            data.put("oldChannelSeqNo", oldChannelSeqNo.getTextValue());
        }
        return data;
    }

    public static List alipayBalance(String str) {
        List data = Lists.newArrayList();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(str);
            JsonNode result = root.path("result");
            JsonNode detail = result.path("detail");
            data = mapper.readValue(detail, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
