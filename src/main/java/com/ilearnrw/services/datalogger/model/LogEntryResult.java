package com.ilearnrw.services.datalogger.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author David
 * 
 * The LogEntryResult wraps the resulting LogEntries.
 *
 */
public class LogEntryResult {
	
	public LogEntryResult()
	{
	}
	
	public LogEntryResult(Integer _page,
						  Integer _totalAmountOfPages,
						  List<LogEntry> _results,
						  Object _debugInfo)
	{
		page = _page;
		totalPages = _totalAmountOfPages;
		results = _results;
		debugInfo = _debugInfo;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		serverTime = df.format(new Date());
	}

	
	@JsonProperty("page")
	private Integer page;
	
	@JsonProperty("total-pages")
	private Integer totalPages;
	
	@JsonProperty("debug-info")
	private Object debugInfo;
	

	@JsonProperty("results")
	private List<LogEntry> results;
	
	@JsonProperty("server-time")
	private String serverTime;

	public Integer getPage() {
		return page;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public Object getDebugInfo() {
		return debugInfo;
	}

	public List<LogEntry> getResults() {
		return results;
	}

	public String getServerTime() {
		return serverTime;
	}
}
