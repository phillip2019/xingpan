package org.jeecg.modules.ibf.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

@Slf4j
public class IbfDateUtil {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getCurrentMonth() {
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        if (currentDay <= 20) {
            if (currentMonth == 1) {
                currentYear--;
                currentMonth = 12;
            } else {
                currentMonth--;
            }
        }

        return String.format("%d-%02d", currentYear, currentMonth);
    }

    /**
     * @description 获得某个月份的上一个月份
     * @author xiaowei.song
     * @date 18/1/2025 下午 4:44
     * @version v1.0.0
     **/
    public static String getLastMonth(String monthCol) {
        String[] split = monthCol.split("-");
        if (split.length == 2) {
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            return String.format("%d-%02d", year, month);
        }
        log.error("monthCol格式错误");
        throw new RuntimeException("monthCol格式错误");
    }

    /***
     * @description 获取当前月份起始时间和结束时间，返回格式为yyyy-MM-dd格式
     *              若当前为1月，则起始时间和结束时间为为1.1,1.20
     *              若当前为2-11月，则起始时间和结束时间为上月21号到本月20号
     *              若当前时间为12月，则起始时间和结束时间为本年11.21号到12.31号
     * @author xiaowei.song
     * @date 18/1/2025 上午 10:42
     * @version v1.0.0
     **/
    public static String[] getCurrentMonthStartAndEndDate(String curMonth) {
        // 当前curMonth转换成LocalDate对象
        LocalDate today = LocalDate.now();
        if (StringUtils.isNotBlank(curMonth)) {
            today = LocalDate.from(LocalDate.parse(curMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        LocalDate startDate;
        LocalDate endDate;

        if (currentMonth == 1) {
            // 1月，起始时间为1月1日，结束时间为1月20日
            startDate = LocalDate.of(currentYear, 1, 1);
            endDate = LocalDate.of(currentYear, 1, 20);
        } else if (currentMonth == 12) {
            // 12月，起始时间为11月21日，结束时间为12月31日
            startDate = LocalDate.of(currentYear, 11, 21);
            endDate = LocalDate.of(currentYear, 12, 31);
        } else {
            // 2-11月，起始时间为上月21日，结束时间为本月20日
            startDate = LocalDate.of(currentYear, currentMonth - 1, 21);
            endDate = LocalDate.of(currentYear, currentMonth, 20);
        }

        return new String[]{startDate.format(dateFormat), endDate.format(dateFormat)};
    }


    public static Integer calculateMonthDifference(String monthCol) {
        String curMonth = getCurrentMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth ym1 = YearMonth.parse(curMonth, formatter);
        YearMonth ym2 = YearMonth.parse(monthCol, formatter);

        return Math.toIntExact(ym1.until(ym2, MONTHS));
    }

    public static Integer calculateMonthDifference(String curMonth, String monthCol) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth ym1 = YearMonth.parse(curMonth, formatter);
        YearMonth ym2 = YearMonth.parse(monthCol, formatter);

        return Math.toIntExact(ym1.until(ym2, MONTHS));
    }
}
