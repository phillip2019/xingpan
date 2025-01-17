package org.jeecg.modules.ibf.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MONTHS;

public class IbfDateUtil {
    public static String getCurrentMonth() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int currentDay = currentDateTime.getDayOfMonth();
        Month currentMonth = currentDateTime.getMonth();
        int currentYear = currentDateTime.getYear();

        if (currentDay <= 20) {
            if (currentMonth == Month.JANUARY) {
                // 如果当前日期是1月1日到1月20日，则月份为上一年12月
                currentYear--;
                currentMonth = Month.DECEMBER;
            } else {
                currentMonth = currentMonth.minus(1);
            }
        }

        // 将月份格式化为两位数字
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return LocalDate.of(currentYear, currentMonth, 1).format(formatter);
    }

    public static long calculateMonthDifference(String monthCol) {
        String curMonth = getCurrentMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth ym1 = YearMonth.parse(curMonth, formatter);
        YearMonth ym2 = YearMonth.parse(monthCol, formatter);

        return ym1.until(ym2, ChronoUnit.MONTHS);
    }

    public static long calculateMonthDifference(String curMonth, String monthCol) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth ym1 = YearMonth.parse(curMonth, formatter);
        YearMonth ym2 = YearMonth.parse(monthCol, formatter);

        return ym1.until(ym2, ChronoUnit.MONTHS);
    }


}
