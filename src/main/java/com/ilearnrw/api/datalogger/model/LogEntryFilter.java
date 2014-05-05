package com.ilearnrw.api.datalogger.model;


public class LogEntryFilter {
	public String username;
	public String timestart;
	public String timeend;
	public Integer page;
	public String tag;
	public String applicationId;
	
	public LogEntryFilter(String username, String timestart, String timeend, 
			Integer page, String tag, String applicationId)
	{
		this.username = username;
		this.timestart = timestart;
		this.timeend = timeend;
		this.page = page;
		this.tag = tag;
		this.applicationId = applicationId;
	}
}
