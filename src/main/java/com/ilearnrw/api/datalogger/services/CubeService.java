package com.ilearnrw.api.datalogger.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.api.datalogger.model.ListWithCount;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.Problem;
import com.ilearnrw.api.datalogger.model.Session;
import com.ilearnrw.api.datalogger.model.WordCount;
import com.ilearnrw.api.datalogger.model.WordSuccessCount;

public interface CubeService {

	boolean handle(LogEntry entry);

	ListWithCount<Session> getUserSessionsByType(String username, String type, String timestart, String timeend, boolean count);

	Session getSessionById(int id);

	ListWithCount<WordCount> getWordsForUser(String username, String status, String timestart,
			String timeend, boolean count);

	ListWithCount<WordCount> getAllWords(String status, String timestart,
			String timeend, boolean count);

	String getUsername(int userId);
	
	ListWithCount<Problem> getProblems(String username, String timestart,
			String timeend, boolean count);

	ListWithCount<WordCount> getWordsByProblem(String username, int category,
			int index, String timestart, String timeend, boolean count);

	ListWithCount<WordSuccessCount> getWordsByProblemAndSessions(String username, int category,
			int index, String timestart, String timeend, int numberOfSessions, boolean count);

	ListWithCount<Problem> getProblemsByGenderAndAge(String gender, int age,
			String timestart, String timeend, boolean count);

	ListWithCount<WordCount> getAllWordsByAgeAndGender(String gender, int age,
			String status, String timestart, String timeend, boolean count);

	List<Map<String, Object>> getFactById(int id);

	ListWithCount<Map<String, Object>> getFactsForApplication(int id, String timestart, String timeend, boolean count);

	ListWithCount<Map<String, Object>> getWordsForApplication(int id,
			String status, String timestart, String timeend, boolean count);

}
