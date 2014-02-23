package com.example.smstb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static String longToStr(long time){
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date d = new Date(time);
		return format.format(d);
	}
}
