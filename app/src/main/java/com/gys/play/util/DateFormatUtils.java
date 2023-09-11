package com.gys.play.util;

import com.gys.play.MyApplication;
import com.gys.play.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateFormatUtils {

    private static final String DATA_FORMAT_PATTERN_MD_HM_ENGLISH = "MMM d, h:m:s aa";
    private static final String DATA_FORMAT_PATTERN_MD_HM = "MM-dd ahh:mm:ss";
    private static final String DATA_FORMAT_PATTERN_Y = "yyyy";
    private static final String DATE_FORMAT_PATTERN_YM = "yyyy-MM";
    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    private static final String DATE_FORMAT_PATTERN_MD_HM = "MM月dd日 HH:mm";
    private static final String DATE_FORMAT_PATTERN_MD_HM_EN = "MM-dd HH:mm";


    public static String long2Str(long timestamp, boolean isPreciseTime) {
        return long2Str(timestamp, getFormatPattern(isPreciseTime));
    }

    public static String long2Str(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
    }

    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    public static String long2StrMDHM(long timestamp) {
        if ("3".equals(MyApplication.getInstance().getActivityResources().getString(R.string.lang_type))) {
            return long2Str(timestamp, getFormatPatternMDHM());
        }
        return long2StrEnglish(timestamp, DATA_FORMAT_PATTERN_MD_HM_ENGLISH);
    }

    private static String long2StrEnglish(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.ENGLISH).format(new Date(timestamp));
    }

    public static String long2StrMDHM1(long timestamp) {
        String pattern = DATE_FORMAT_PATTERN_MD_HM;
        if ("3".equals(MyApplication.getInstance().getActivityResources().getString(R.string.lang_type))) {
            pattern = DATE_FORMAT_PATTERN_MD_HM;
        } else {
            pattern = DATE_FORMAT_PATTERN_MD_HM_EN;
        }
        return timeStamp2Date(timestamp, pattern);
    }


    public static String long2Str(long timestamp) {
        return long2Str(timestamp, getFormatPatternOnlyYear());
    }


    public static long str2Long(String dateStr) {
        return str2Long(dateStr, getFormatPatternOnlyYear());
    }


    public static long str2Long(String dateStr, boolean isPreciseTime) {
        return str2Long(dateStr, getFormatPattern(isPreciseTime));
    }

    private static long str2Long(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static String getFormatPattern(boolean showSpecificTime) {
        if (showSpecificTime) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else {
            return DATE_FORMAT_PATTERN_YMD;
        }
    }

    private static String getFormatPatternOnlyYear() {
        return DATA_FORMAT_PATTERN_Y;
    }

    private static String getFormatPatternMDHM() {
        return DATA_FORMAT_PATTERN_MD_HM;
    }
}
