package com.ilearnrw.api.datalogger.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static String minIfNull(String time) {
		if (time == null) {
			time = (new SimpleDateFormat("yyyy-mm-dd mm:ss")).format(new Date(0L));
		//	time = "0000-00-00 00:00";
		}
		return time;
	}

	public static String maxIfNull(String time) {
		if (time == null) {
			time = (new SimpleDateFormat("yyyy-MM-dd mm:ss")).format(new Date(Long.MAX_VALUE));
		}
		return time;
	}
	
	public static int getBirthYear(Date birthdate) {
		Calendar c = Calendar.getInstance();
		c.setTime(birthdate);
		return c.get(Calendar.YEAR);
	}
}
