package com.ilearnrw.api.datalogger.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SystemTags {
	public static final String LEARN_SESSION_START 		= "LEARN_SESSION_START";
	public static final String LEARN_SESSION_END 		= "LEARN_SESSION_END";
	public static final String APP_SESSION_START 		= "APP_SESSION_START";
	public static final String APP_SESSION_END 			= "APP_SESSION_END";
	public static final String APP_ROUND_SESSION_START 	= "APP_ROUND_SESSION_START";
	public static final String APP_ROUND_SESSION_END 	= "APP_ROUND_SESSION_END";
	public static final String ACTIVITY_PROPOSED 	= "ACTIVITY_PROPOSED";
	
	public static final String APP_USAGE_TIME	= "APP_USAGE_TIME";
	public static final String SETTINGS_UPDATED = "SETTINGS_UPDATED";
	public static final String APP_POINTER = "APP_POINTER";
	
	public static final String APP_READ_SESSION_START = "APP_READ_SESSION_START";
	public static final String APP_READ_SESSION_END = "APP_READ_SESSION_END";
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Fact {

	}

	@Fact
	public static final String WORD_SELECTED = "WORD_SELECTED";
	@Fact
	public static final String WORD_DISPLAYED = "WORD_DISPLAYED";
	@Fact
	public static final String WORD_SUCCESS = "WORD_SUCCESS";
	@Fact
	public static final String WORD_FAILED = "WORD_FAILED";
	@Fact
	public static final String WORD_NOT_ANSWERED = "WORD_NOT_ANSWERED";
	@Fact
	public static final String WORD_NOT_SEEN = "WORD_NOT_SEEN";
	@Fact
	public static final String PROFILE_UPDATE = "PROFILE_UPDATE";
	@Fact
	public static final String USER_SEVERITIES_SET= "USER_SEVERITIES_SET";
	@Fact
	public static final String LOGIN = "LOGIN";
	@Fact
	public static final String LOGOUT = "LOGOUT";
	
	private static List<Field> systemFields = null;

	public static boolean isFact(String tag) {
		Field field = getField(tag);
		if (field == null)
		{
			return false;
		}
		return (field.getAnnotation(Fact.class) != null);
	}

	public static boolean isSystemTag(String tag) {
		return (getField(tag) != null);
	}

	public static Field getField(String tag) {
		List<Field> systemTags = getSystemTags();
		for (Field f : systemTags) {
			try {
				String fieldValue = (String) f.get(null);
				if (fieldValue.compareTo(tag) == 0) {
					return f;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
		
	}
	
	private static List<Field> getSystemTags() {
		if (systemFields == null) {
			Field[] fields = SystemTags.class.getDeclaredFields();
			systemFields = new ArrayList<Field>();
			for (Field f : fields) {
				systemFields.add(f);
			}
		}
		return systemFields;
	}

}
