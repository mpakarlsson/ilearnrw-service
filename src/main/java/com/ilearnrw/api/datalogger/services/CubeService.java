package com.ilearnrw.api.datalogger.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;
import java.util.Map;

import com.ilearnrw.api.datalogger.model.ListWithCount;
import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.Problem;
import com.ilearnrw.api.datalogger.model.Session;
import com.ilearnrw.api.datalogger.model.WordCount;
import com.ilearnrw.api.datalogger.model.WordSuccessCount;
import com.ilearnrw.api.datalogger.model.filters.DateFilter;
import com.ilearnrw.api.datalogger.model.filters.StudentFilter;
import com.ilearnrw.api.datalogger.model.result.BreakdownResult;
import com.ilearnrw.api.datalogger.model.result.GamesComparisonResult;
import com.ilearnrw.api.datalogger.model.result.OverviewBreakdownResult;
import com.ilearnrw.api.datalogger.model.result.ReaderComparisonResult;

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
	
	BreakdownResult getSkillBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter, int category, int language);
	
	BreakdownResult getActivityBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter, String activityName);
	
	OverviewBreakdownResult getOverviewBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter);

	List<GamesComparisonResult> getGamesComparisonResult(DateFilter dateFilter,
			StudentFilter studentFilter);

	List<ReaderComparisonResult> getReaderComparisonResult(
			DateFilter dateFilter, StudentFilter studentFilter);

}
