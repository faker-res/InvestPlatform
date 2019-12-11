package la.niub.abcapi.invest.seller.component.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);


    public static long getSecondsByDateTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return 0;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(time);
        return (Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime()) / 1000;
    }

    public static String fillTime4Date(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        return date + "T00:00:00";
    }

    /**
     * 将日期格式转为日期时间格式,目前结束时间为第二天零点
     *
     * @param date 日期字符串，yyyy-MM-dd格式
     * @return 日期时间格式，yyyy-MM-ddTHH:mm:ss
     */
    public static String fillEndTime4Date(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        return date + "T00:00:00";
    }

    public static long getMinEndTime(Long endTime1, Long endTime2) {
        if (endTime1 == null && endTime2 == null) {
            return 0;
        }
        if (endTime1 == null) {
            return endTime2;
        }
        if (endTime2 == null) {
            return endTime1;
        }
        return Math.min(endTime1, endTime2);
    }

    /**
     * 将日期字符串转为时间戳
     *
     * @param dateStr 日期字符串，格式 yyyy年M月d日
     * @return 该日期的时间戳（单位秒）
     */
    public static long getTimeFromDateStr(String dateStr) {
        long time = 0;
        try {
            LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy年M月d日"));
            time = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        } catch (Exception e) {
            logger.error("解析时间错误，data={},e={}", dateStr, e);
        }
        return time;
    }

    public static Date getLastWeek() {
        return getLastDay(7);
    }

    public static Date getLastDay() {
        return getLastDay(0);
    }

    private static Date getLastDay(int day) {
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.of(0, 0);
        LocalDateTime now = LocalDateTime.of(today, time);
        LocalDateTime localDateTime = now.minusDays(day);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long getLastWeekIndexDay(long time) {
        LocalDate lastWekDay = LocalDate.now().minusDays(7);
        long lastWeekTime = Date.from(lastWekDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime();
        long dayMills = 60 * 60 * 24 * 1000;
        for (int i = 0; i < 7; i++) {
            if (time < lastWeekTime) {
                break;
            }
            lastWeekTime += dayMills;
        }
        return lastWeekTime;
    }

    public static long getZeroHour(long time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
