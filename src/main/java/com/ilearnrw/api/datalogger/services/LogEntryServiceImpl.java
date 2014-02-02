package com.ilearnrw.api.datalogger.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.api.datalogger.dao.LogEntryDao;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.LogEntryFilter;
import com.ilearnrw.api.datalogger.model.LogEntryResult;

@Service
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
