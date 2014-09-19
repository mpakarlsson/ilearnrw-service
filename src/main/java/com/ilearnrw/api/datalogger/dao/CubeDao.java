package com.ilearnrw.api.datalogger.dao;

import ilearnrw.utils.LanguageCode;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ilearnrw.api.datalogger.model.ListWithCount;
import com.ilearnrw.api.datalogger.model.Problem;
import com.ilearnrw.api.datalogger.model.Session;
import com.ilearnrw.api.datalogger.model.SessionType;
import com.ilearnrw.api.datalogger.model.WordCount;
import com.ilearnrw.api.datalogger.model.WordSuccessCount;

public interface CubeDao {

	public static final int NO_USER = -1;
	

	int getApplicationIdByAppId(String applicationId);

	int createApplication(String applicationId, String name);

	int getUserIdByName(String username);

	int createUser(String username, String gender, int birthyear, String language);

	int getProblemByCategoryIndexAndLanguage(int problemCategory, int problemIndex, LanguageCode languageCode);

	int createProblem(int problemCategory, int problemIndex, int languageCode, String description);

	int createFact(Timestamp timestamp, int userId, int appId, int problemId,
			String word, String wordStatus, float duration, int learnSessionId,
			int appSessionId, int appRoundSessionId);

	int getLastSessionIdByType(String username, SessionType type);

	int createSession(SessionType type, String value, Timestamp timestamp, String username, String supervisor);

	ListWithCount<Session> getUserSessionsByType(String username, String type, String timestart, String timeend, boolean count);

	Session getSessionById(int id);
	
	void endSession(int sessionId);

	ListWithCount<WordCount> getWordsForUser(int userId, String status,
			String timestart, String timeend, boolean count);

	String getUsername(int userId);
	
	ListWithCount<Problem> getProblems(int userId, String timestart,
			String timeend, boolean count);

	ListWithCount<WordCount> getWordsByProblem(int userId, int category,
			int index, String timestart, String timeend, boolean count);

	ListWithCount<WordSuccessCount> getWordsByProblemAndSessions(int userId, int category,
			int index, String timestart, String timeend, int numberOfSessions);

	ListWithCount<Problem> getProblemsByGenderAndAge(String gender, int age,
			String timestart, String timeend, boolean count);

	ListWithCount<WordCount> getWordsByAgeAndGender(String gender, int age,
			String status, String timestart, String timeend, boolean count);

	List<Map<String, Object>> getFactById(int id);

	ListWithCount<Map<String, Object>> getFactsForApplication(int id, String timestart, String timeend, boolean count);

	ListWithCount<Map<String, Object>> getWordsForApplication(int id,
			String status, String timestart, String timeend, boolean count);

}
