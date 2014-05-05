package com.ilearnrw.api.datalogger.model;


public class LogEntryFilter {
	public Integer userId;
	public String timestart;
	public String timeend;
	public Integer page;
	public String tag;
	public String applicationId;
	
	public LogEntryFilter(Integer userId, String timestart, String timeend, Integer page, String tag, String applicationId)
	{
		this.userId = userId;
		this.timestart = timestart;
		this.timeend = timeend;
		this.page = page;
		this.tag = tag;
		this.applicationId = applicationId;
	}
}
