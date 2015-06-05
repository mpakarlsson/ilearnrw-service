package com.ilearnrw.api.datalogger.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.LogEntryFilter;
import com.ilearnrw.api.datalogger.model.LogEntryResult;

public interface LogEntryDao {

	public LogEntry getLogEntry(int id);

	public int insertData(LogEntry entry);

	public List<String> getUsers();

	public LogEntryResult getLogs(LogEntryFilter filter);
	
	public LogEntryResult getLogs(LogEntryFilter filter,boolean desc, int resultLimit);
	
}
