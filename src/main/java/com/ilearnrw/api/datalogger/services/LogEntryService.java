package com.ilearnrw.api.datalogger.services;

import java.util.List;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.LogEntryFilter;
import com.ilearnrw.api.datalogger.model.LogEntryResult;

public interface LogEntryService {
	public LogEntry getLogEntry(int id);

	public int insertData(LogEntry entry);

	public List<String> getUsers();

	public LogEntryResult getLogs(LogEntryFilter filter);

	public LogEntryResult getLastLogs(LogEntryFilter filter);
	
	
}
