package com.ilearnrw.api.datalogger.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.user.problems.Problems;
import ilearnrw.utils.LanguageCode;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.api.datalogger.dao.CubeDao;
import com.ilearnrw.api.datalogger.dao.TimeUtils;
import com.ilearnrw.api.datalogger.model.ListWithCount;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.Problem;
import com.ilearnrw.api.datalogger.model.Session;
import com.ilearnrw.api.datalogger.model.SessionType;
import com.ilearnrw.api.datalogger.model.SystemTags;
import com.ilearnrw.api.datalogger.model.User;
import com.ilearnrw.api.datalogger.model.WordCount;
import com.ilearnrw.api.datalogger.model.WordSuccessCount;
import com.ilearnrw.api.datalogger.model.filters.DateFilter;
import com.ilearnrw.api.datalogger.model.filters.StudentFilter;
import com.ilearnrw.api.datalogger.model.result.BreakdownResult;
import com.ilearnrw.api.datalogger.model.result.GamesComparisonResult;
import com.ilearnrw.api.datalogger.model.result.OverviewBreakdownResult;
import com.ilearnrw.api.datalogger.model.result.ReaderComparisonResult;
import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.services.InfoService;
import com.ilearnrw.common.AuthenticatedRestClient;
import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;

@Service
public class CubeServiceImpl implements CubeService {

	private static Logger LOG = Logger.getLogger(CubeServiceImpl.class);

	@Autowired
	CubeDao cubeDao;

	@Autowired
	AuthenticatedRestClient authenticatedRestClient;

	@Autowired
	InfoService infoService;
	
	@Override
	public boolean handle(LogEntry entry) {

		if (!SystemTags.isSystemTag(entry.getTag())) {
			LOG.debug("Unknown tag: " + entry.getTag());
			return false;
		}

		int appId = findOrCreateApplication(entry.getApplicationId());
		
		int userId = findOrCreateUser(entry.getUsername());
		
		int problemId = findOrCreateProblem(entry.getProblemCategory(),
				entry.getProblemIndex(), entry.getUsername());

		int learnSessionId = getLastSessionIdByType(entry.getUsername(), SessionType.LEARN);
		if (entry.getTag().compareTo(SystemTags.LEARN_SESSION_START) == 0) {
			learnSessionId = createSession(SessionType.LEARN, entry.getValue(),
					entry.getTimestamp(), entry.getUsername(), entry.getSupervisor());
		}
		if (entry.getTag().equals(SystemTags.LEARN_SESSION_END)) {
			endSession(learnSessionId, entry.getTimestamp());
		}

		int appSessionId = getLastSessionIdByType(entry.getUsername(), SessionType.APPLICATION);
		if (entry.getTag().compareTo(SystemTags.APP_SESSION_START) == 0) {
			appSessionId = createSession(SessionType.APPLICATION,
					entry.getValue(), entry.getTimestamp(), entry.getUsername(), entry.getSupervisor());
		}
		if (entry.getTag().equals(SystemTags.APP_SESSION_END)) {
			endSession(appSessionId, entry.getTimestamp());
		}

		int appRoundSessionId = getLastSessionIdByType(entry.getUsername(), SessionType.ROUND);
		if (entry.getTag().compareTo(SystemTags.APP_ROUND_SESSION_START) == 0) {
			appRoundSessionId = createSession(SessionType.ROUND,
					entry.getValue(), entry.getTimestamp(), entry.getUsername(), entry.getSupervisor());
		}
		if (entry.getTag().equals(SystemTags.APP_ROUND_SESSION_END)) {
			endSession(appRoundSessionId, entry.getTimestamp());
		}

		if (isFact(entry)) {
			LOG.debug(String
					.format("Creating fact with appId=%d, userId=%d, problemId=%d, learnSessionId=%d, appSessionId=%d, appRoundSessionId=%d ",
							appId, userId, problemId, learnSessionId,
							appSessionId, appRoundSessionId));

			int factId = createFact(entry, appId, userId, problemId,
					learnSessionId, appSessionId, appRoundSessionId);

			LOG.debug(String.format("Created fact with ID=%d", factId));
		}

		return true;
	}

	private int getLastSessionIdByType(String username, SessionType type) {
		return cubeDao.getLastSessionIdByType(username, type);
	}

	private int createFact(LogEntry entry, int appId, int userId,
			int problemId, int learnSessionId, int appSessionId,
			int appRoundSessionId) {
		String word = entry.getWord();
		String wordStatus = entry.getTag();
		float duration = entry.getDuration();
		Timestamp timestamp = entry.getTimestamp();

		return cubeDao.createFact(timestamp, userId, appId, problemId, word,
				wordStatus, duration, learnSessionId, appSessionId,
				appRoundSessionId);
	}

	private int createSession(SessionType type, String value,
			Timestamp timestamp, String username, String supervisor) {
		return cubeDao.createSession(type, value, timestamp, username, supervisor);
	}

	private void endSession(int sessionId, Timestamp timestamp) {
		cubeDao.endSession(sessionId, timestamp);
	}

	private int findOrCreateProblem(int problemCategory, int problemIndex, String username) {
		com.ilearnrw.common.security.users.model.User user = authenticatedRestClient.getUserDetails(username);
		if (user == null) {
			return -1;
		}
		LanguageCode languageCode = LanguageCode.fromString(user.getLanguage());
		int id = cubeDao.getProblemByCategoryIndexAndLanguage(problemCategory,
				problemIndex, languageCode);
		if (id == -1) {
			ProblemDefinitionIndex problemDefinitionIndex = authenticatedRestClient.getProblemDefinitions(user.getId());
			String description = problemDefinitionIndex
					.getProblemDescription(problemCategory, problemIndex)
					.returnDescriptionsAsString();
			id = cubeDao.createProblem(problemCategory, problemIndex, languageCode.getCode(), description);
		}
		return id;
	}

