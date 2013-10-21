package com.ilearnrw.services.datalogger;

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
	
	public LogEntryResult(Integer _page,
						  List<LogEntry> _results,
						  Object _debugInfo)
	{
		page = _page;
		results = _results;
		debugInfo = _debugInfo;
	}

	
	@JsonProperty("page")
	private Integer page;
	
	@JsonProperty("debug-info")
	private Object debugInfo;
	

	@JsonProperty("results")
	private List<LogEntry> results;
}
