package com.maple.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Maple.Ran on 2017/5/23.
 */
public class PropertiesUtil {

//    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties props;

    static {
        String fileName = "maple.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            System.out.println("配置文件异常"+e);
        }
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return value.trim();
        }
        return null;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return value.trim();
        }
        return defaultValue;
    }

}

