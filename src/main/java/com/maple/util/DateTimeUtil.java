package com.maple.util;

import com.google.common.collect.Lists;
import com.maple.common.Const;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateTimeUtil() {}

    public static Date strToDate(String str,String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date,String format) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }

    public static Date strToDate(String str) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str).secondOfDay().withMinimumValue();
        return dateTime.toDate();
    }

    public static Date webStrToDate(String str) {
        if (str.contains("T")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Const.JS_DATE_FORMAT);
            DateTime dateTime = dateTimeFormatter.parseDateTime(str);
            return dateTime.toDate();
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime dateTime = dateTimeFormatter.parseDateTime(str);
            return dateTime.toDate();
        }
    }

    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    public static Date getWeekStartDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.TUESDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    public static Date getWeekEndDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.TUESDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
    public static Long getYearWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.TUESDAY);
        long week = cal.get(Calendar.WEEK_OF_YEAR);
        DecimalFormat aa = new DecimalFormat("00");
        String format = aa.format(week);
        week=Long.parseLong(format);
        long year = cal.get(Calendar.YEAR);
        return year*100+week-1;
    }

    public static Date getMonthStartDate(Date date) {
        LocalDate localDate = new LocalDate(date);
        return localDate.dayOfMonth().withMinimumValue().toDate();
    }

    public static Date getMonthEndDate(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(1).dayOfMonth().withMinimumValue().minusSeconds(1).toDate();
    }

    public static List<Date> getWeekStartDateList(Date startDate, Date endDate){
        List<Date> weekStartDateList = Lists.newArrayList();
        while (Math.abs(startDate.getTime()-endDate.getTime())>1000){
            weekStartDateList.add(startDate);
            DateTime dateTime = new DateTime(startDate);
            dateTime = dateTime.plusWeeks(1);
            startDate=dateTime.toDate();
        }
        weekStartDateList.add(endDate);

        return weekStartDateList;

    }

}