	private int findOrCreateUser(String username) {
		int id = cubeDao.getUserIdByName(username);
		if (id == -1) {
			User user = getUserDetailsFromRemoteService(username);
			Classroom classroom = authenticatedRestClient.getUserClassroom(username);
			School school = authenticatedRestClient.getUserSchool(username);
			id = cubeDao.createUser(username, user.getGender(), user.getBirthyear(), user.getLanguage(), classroom.getName(), school.getName());
		}
		return id;
	}

	@Cacheable(value = "users", unless = "#result == null")
	private User getUserDetailsFromRemoteService(String username) {
		LOG.debug(String.format("Fetching data for %s from remote service", username));
		com.ilearnrw.common.security.users.model.User user = authenticatedRestClient.getUserDetails(username);
		
		if (user == null) {
			LOG.debug(String.format("User %s not found by remote service!", username));
			return new User(-1, username, " ", 1900, "");
		}
		
		return new User(user.getId(), username, user.getGender(), TimeUtils.getBirthYear(user.getBirthdate()), user.getLanguage());
	}

	private int findOrCreateApplication(String applicationId) {
		int id = cubeDao.getApplicationIdByAppId(applicationId);
		if (id == -1) {
			Application app = infoService.getApplicationByAppId(applicationId);
			if (app != null) {			
				id = cubeDao.createApplication(app.getAppId(), app.getName());
			}
			else {
				// this is for unknown applications
				id = cubeDao.createApplication(applicationId, applicationId);
			}
		}
		return id;
	}

	private boolean isFact(LogEntry entry) {
		return SystemTags.isFact(entry.getTag());
	}

	@Override
	public ListWithCount<Session> getUserSessionsByType(String username, String type, String timestart, String timeend, boolean count) {
		return cubeDao.getUserSessionsByType(username, type, timestart, timeend, count);
	}

	@Override
	public Session getSessionById(int id) {
		return cubeDao.getSessionById(id);
	}

	@Override
	public ListWithCount<WordCount> getWordsForUser(String username, String status,
			String timestart, String timeend, boolean count) {
	
		int userId = cubeDao.getUserIdByName(username);
		return cubeDao.getWordsForUser(userId, status, timestart, timeend, count);
	}
	
	@Override
	public ListWithCount<WordCount> getAllWords(String status,
			String timestart, String timeend, boolean count) {
		return cubeDao.getWordsForUser(CubeDao.NO_USER, status, timestart, timeend, count);
	}

	@Override
	public ListWithCount<WordCount> getAllWordsByAgeAndGender(String gender,
			int age, String status, String timestart, String timeend,
			boolean count) {
		return cubeDao.getWordsByAgeAndGender(gender, age, status, timestart, timeend, count);
	}

	@Override
	public ListWithCount<Problem> getProblems(String username,
			String timestart, String timeend, boolean count) {
		int userId = cubeDao.getUserIdByName(username);
		return cubeDao.getProblems(userId, timestart, timeend, count);
	}
	
	@Override
	public String getUsername(int userId) {
		return cubeDao.getUsername(userId);
	}

	@Override
	public ListWithCount<WordCount> getWordsByProblem(String username,
			int category, int index, String timestart, String timeend,
			boolean count) {
		int userId = cubeDao.getUserIdByName(username);
		return cubeDao.getWordsByProblem(userId, category, index, timestart, timeend, count);
	}

	@Override
	public ListWithCount<WordSuccessCount> getWordsByProblemAndSessions(String username,
			int category, int index, String timestart, String timeend,
			int numberOfSessions, boolean count) {
		int userId = cubeDao.getUserIdByName(username);
		return cubeDao.getWordsByProblemAndSessions(userId, category, index, timestart, timeend, numberOfSessions);
	}

	@Override
	public ListWithCount<Problem> getProblemsByGenderAndAge(String gender,
			int age, String timestart, String timeend, boolean count) {
		return cubeDao.getProblemsByGenderAndAge(gender, age, timestart, timeend, count);
	}

	@Override
	public List<Map<String, Object>> getFactById(int id) {
		return cubeDao.getFactById(id);
	}

	@Override
	public ListWithCount<Map<String, Object>> getFactsForApplication(int id, String timestart, String timeend, boolean count) {
		return cubeDao.getFactsForApplication(id, timestart, timeend, count);
	}

	@Override
	public ListWithCount<Map<String, Object>> getWordsForApplication(int id,
			String status, String timestart, String timeend, boolean count) {
		return cubeDao.getWordsForApplication(id, status, timestart, timeend, count);
	}

	@Override
	public BreakdownResult getSkillBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter, int category, int language) {
		return cubeDao.getSkillBreakdownResult(dateFilter, studentFilter, category, language);
	}

	@Override
	public BreakdownResult getActivityBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter, String activityName) {
		return cubeDao.getActivityBreakdownResult(dateFilter, studentFilter, activityName);
	}

	@Override
	public OverviewBreakdownResult getOverviewBreakdownResult(
			DateFilter dateFilter, StudentFilter studentFilter) {
		return cubeDao.getOverviewBreakdownResult(dateFilter, studentFilter);
	}

	@Override
	public List<GamesComparisonResult> getGamesComparisonResult(
			DateFilter dateFilter, StudentFilter studentFilter) {
		return cubeDao.getGamesComparisonResult(dateFilter, studentFilter);
	}

	@Override
	public List<ReaderComparisonResult> getReaderComparisonResult(
			DateFilter dateFilter, StudentFilter studentFilter) {
		return cubeDao.getReaderComparisonResult(dateFilter, studentFilter);
	}

}
