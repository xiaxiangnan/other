package com.journey.other.jdk78new;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Date/Time API
 * Created by xiaxiangnan on 16/2/25.
 */
public class DateTime {

    public static void main(String[] args) {
        /**
         * Clock类，它通过指定一个时区，可以获取到当前的时刻，日期与时间
         */
        // Get the system clock as UTC offset
        final Clock clock = Clock.systemUTC();
        System.out.println(clock.instant());
        System.out.println(clock.millis());

        /**
         * LocaleDate与LocalTime, LocaleDate只持有ISO-8601格式且无时区信息的日期部分。
         * 相应的，LocaleTime只持有ISO-8601格式且无时区信息的时间部分
         */
        // Get the local date and local time
        final LocalDate date = LocalDate.now();
        final LocalDate dateFromClock = LocalDate.now(clock);
        System.out.println(date);
        System.out.println(dateFromClock);
        // Get the local date and local time
        final LocalTime time = LocalTime.now();
        final LocalTime timeFromClock = LocalTime.now(clock);
        System.out.println(time);
        System.out.println(timeFromClock);

        /**
         * LocaleDateTime把LocaleDate与LocaleTime的功能合并起来，
         * 它持有的是ISO-8601格式无时区信息的日期与时间
         */
        // Get the local date/time
        final LocalDateTime datetime = LocalDateTime.now();
        final LocalDateTime datetimeFromClock = LocalDateTime.now(clock);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM dd yyyy HH:mm:ss");
        System.out.println(datetime);
        System.out.println("format: " + datetimeFromClock.format(format));

        /**
         * Duration类：在秒与纳秒级别上的一段时间。Duration使计算两个日期间的不同变的十分简单
         */
        // Get duration between two dates
        final LocalDateTime from = LocalDateTime.of(2014, Month.APRIL, 16, 0, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2015, Month.APRIL, 16, 23, 59, 59);
        final Duration duration = Duration.between(from, to);
        System.out.println("Duration in days: " + duration.toDays());
        System.out.println("Duration in hours: " + duration.toHours());


    }

}
