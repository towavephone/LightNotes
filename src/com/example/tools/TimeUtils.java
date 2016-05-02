package com.example.tools;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.R.integer;
import android.text.format.DateFormat;
import android.util.Log;

public class TimeUtils {

    /**
     * 日期转换成Java字符串
     * 
     * @param date
     * @return str
     */
    public static String DateToStr(Date date, String format) {

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	String str = simpleDateFormat.format(date);
	return str;
    }

    /**
     * 字符串转换成日期
     * 
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

	SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
	Date date = null;
	try {
	    date = format.parse(str);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return date;
    }

    public static ArrayList<HashMap<String, Object>> compareTimeSort(
	    ArrayList<HashMap<String, Object>> infoMapArr) {
	Collections.sort(infoMapArr, new Comparator<Map<String, Object>>() {
	    @Override
	    public int compare(Map<String, Object> arg0,
		    Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		String date = arg0.get("CreateTime").toString();
		String date2 = arg1.get("CreateTime").toString();
		L.e(compareTime(date, date2)+"");
		return (int) compareTime(date, date2);
	    }
	});
	return infoMapArr;
    }

    public static long compareTime(String date, String date2) {
//	Calendar target = Calendar.getInstance();
//	target.setTime(StrToDate(date));
//	target.set(Calendar.HOUR, 0);
//	target.set(Calendar.MINUTE, 0);
//	target.set(Calendar.SECOND, 0);
//
//	Calendar target2 = Calendar.getInstance();
//	target2.setTime(StrToDate(date2));
//	target2.set(Calendar.HOUR, 0);
//	target2.set(Calendar.MINUTE, 0);
//	target2.set(Calendar.SECOND, 0);
	return StrToDate(date2).getTime()-StrToDate(date).getTime();

    }

    /**
     * 将日期信息转换成今天、明天、后天、星期
     * 
     * @param date
     * @return
     */
    // public static String getDateDetail(String date) {
    // Calendar target = Calendar.getInstance();
    // Date target_date = StrToDate(date);
    // target.setTime(target_date);
    // target.set(Calendar.HOUR, 0);
    // target.set(Calendar.MINUTE, 0);
    // target.set(Calendar.SECOND, 0);
    // target.add(Calendar.DATE, +1);
    // long intervalMilli = target.getTimeInMillis()
    // - System.currentTimeMillis();
    // int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
    // String dateDetail = showDateDetail(xcts, target);
    // String showtime = "";
    // if (xcts == 0 || xcts == 1 || xcts == 2 || xcts == -1 || xcts == -2) {
    // showtime = dateDetail + " "
    // + DateToStr(target_date, Constants.TIME_FORMAT);
    // } else {
    // showtime = DateToStr(target_date, Constants.MONTH_DAY_FORMAT) + " "
    // + dateDetail;
    // }
    // return showtime;
    // }

    /**
     * 将日期差显示为日期或者星期
     * 
     * @param xcts
     * @param target
     * @return
     */
    // private static String showDateDetail(int xcts, Calendar target) {
    // switch (xcts) {
    // case 0:
    // return Constants.TODAY;
    // case 1:
    // return Constants.TOMORROW;
    // case 2:
    // return Constants.AFTER_TOMORROW;
    // case -1:
    // return Constants.YESTERDAY;
    // case -2:
    // return Constants.BEFORE_YESTERDAY;
    // default:
    // int dayForWeek = 0;
    // dayForWeek = target.get(Calendar.DAY_OF_WEEK);
    // switch (dayForWeek) {
    // case 1:
    // return Constants.SUNDAY;
    // case 2:
    // return Constants.MONDAY;
    // case 3:
    // return Constants.TUESDAY;
    // case 4:
    // return Constants.WEDNESDAY;
    // case 5:
    // return Constants.THURSDAY;
    // case 6:
    // return Constants.FRIDAY;
    // case 7:
    // return Constants.SATURDAY;
    // default:
    // return null;
    // }
    //
    // }
    // }
    public static String getTimestampString(Date paramDate) {
	String str = null;
	long l = paramDate.getTime();
	Calendar localCalendar = GregorianCalendar.getInstance();
	localCalendar.setTime(paramDate);
	int year = localCalendar.get(Calendar.YEAR);
	if (!isSameYear(year)) { // 去年，直接返回
	    String paramDate2str = new SimpleDateFormat(Constants.DATE_FORMAT,
		    Locale.CHINA).format(paramDate);
	    return paramDate2str;
	}

	if (isSameDay(l)) {
	    int i = localCalendar.get(Calendar.HOUR_OF_DAY);
	    if (i > 17) {
		str = "晚上 HH:mm:ss";// HH表示24小时,hh表示12小时进制，
	    } else if ((i >= 0) && (i <= 6)) {
		str = "凌晨 HH:mm:ss";
	    } else if ((i > 11) && (i <= 17)) {
		str = "下午 HH:mm:ss";
	    } else {
		str = "上午 HH:mm:ss";
	    }
	} else if (isYesterday(l)) {
	    str = "昨天 HH:mm";
	} else if (isBeforeYesterday(l)) {
	    str = "前天 HH:mm";
	} else {
	    str = "MM月dd日 HH:mm";
	}
	String paramDate2str = new SimpleDateFormat(str, Locale.CHINA)
		.format(paramDate);
	return paramDate2str;
    }

