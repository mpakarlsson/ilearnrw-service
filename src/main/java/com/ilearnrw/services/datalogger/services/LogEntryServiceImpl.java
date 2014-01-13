package com.ilearnrw.services.datalogger.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ilearnrw.services.datalogger.dao.LogEntryDao;
import com.ilearnrw.services.datalogger.model.LogEntry;
import com.ilearnrw.services.datalogger.model.LogEntryFilter;
import com.ilearnrw.services.datalogger.model.LogEntryResult;

@Component
public class LogEntryServiceImpl implements LogEntryService {
	@Autowired
	LogEntryDao logEntryDao;

	@Override
	public LogEntry getLogEntry(int id) {
		return logEntryDao.getLogEntry(id);
	}

	@Override
	public int insertData(LogEntry entry) {
		return logEntryDao.insertData(entry);
	}

	@Override
	public List<String> getUsers() {
		return logEntryDao.getUsers();
	}

	@Override
	public LogEntryResult getLogs(LogEntryFilter filter) {

		return logEntryDao.getLogs(filter);

	}

}
