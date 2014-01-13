package com.ilearnrw.services.datalogger.dao;

import java.util.List;

import com.ilearnrw.services.datalogger.model.LogEntry;
import com.ilearnrw.services.datalogger.model.LogEntryFilter;
import com.ilearnrw.services.datalogger.model.LogEntryResult;

public interface LogEntryDao {

	public LogEntry getLogEntry(int id);

	public int insertData(LogEntry entry);

	public List<String> getUsers();

	public LogEntryResult getLogs(LogEntryFilter filter);
	
}
