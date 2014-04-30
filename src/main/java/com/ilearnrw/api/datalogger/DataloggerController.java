package com.ilearnrw.api.datalogger;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.model.*;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.datalogger.services.LogEntryService;

/**
 * @author David Johansson
 * 
 *         TODO: Fix result values and HTTP status codes. TODO: Remove/disable
 *         debug information. TODO: Add support for authorization (tokens).
 * 
 *         Database --------
 * 
 *         Database name: datalogs
 * 
 *         The required sql routines can be found in datalogs.sql
 * 
 *         Logs ---------------------------------- id | PK, AUTO_INCREMENT
 *         userId | VarChar(32) tag | VarChar(32) value | VarChar(512)
 *         applicationId | VarChar(32) timestamp | DateTime sessionId |
 *         VarChar(32)
 * 
 */
@Controller
public class DataloggerController {

	private static Logger LOG = Logger.getLogger(DataloggerController.class);

	@Autowired
	LogEntryService logEntryService;

	@Autowired
	CubeService cubeService;

	public List<WordSuccessCount> getListOfSuccessesAndFailures(String userId, int category, int index){
		String username = cubeService.getUsername(Integer.parseInt(userId));
		ListWithCount<WordSuccessCount> l = 
				cubeService.getWordsByProblemAndSessions(username, 
						category, index, null, null, 20, true);
		return l.getList();
	}
	
	/**
	 * Temporary test function which lists all avaliable users in the datalogger
	 * database.
	 * 
	 * @return List of userId's.
	 */
	@RequestMapping(value = "/logs/users", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getUsers() {
		return logEntryService.getUsers();
	}

	/**
	 * Simple ping function to verify the service is running.
	 * 
	 * @return "pong" as a JSON string object.
	 */
	@RequestMapping(value = "/logs/ping", method = RequestMethod.GET)
	public @ResponseBody
	String ping() {
		return "\"pong\"";
	}

	/**
	 * Insert a log entry.
	 * 
	 * Note that the `content-type` header must be set to `application/json`.
	 * 
	 * @param log
	 *            - A LogEntry object in JSON Format.
	 * @return The string "Added" as a JSON string object.
	 */
	@RequestMapping(headers = { "Accept=application/json" }, value = "/logs", method = RequestMethod.POST)
	public @ResponseBody
	int addLog(@Valid @RequestBody LogEntry log) {
		int result = -1;
		try {
			result = logEntryService.insertData(log);
			cubeService.handle(log);
		} catch (Exception ex) {
			LOG.debug("Error when received log: " + ex.getMessage());

		}
		return result;
	}

	/**
	 * Provides a way to query the datalogger.
	 * 
	 * Url format:
	 * 
	 * /logs/<userId>?timestart=<>&timeend=<>&tags=<>;<>;<>&applicationId=<>;
	 * sessionId=<>
	 * 
	 * All GET Parameters can be omitted. timestart, timeend and page will be
	 * set to defaults if missing.
	 * 
	 * If timestart or timeend is present a range of 10 years will be applied.
	 * If both are missing 10 years from now will be used.
	 * 
	 * Page will always be set to 1 if missing.
	 * 
	 * @param userId
	 *            - Required, userId to query (part of the url).
	 * @param timestart
	 *            - Optional, Start value of timerange to include in results
	 *            (YYYY-MM-DD format).
	 * @param timeend
	 *            - Optional, End value of timerange to include in results
	 *            (YYYY-MM-DD format).
	 * @param page
	 *            - Optional, The page of results to return.
	 * @param tags
	 *            - Optional, CSV (; separated) list of Tags to filter.
	 * @param applicationId
	 *            - Optional, Application Id to filter.
	 * @param sessionId
	 *            - Optional, Session Id to filter.
	 * @return A LogEntryResult object in JSON format.
	 */
	@RequestMapping(value = "/logs/{username}", method = RequestMethod.GET)
	public @ResponseBody
	LogEntryResult getLogs(
			@PathVariable String username,
			/*
			 * Following parameters are "semi-required" if they are not set,
			 * defaults will be used
			 */
			@RequestParam(value = "timestart", required = false) String timestart,
			@RequestParam(value = "timeend", required = false) String timeend,
			@RequestParam(value = "page", required = false) Integer page,
			/*
			 * Optional parameters. If omitted they will be ignored.
			 */
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "applicationId", required = false) String applicationId) {
		LogEntryFilter filter = new LogEntryFilter(username, timestart,
				timeend, page, tags, applicationId);
		return logEntryService.getLogs(filter);
	}

	@RequestMapping(value = "/logs/{username}/sessions/{session_type}", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<Session> getSessionsByType(
			@PathVariable("username") String username,
			@PathVariable("session_type") String type,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getUserSessionsByType(username, type, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/sessions/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Session getSessionsByType(@PathVariable("id") int id) {

		return cubeService.getSessionById(id);
	}

	@RequestMapping(value = "/logs/{username}/words", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<WordCount> getWordsForUser(
			@PathVariable("username") String username,
			@RequestParam(value = "status", required = false) String status,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getWordsForUser(username, status, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/words", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<WordCount> getAllWords(
			@RequestParam(value = "status", required = false) String status,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getAllWords(status, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/words/{gender}/{age}", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<WordCount> getAllWords(
			@RequestParam(value = "status", required = false) String status,
			@PathVariable("gender") String gender,
			@PathVariable("age") int age,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getAllWordsByAgeAndGender(gender, age, status, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/{username}/problems", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<Problem> getProblems(
			@PathVariable("username") String username,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getProblems(username, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/{username}/{problem_category}/{problem_index}/words", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<WordCount> getWordsByProblem(
			@PathVariable("username") String username,
			@PathVariable("problem_category") int category,
			@PathVariable("problem_index") int index,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getWordsByProblem(username, category, index, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/{username}/{problem_category}/{problem_index}/{number_of_sessions}/words", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<WordSuccessCount> getWordsByProblemAndSessions(
			@PathVariable("username") String username,
			@PathVariable("problem_category") int category,
			@PathVariable("problem_index") int index,
			@PathVariable("number_of_sessions") int numberOfSessions,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getWordsByProblemAndSessions(username, category, index, filters.getTimestart(), filters.getTimeend(), numberOfSessions, filters.isCount());
	}

	@RequestMapping(value = "/logs/problems/{gender}/{age}", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<Problem> getProblemsByGenderAndAge(
			@PathVariable("gender") String gender,
			@PathVariable("age") int age,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getProblemsByGenderAndAge(gender, age, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}


	@RequestMapping(value = "/logs/facts/{id}", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> getFactById(
			@PathVariable("id") int id) {

		return cubeService.getFactById(id);
	}

	@RequestMapping(value = "/logs/apps/{id}/facts", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<Map<String, Object>> getFactsForApplication(
			@PathVariable("id") int id,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getFactsForApplication(id, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

	@RequestMapping(value = "/logs/apps/{id}/words", method = RequestMethod.GET)
	public @ResponseBody
	ListWithCount<Map<String, Object>> getWordsForApplication(
			@PathVariable("id") int id,
			@RequestParam(value = "status", required = false) String status,
			@ModelAttribute RequestFilters filters) {

		return cubeService.getWordsForApplication(id, status, filters.getTimestart(), filters.getTimeend(), filters.isCount());
	}

}
