package cn.pospal.www.util;

import android.text.TextUtils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.posbase.R;

/**
 * 日期时间相关函数
 * 
 * @author Near Chan 2012.12.26
 */
public class DatetimeUtil {
	public static final String DEFAULT_DATETIME = "1970-01-01 00:00:00";
	public static final String DEFAULT_SYNC_DATETIME = "3099-01-01 00:00:00";

	/**
	 * 获取系统日期时间
	 * 
	 * @return 日期时间
	 */
	public static Date getDateTime() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		return curDate;
	}

	/**
	 * 获取系统日期时间字符串
	 * 
	 * @return 日期时间字符串
	 */
	public static String getDateTimeStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

	/**
	 * 获取系统日期字符串
	 * 
	 * @return 日期字符串
	 */
	public static String getDateStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

	public static String getTimeStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

	/**
	 * 获取收据系统日期时间字符串 用于收据打印
	 * 
	 * @return 日期时间字符串
	 */
	public static String getReceiptDateTimeStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

	/**
	 * 获取收据系统日期时间字符串 用于收据打印
	 *
	 * @return 日期时间字符串
	 */
	public static String getLakalaDateTimeStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

	/**
	 * 获取通联支付
	 *
	 * @return 日期时间字符串
	 */
	public static String getAllinpayDateStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMDD");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

    public static final String TYPE_SHORT = "SHORT";
    public static final String TYPE_MEDIUM = "MEDIUM";
    public static final String TYPE_FULL = "FULL";
    public static final String TYPE_DOT = "DOT";
	/**
	 * date对象展示为字符串
	 * 
	 * @param date
	 *            输入的时间
	 * @param type
	 *            转化的类型
	 * @return
	 */
	public static String dateToString(Date date, String type) {
		String str = null;
		// 默认方式
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (type.equals(TYPE_SHORT)) {
			// 07-1-18
			format = DateFormat.getDateInstance(DateFormat.SHORT);
			str = format.format(date);
		} else if (type.equals(TYPE_MEDIUM)) {
			// 2007-1-18
			format = DateFormat.getDateInstance(DateFormat.MEDIUM);
			str = format.format(date);
		} else if (type.equals(TYPE_FULL)) {
			// 2007年1月18日 星期四
			format = DateFormat.getDateInstance(DateFormat.FULL);
			str = format.format(date);
		} else if (type.equals(TYPE_DOT)) {
			// 2007年1月18日 星期四
			format = new SimpleDateFormat("yyyy.MM.dd");
			str = format.format(date);
		} else {
			str = format.format(date);
		}

		return str;
	}

	/**
	 * date对象展示为字符串 重载方法，默认方法转化
	 * 
	 * @param date
	 *            输入的时间
	 * @return
	 */
	public static String dateToString(Date date) {

		return dateToString(date, "");
	}

	/**
	 * 将字符串转化为Date对象
	 * 
	 * @param str
	 *            输入的时间数据
	 * @return
	 */
	public static Date stringToDate(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 将字符串转化为Time对象
	 *
	 * @param str
	 *            输入的时间数据
	 * @return
	 */
	public static Time stringToTime(String str) {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new Time(date.getTime());
	}

	public static final String TIME_TYPE_FULL = "HH:mm:ss";
	public static final String TIME_TYPE_MEDIUM = "HH:mm";
	/**
	 * 将字符串转化为Time对象
	 *
	 *            输入的时间数据
	 * @return
	 */
	public static String timeToString(Time time, String timeType) {
		DateFormat format = new SimpleDateFormat(timeType);

		return format.format(time);
	}

	/**
	 * 将字符串转化为毫秒
	 * 
	 * @param str
	 *            输入的时间数据
	 * @return
	 */
	public static long stringToSecond(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date.getTime() / 1000;
	}

	/**
	 * 将毫秒转化为Datetime字符串
	 * 
	 *            输入的时间数据
	 * @return
	 */
	public static String secondToDatetimeStr(long second) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date tarDate = new Date(second * 1000);// 获取当前时间
		String str = format.format(tarDate);

		return str;
	}

	/**
	 * 获取系统日期字符串
	 * 
	 * @return 日期字符串
	 */
	public static String getDateNearStr(int days) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		Date curDate = new Date(calendar.getTimeInMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}
	public static String getDatetimeNearStr(int days) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		Date curDate = new Date(calendar.getTimeInMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}
	
	public static String Date2Datetime(String dateStr) {
		if(dateStr.contains(":")) {
			return dateStr;
		}
		String datetimeStr = dateStr + " 00:00:00";
		return datetimeStr;
	}
	
	public static String str2EasyStr(String datetimeStr) {
		Date date = stringToDate(datetimeStr);
		DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
		return format.format(date);
	}

	/**
	 * 计算过去的时间，按分钟
	 * @param datetimeStr
	 * @return
	 */
	public static int getPassTimeMinutes(String datetimeStr) {
		if (StringUtil.isNullOrEmpty(datetimeStr)) {
			return 0;
		}

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date last = format.parse(datetimeStr);
			long time = last.getTime();
			int minites = (int) ((System.currentTimeMillis() - time) / 1000 / 60);

			return minites;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return  0;
	}



	/**
	 *字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate,String bdate) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * @param dataStr "2014-09-01 19:47";
	 */
	public static String getTimeString(String dataStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.CHINA);
		Date date;
		try {
			date = sdf.parse(dataStr);
			return DatetimeUtil.getTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
	public static String getTime(Date date) {
		boolean sameYear = false;
		String todySDF = "HH:mm";
		String yesterDaySDF = "昨天";
		String beforYesterDaySDF = "前天";
		String otherSDF = "MM-dd";
		String otherYearSDF = "yyyy-MM-dd";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Date now = new Date();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(now);
		todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayCalendar.set(Calendar.MINUTE, 0);

		if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
			sameYear = true;
		} else {
			sameYear = false;
		}

		if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
			sfd = new SimpleDateFormat(todySDF);
			time = sfd.format(date);
			return time;
		} else {
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
				// sfd = new SimpleDateFormat(yesterDaySDF);
				// time = sfd.format(date);
				time = yesterDaySDF;
				return time;
			}
			todayCalendar.add(Calendar.DATE, -1);
			if (dateCalendar.after(todayCalendar)) {// 判断是不是前天
				// sfd = new SimpleDateFormat(beforYesterDaySDF);
				// time = sfd.format(date);
				time = beforYesterDaySDF;
				return time;
			}
		}

		if (sameYear) {
			sfd = new SimpleDateFormat(otherSDF);
			time = sfd.format(date);
		} else {
			sfd = new SimpleDateFormat(otherYearSDF);
			time = sfd.format(date);
		}

		return time;
	}

    /**
     * @param dataStr "2014-09-01 19:47";
     */
    public static String getListTimeString(String dataStr) {
        String dateStr = dataStr.split(" ")[0];
        // yyyy-MM-dd HH:mm:ss
        if (dateStr.compareTo(getDateStr()) != 0) {
            return dataStr.substring(5, 16);
        } else {
            return dataStr.substring(11, 16);
        }
    }

    /**
     */
    public static String getListTimeString(Date date) {
        return getListTimeString(dateToString(date));
    }


	/**
	 *系统时间与服务器时间相差超过12小时则返回true
	 */
	public static boolean expirationTime(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long subMill = System.currentTimeMillis() - date.getTime();
		if (subMill < 0){
			subMill = -subMill;
		}
		Long s = subMill / (1000 * 60 * 60);
		if (s <= 12){
			return false;
		}else {
			 return true;
		}
	}


    /**
     * 联动需要
     * @return
     */
    public static String getUmpDateTimeStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);

        return str;
    }

    /**
     * 根据出生日期 计算年纪
     * @param birthDay  yyyy-MM-dd
     * @return
     */
    public static String getBirthAge(String birthDay) {
        if (TextUtils.isEmpty(birthDay)) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthDate = formatter.parse(birthDay);
            Calendar cal = Calendar.getInstance();
            Calendar calBirth = Calendar.getInstance();
            calBirth.setTime(birthDate);
            if (cal.after(calBirth)) {
                int yearNow = cal.get(Calendar.YEAR);
                int monthNow = cal.get(Calendar.MONTH);
                int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
                cal.setTime(birthDate);
                int yearBirth = cal.get(Calendar.YEAR);
                int monthBirth = cal.get(Calendar.MONTH);
                int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
                int year = yearNow - yearBirth;
                int month = 0;
                int day = 0;
                if (monthNow <= monthBirth) {
                    if (monthNow == monthBirth) {
                        month = 0;
                        if (dayOfMonthNow < dayOfMonthBirth) {
                            year--;
                        }
                    } else {
                        month = 11 - monthBirth + monthNow + 1;
                        year--;
                    }
                } else {
                    month = monthNow - monthBirth;
                }
                if (year == 0 && month == 0) {
                    day = dayOfMonthNow - dayOfMonthBirth;
                    return day + ManagerApp.getInstance().getString(R.string.customer_pet_day);
                } else {
                    if (year == 0) {
                        return month + ManagerApp.getInstance().getString(R.string.customer_pet_month);
                    } else {
                        if (month ==0) {
                            return year + ManagerApp.getInstance().getString(R.string.customer_pet_year);
                        } else  {
                            return year + ManagerApp.getInstance().getString(R.string.customer_pet_year) + month + ManagerApp.getInstance().getString(R.string.customer_pet_month);
                        }
                    }
                }
            }

            return "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
