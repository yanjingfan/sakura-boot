package com.sakura.common.minio.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @auther yangfan
 * @date 2021/6/21
 * @describle
 */
public class BucketNameConfig {

    /**minion文档目录分割符**/
    public static final String FILE_SPLIT_PATH = "/";
    /**时间格式化格式**/
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 获得当前时间：年（格式：yyyy）
     * @return
     */
    public static int getYear(){
        return LocalDate.now().getYear();
    }

    /**
     * 获得当前时间：月（格式：MM）
     * @return
     */
    public static String getMonth(){
        int month = LocalDate.now().getMonth().getValue();
        if(month < 10){
            return "0"+month;
        }
        return Integer.valueOf(month).toString();
    }

    /**
     * 获得当前时间：日（格式：dd）
     * @return
     */
    public static String getDay(){
        int day = LocalDate.now().getDayOfMonth();
        if(day < 10){
            return "0"+day;
        }
        return Integer.valueOf(day).toString();
    }

    /**
     * 获得当前时间（格式：yyyyMMdd）
     * @return
     */
    public static String getFullDay(){
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 获得当前时间月和日（格式：MMdd）
     * @return
     */
    public static String getMonthAndDay(){
        return getMonth()+getDay();
    }
}
