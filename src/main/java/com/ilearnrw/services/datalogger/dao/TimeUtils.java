package com.ilearnrw.services.datalogger.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public static String minIfNull(String time) {
		if (time == null) {
			time = (new SimpleDateFormat("yyyy-mm-dd mm:ss")).format(new Date(0L));
		}
		return time;
	}

	public static String maxIfNull(String time) {
		if (time == null) {
			time = (new SimpleDateFormat("yyyy-MM-dd mm:ss")).format(new Date(Long.MAX_VALUE));
		}
		return time;
	}
}
