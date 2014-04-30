package com.ilearnrw.api.datalogger.services;

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
import com.ilearnrw.common.AuthenticatedRestClient;

@Service
public class CubeServiceImpl implements CubeService {

	private static Logger LOG = Logger.getLogger(CubeServiceImpl.class);

	@Autowired
	CubeDao cubeDao;

	@Autowired
	AuthenticatedRestClient authenticatedRestClient;
	
	@Override
	public boolean handle(LogEntry entry) {

		if (!SystemTags.isSystemTag(entry.getTag())) {
			LOG.debug("Unknown tag: " + entry.getTag());
			return false;
		}

		int appId = findOrCreateApplication(entry.getApplicationId());
		
		int userId = findOrCreateUser(entry.getUsername());
		
		int problemId = findOrCreateProblem(entry.getProblemCategory(),
				entry.getProblemIndex());

		int learnSessionId = getLastSessionIdByType(entry.getUsername(), SessionType.LEARN);
		if (entry.getTag().compareTo(SystemTags.LEARN_SESSION_START) == 0) {
			learnSessionId = createSession(SessionType.LEARN, entry.getValue(),
					entry.getTimestamp(), entry.getUsername());
		}

		int appSessionId = getLastSessionIdByType(entry.getUsername(), SessionType.APPLICATION);
		if (entry.getTag().compareTo(SystemTags.APP_SESSION_START) == 0) {
			appSessionId = createSession(SessionType.APPLICATION,
					entry.getValue(), entry.getTimestamp(), entry.getUsername());
		}

		int appRoundSessionId = getLastSessionIdByType(entry.getUsername(), SessionType.ROUND);
		if (entry.getTag().compareTo(SystemTags.APP_ROUND_SESSION_START) == 0) {
			appRoundSessionId = createSession(SessionType.ROUND,
					entry.getValue(), entry.getTimestamp(), entry.getUsername());
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
			Timestamp timestamp, String username) {
		return cubeDao.createSession(type, value, timestamp, username);
	}

	private int findOrCreateProblem(int problemCategory, int problemIndex) {
		int id = cubeDao.getProblemByCategoryAndIndex(problemCategory,
				problemIndex);
		if (id == -1) {
			id = cubeDao.createProblem(problemCategory, problemIndex, "");
		}
		return id;
	}

	private int findOrCreateUser(String username) {
		int id = cubeDao.getUserIdByName(username);
		if (id == -1) {
			User user = getUserDetailsFromRemoteService(username);
			id = cubeDao.createUser(username, user.getGender(), user.getBirthyear(), user.getLanguage());
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
		int id = cubeDao.getApplicationIdByName(applicationId);
		if (id == -1) {
			id = cubeDao.createApplication(applicationId);
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
		return cubeDao.getWordsByProblemAndSessions(userId, category, index, timestart, timeend, numberOfSessions, count);
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

}