package com.maple.util;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
public class BigDecimalUtil {

    private BigDecimalUtil() {}

    public static BigDecimal add(double v1, double v2) {
        BigDecimal r1 = new BigDecimal(Double.toString(v1));
        BigDecimal r2 = new BigDecimal(Double.toString(v2));
        return r1.add(r2);
    }

    public static BigDecimal sub(double v1, double v2) {
        BigDecimal r1 = new BigDecimal(Double.toString(v1));
        BigDecimal r2 = new BigDecimal(Double.toString(v2));
        return r1.subtract(r2);
    }

    public static BigDecimal mul(double v1, double v2) {
        BigDecimal r1 = new BigDecimal(Double.toString(v1));
        BigDecimal r2 = new BigDecimal(Double.toString(v2));
        return r1.multiply(r2);
    }

    public static BigDecimal div(double v1, double v2) {
        BigDecimal r1 = new BigDecimal(Double.toString(v1));
        BigDecimal r2 = new BigDecimal(Double.toString(v2));
        return r1.divide(r2, 2, BigDecimal.ROUND_HALF_UP);
    }
}
