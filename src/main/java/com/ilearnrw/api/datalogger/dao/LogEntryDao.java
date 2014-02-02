package com.ilearnrw.api.datalogger.dao;

import java.util.List;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.LogEntryFilter;
import com.ilearnrw.api.datalogger.model.LogEntryResult;

public interface LogEntryDao {

	public LogEntry getLogEntry(int id);

	public int insertData(LogEntry entry);

	public List<String> getUsers();

	public LogEntryResult getLogs(LogEntryFilter filter);
	
}
