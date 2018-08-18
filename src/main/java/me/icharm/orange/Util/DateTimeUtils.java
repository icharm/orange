package me.icharm.orange.Util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/18 15:03
 */
public class DateTimeUtils {

    /**
     * Connector Symbol
     */
    public static final String SLASH = "/";
    public static final String HYPHEN = "-";
    public static final String COLON = ":";
    public static final String BLANK = " ";

    /**
     * Date Format (DF)
     *
     * year    : yyyy
     * month   : MM
     * day     : dd
     */
    public static final String DF_YEAR = "yyyy";
    public static final String DF_MONTH = "MM";
    public static final String DF_DAY = "dd";
    // "yyyy-MM-dd"
    public static final String DF_FULL_NORMAL = DF_YEAR + HYPHEN + DF_MONTH + HYPHEN + DF_DAY;
    // "yyyy/MM/dd"
    public static final String DF_FULL_SLASH = DF_YEAR + SLASH + DF_MONTH + SLASH + DF_DAY;
    public static final String DF_FULL_CN = "yyyy年MM月dd日";
    // "yyyy-MM"
    public static final String DF_HALF_YM_NORMAL = DF_YEAR + HYPHEN + DF_MONTH;
    // "yyyy/MM"
    public static final String DF_HALF_YM_SLASH = DF_YEAR + SLASH + DF_MONTH;
    // "MM-dd"
    public static final String DF_HALF_MD_NORMAL = DF_MONTH + HYPHEN + DF_DAY;
    // "MM/dd"
    public static final String DF_HALF_MD_SLASH = DF_MONTH + SLASH + DF_DAY;


    /**
     * Time Format (TF)
     *
     * hour    : HH
     * minute  : mm
     * second  : ss
     *
     */
    public static final String TF_HOUR = "HH";
    public static final String TF_MINUTE = "mm";
    public static final String TF_SECOND = "ss";
    // "HH:mm:ss"
    public static final String TF_FULL = TF_HOUR + COLON + TF_MINUTE + COLON + TF_SECOND;
    // "HH:mm"
    public static final String TF_HALF_HM = TF_HOUR + COLON + TF_MINUTE;
    // "mm:ss"
    public static final String TF_HALF_MS = TF_MINUTE + COLON + TF_SECOND;


    /**
     * Date Time Format (DTF)
     *
     */
    // "yyyy-MM-dd HH:mm:ss"
    public static final String DTF_FULL_NORMAL = DF_FULL_NORMAL + BLANK + TF_FULL;
    // "yyyy/MM/dd HH:mm:ss"
    public static final String DTF_FULL_SLASH = DF_FULL_SLASH + BLANK + TF_FULL;
    // "yyyy年MM月dd日 HH:mm:ss"
    public static final String DTF_FULL_CN = DF_FULL_CN + BLANK + TF_FULL;


    static final int MINUTE_IN_HOUR = 60;
    static final int MILLIS_IN_MINUTE = 1000;
    static final int SECOND_IN_HOUR = 3600;
    static final int HOUR_IN_DAY = 24;

    /**
     * format date to string
     *
     * @param date Date
     * @param format String
     * @return String
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat ft = new SimpleDateFormat(format);
        return ft.format(date);
    }

    /**
     * parse string date to Date
     *
     * @param date String
     * @param format String
     * @return Date
     * @throws ParseException
     */
    public static Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat(format);
        return ft.parse(date);
    }

    /**
     * get current time stamp for datebase
     *
     * @return Timestamp
     */
    public static Timestamp currentTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }










}