    public static class TimeInfo {
	private long startTime;
	private long endTime;

	public long getStartTime() {
	    return this.startTime;
	}

	public void setStartTime(long paramLong) {
	    this.startTime = paramLong;
	}

	public long getEndTime() {
	    return this.endTime;
	}

	public void setEndTime(long paramLong) {
	    this.endTime = paramLong;
	}
    }

    // 获取 今天开始结束 时间

    public static TimeInfo getTodayStartAndEndTime() {

	Calendar localCalendar1 = Calendar.getInstance();
	localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
	localCalendar1.set(Calendar.MINUTE, 0);
	localCalendar1.set(Calendar.SECOND, 0);
	localCalendar1.set(Calendar.MILLISECOND, 0);
	Date localDate1 = localCalendar1.getTime();
	long l1 = localDate1.getTime();

	Calendar localCalendar2 = Calendar.getInstance();
	localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
	localCalendar2.set(Calendar.MINUTE, 59);
	localCalendar2.set(Calendar.SECOND, 59);
	localCalendar2.set(Calendar.MILLISECOND, 999);
	Date localDate2 = localCalendar2.getTime();
	long l2 = localDate2.getTime();
	TimeInfo localTimeInfo = new TimeInfo();
	localTimeInfo.setStartTime(l1);
	localTimeInfo.setEndTime(l2);
	return localTimeInfo;
    }

    // 获取 昨天开始结束 时间
    public static TimeInfo getYesterdayStartAndEndTime() {
	Calendar localCalendar1 = Calendar.getInstance();
	localCalendar1.add(Calendar.DAY_OF_MONTH, -1);// 5
	localCalendar1.set(Calendar.HOUR_OF_DAY, 0);// 11
	localCalendar1.set(Calendar.MINUTE, 0);// 12
	localCalendar1.set(Calendar.SECOND, 0);// 13
	localCalendar1.set(Calendar.MILLISECOND, 0);// Calendar.MILLISECOND
	Date localDate1 = localCalendar1.getTime();
	long l1 = localDate1.getTime();

	Calendar localCalendar2 = Calendar.getInstance();
	localCalendar2.add(Calendar.DAY_OF_MONTH, -1);// 5
	localCalendar2.set(Calendar.HOUR_OF_DAY, 23);// 11
	localCalendar2.set(Calendar.MINUTE, 59);// 12
	localCalendar2.set(Calendar.SECOND, 59);// 13
	localCalendar2.set(Calendar.MILLISECOND, 999);// Calendar.MILLISECOND
	Date localDate2 = localCalendar2.getTime();
	long l2 = localDate2.getTime();

	TimeInfo localTimeInfo = new TimeInfo();
	localTimeInfo.setStartTime(l1);
	localTimeInfo.setEndTime(l2);
	return localTimeInfo;
    }

    // 获取 前天开始结束 时间
    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
	Calendar localCalendar1 = Calendar.getInstance();
	localCalendar1.add(Calendar.DAY_OF_MONTH, -2);
	localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
	localCalendar1.set(Calendar.MINUTE, 0);
	localCalendar1.set(Calendar.SECOND, 0);
	localCalendar1.set(Calendar.MILLISECOND, 0);
	Date localDate1 = localCalendar1.getTime();
	long l1 = localDate1.getTime();

	Calendar localCalendar2 = Calendar.getInstance();
	localCalendar2.add(Calendar.DAY_OF_MONTH, -2);
	localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
	localCalendar2.set(Calendar.MINUTE, 59);
	localCalendar2.set(Calendar.SECOND, 59);
	localCalendar2.set(Calendar.MILLISECOND, 999);
	Date localDate2 = localCalendar2.getTime();
	long l2 = localDate2.getTime();
	TimeInfo localTimeInfo = new TimeInfo();
	localTimeInfo.setStartTime(l1);
	localTimeInfo.setEndTime(l2);
	return localTimeInfo;
    }

    private static boolean isSameDay(long paramLong) {
	TimeInfo localTimeInfo = getTodayStartAndEndTime();
	return (paramLong > localTimeInfo.getStartTime())
		&& (paramLong < localTimeInfo.getEndTime());
    }

    private static boolean isYesterday(long paramLong) {
	TimeInfo localTimeInfo = getYesterdayStartAndEndTime();
	return (paramLong > localTimeInfo.getStartTime())
		&& (paramLong < localTimeInfo.getEndTime());
    }

    private static boolean isBeforeYesterday(long paramLong) {
	TimeInfo localTimeInfo = getBeforeYesterdayStartAndEndTime();
	return (paramLong > localTimeInfo.getStartTime())
		&& (paramLong < localTimeInfo.getEndTime());
    }

    public static boolean isSameYear(int year) {
	Calendar cal = Calendar.getInstance();
	int CurYear = cal.get(Calendar.YEAR);
	// Log.e("","CurYear="+CurYear);//2015
	return CurYear == year;
    }

}
