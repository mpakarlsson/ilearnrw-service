package com.ilearnrw.api.datalogger.dao;

import ilearnrw.utils.LanguageCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.mockito.internal.matchers.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ilearnrw.api.datalogger.model.Application;
import com.ilearnrw.api.datalogger.model.ListWithCount;
import com.ilearnrw.api.datalogger.model.Problem;
import com.ilearnrw.api.datalogger.model.Session;
import com.ilearnrw.api.datalogger.model.SessionType;
import com.ilearnrw.api.datalogger.model.User;
import com.ilearnrw.api.datalogger.model.WordCount;
import com.ilearnrw.api.datalogger.model.WordSuccessCount;
import com.ilearnrw.api.datalogger.model.filters.DateFilter;
import com.ilearnrw.api.datalogger.model.filters.StudentFilter;
import com.ilearnrw.api.datalogger.model.result.BreakdownResult;
import com.ilearnrw.api.datalogger.model.result.OverviewBreakdownResult;

@Repository
public class CubeDaoImpl implements CubeDao {

	private static Logger LOG = Logger.getLogger(CubeDaoImpl.class);

	@Autowired
	DataSource dataLoggerCubeDataSource;

	@Override
	@Cacheable(value = "cube_applications", unless = "#result == -1")
	public int getApplicationIdByAppId(String applicationId) {
		LOG.debug("Hitting DB to get application " + applicationId);
		try {

			Application app = new JdbcTemplate(dataLoggerCubeDataSource).queryForObject(
					"select * from applications where app_id like ? limit 0,1",
					new Object[] { applicationId },
					new BeanPropertyRowMapper<Application>(Application.class));
			return app.getId();
		} catch (Exception ex) {
			LOG.debug("Application not found: " + applicationId);
			return -1;

		}
	}

	@Override
	public int createApplication(String applicationId, String name) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("app_id", applicationId);
		parameters.put("name", name);

		return insertAndReturnKey("applications", parameters);
	}

	private int insertAndReturnKey(String tableName,
			Map<String, Object> parameters) {
		try {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(
					dataLoggerCubeDataSource).withTableName(tableName)
					.usingGeneratedKeyColumns("id");

			Number newId = insert.executeAndReturnKey(parameters);

			return newId.intValue();
		} catch (Exception ex) {
			LOG.debug("Failed to insert into " + tableName + ": "
					+ ex.getMessage());
			return -1;
		}
	}

	@Override
	@Cacheable(value = "cube_users", unless = "#result == -1")
	public int getUserIdByName(String username) {
		LOG.debug("Hitting DB to get user " + username);
		try {
			User user = new JdbcTemplate(dataLoggerCubeDataSource).queryForObject(
					"select * from users where username like ? limit 0,1",
					new Object[] { username }, new BeanPropertyRowMapper<User>(
							User.class));
			return user.getId();
		} catch (Exception ex) {
			LOG.debug("User not found: " + username);
			return -1;
		}

	}
	
	@Override
	public String getUsername(int userId) {
		LOG.debug("Hitting DB to get user with id " + userId);
		try {
			User user = new JdbcTemplate(dataLoggerCubeDataSource).queryForObject(
					"select * from users where id = ? limit 0,1",
					new Object[] { userId }, new BeanPropertyRowMapper<User>(
							User.class));
			return user.getUsername();
		} catch (Exception ex) {
			LOG.debug("User not found: " + userId);
			return "";
		}
	}

	@Override
	public int createUser(String username, String gender, int birthyear,
			String language, String classroom, String school) {

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("username", username);
		parameters.put("gender", gender);
		parameters.put("birthyear", birthyear);
		parameters.put("language", language);
		parameters.put("classroom", classroom);
		parameters.put("school", school);

		return insertAndReturnKey("users", parameters);
	}

	@Override
	@Cacheable(value = "cube_problems", unless = "#result == -1")
	public int getProblemByCategoryIndexAndLanguage(int problemCategory,
			int problemIndex, LanguageCode languageCode) {
		LOG.debug(String.format("Hitting DB to get problem [%d,%d]",
				problemCategory, problemIndex));
		try {
			Problem problem = new JdbcTemplate(dataLoggerCubeDataSource)
					.queryForObject(
							"select * from problems where `category`=? and `idx`=? and `language`=? limit 0,1",
							new Object[] { problemCategory, problemIndex, languageCode.getCode() },
							new BeanPropertyRowMapper<Problem>(Problem.class));
			return problem.getId();
		} catch (Exception ex) {
			LOG.debug(String.format("Problem [%d,%d] not found",
					problemCategory, problemIndex));
			return -1;
		}
	}

	@Override
	public int createProblem(int problemCategory, int problemIndex, int languageCode,
			String description) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", problemCategory);
		parameters.put("idx", problemIndex);
		parameters.put("language", languageCode);
		parameters.put("description", description);

		return insertAndReturnKey("problems", parameters);
	}

	@Override
	public int createFact(Timestamp timestamp, int userId, int appId,
			int problemId, String word, String wordStatus, float duration,
			int learnSessionId, int appSessionId, int appRoundSessionId) {

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("timestamp", timestamp);
		parameters.put("user_ref", userId);
		parameters.put("app_ref", appId);
		parameters.put("problem_ref", problemId);
		parameters.put("word", word);
		parameters.put("word_status", wordStatus);
		parameters.put("duration", duration);
		parameters.put("learn_session_ref", learnSessionId);
		parameters.put("app_session_ref", appSessionId);
		parameters.put("app_round_session_ref", appRoundSessionId);

		return insertAndReturnKey("facts", parameters);
	}

	@Override
	public int getLastSessionIdByType(String username, SessionType type) {
		try {
			Session session = new JdbcTemplate(dataLoggerCubeDataSource)
					.queryForObject(
							"select * from sessions where username=? and sessiontype=? order by start desc limit 0,1",
							new Object[] { username,
									String.valueOf(SessionType.toChar(type)) },
							new BeanPropertyRowMapper<Session>(Session.class));
			return session.getId();
		} catch (Exception ex) {
			LOG.debug(String.format("No session with type [%c] found: %s",
					SessionType.toChar(type), ex.getMessage()));
			return -1;
		}
	}

	@Override
	public int createSession(SessionType type, String value,
			Timestamp timestamp, String username, String supervisor) {

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("sessiontype", SessionType.toChar(type));
		parameters.put("name", value);
		parameters.put("start", timestamp);
		parameters.put("username", username);
		parameters.put("supervisor", supervisor);

		return insertAndReturnKey("sessions", parameters);
	}
	
	@Override
	public void endSession(int sessionId) {
		new JdbcTemplate(dataLoggerCubeDataSource).update("update sessions set end = ? where id = ?", sessionId);
	}

	@Override
	public ListWithCount<Session> getUserSessionsByType(String username,
			String type, String timestart, String timeend, boolean count) {

		String fields = count ? "count(*)" : "*";
		String sql = "select "
				+ fields
				+ " from sessions where username=:username and sessiontype=:sessiontype and start>:start and start<:end";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("username", username);
		namedParameters.put("sessiontype", type);
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));

		return execute(sql, count, namedParameters, Session.class);
	}

	@Override
	public Session getSessionById(int id) {
		Session session = new JdbcTemplate(dataLoggerCubeDataSource).queryForObject(
				"select * from sessions where id=?", new Object[] { id },
				new BeanPropertyRowMapper<Session>(Session.class));
		return session;
	}

	@Override
	public ListWithCount<WordCount> getWordsForUser(int userId, String status,
			String timestart, String timeend, boolean count) {

		String fields = count ? "count(*)" : "count(*) as count, word";
		String sql = "select " + fields
				+ " from facts where timestamp>=:start and timestamp<=:end ";

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));

		if (userId != NO_USER) {
			sql += " and user_ref=:userid ";
			namedParameters.put("userid", userId);
		}
		if (status != null) {
			sql += " and word_status=:status ";
			namedParameters.put("status", status);
		}
		sql += " group by word ";

		return execute(sql, count, namedParameters, WordCount.class);
	}

	@Override
	public ListWithCount<WordCount> getWordsByAgeAndGender(String gender,
			int age, String status, String timestart, String timeend,
			boolean count) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int birthyear = year - age;

		String fields = count ? "count(*)" : "count(*) as count, word";
		String sql = "select " + fields
				+ " from facts f left join users u on u.id=f.user_ref "
				+ " where timestamp>=:start and timestamp<=:end ";

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));

		if (gender.compareTo("*") != 0) {
			sql += " and u.gender=:gender ";
			namedParameters.put("gender", gender);
		}
		if (age > 0) {
			sql += " and u.birthyear=:birthyear";
			namedParameters.put("birthyear", birthyear);
		}
		if (status != null) {
			sql += " and f.word_status=:status ";
			namedParameters.put("status", status);
		}
		sql += " group by f.word ";

		return execute(sql, count, namedParameters, WordCount.class);
	}

	@Override
	public ListWithCount<Problem> getProblems(int userId, String timestart,
			String timeend, boolean count) {

		String sql = "select "
				+ (count ? "count(*)" : " p.*")
				+ " from facts f left join problems p on f.problem_ref=p.id "
				+ " where p.id is not null "
				+ " and f.user_ref=:userid and f.timestamp>=:start and f.timestamp<=:end and f.word_status='WORD_FAILED' "
				+ " group by f.problem_ref order by count(f.problem_ref) desc";

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("userid", userId);
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));

		return execute(sql, count, namedParameters, Problem.class);
	}

	@Override
	public ListWithCount<WordCount> getWordsByProblem(int userId, int category,
			int index, String timestart, String timeend, boolean count) {
		String sql = "select "
				+ (count ? "count(distinct f.word)"
						: " f.word, count(f.word) as count")
				+ " from facts f left join problems p on f.problem_ref=p.id "
				+ " where p.id is not null "
				+ " and f.user_ref=:userid and f.timestamp>=:start and f.timestamp<=:end "
				+ " and p.category=:category and p.idx=:index" + " group by "
				+ (count ? "p.id" : "f.word") + " order by count(f.word) desc";

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("userid", userId);
		namedParameters.put("category", category);
		namedParameters.put("index", index);
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));

		return execute(sql, count, namedParameters, WordCount.class);
	}

	@Override
	public ListWithCount<WordSuccessCount> getWordsByProblemAndSessions(int userId, int category,
			int index, String timestart, String timeend, int numberOfSessions) {
		String sql = "select  f.word, sum(f.word_status='WORD_DISPLAYED') as count, "
				+ "sum(f.word_status='WORD_SUCCESS') as succeed, "
				+ "sum(f.word_status='WORD_FAILED') as failed "
				+ "from facts f "
				+ "left join problems p on f.problem_ref=p.id "
				+ "right join ( "
				+ "select app_session_ref as theid from facts "
				+ "where user_ref=:userid "
				+ "group by app_session_ref order by app_session_ref desc limit :numberOfSessions ) "
				+ "as s on f.app_session_ref=s.theid "
				+ "where p.id is not null  and f.user_ref=:userid " 
				+ "and f.timestamp>=:start and f.timestamp<=:end "
				+ "and p.category=:category and p.idx=:index" + " group by f.word";

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("userid", userId);
		namedParameters.put("category", category);
		namedParameters.put("index", index);
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));
		namedParameters.put("numberOfSessions", numberOfSessions>=0 ? numberOfSessions : Integer.MAX_VALUE);

		return execute(sql, false, namedParameters, WordSuccessCount.class);
	}

	private <T> ListWithCount<T> execute(String sql, boolean count,
			Map<String, Object> namedParameters, Class<T> classT) {
		ListWithCount<T> list = new ListWithCount<T>();
		if (count) {
			try {
				list.setCount(new NamedParameterJdbcTemplate(dataLoggerCubeDataSource).queryForObject(sql,
						namedParameters, Integer.class));
			} catch (IncorrectResultSizeDataAccessException ex) {
				list.setCount(ex.getActualSize());
			}
		} else {
			list.setList(new NamedParameterJdbcTemplate(dataLoggerCubeDataSource).query(sql, namedParameters,
					new BeanPropertyRowMapper<T>(classT)));
		}
		return list;
	}

	private ListWithCount<Map<String, Object>> execute(String sql,
			boolean count, Map<String, Object> namedParameters) {
		ListWithCount<Map<String, Object>> list = new ListWithCount<Map<String, Object>>();
		if (count) {
			try {
				list.setCount(new NamedParameterJdbcTemplate(dataLoggerCubeDataSource).queryForObject(sql,
						namedParameters, Integer.class));
			} catch (IncorrectResultSizeDataAccessException ex) {
				list.setCount(ex.getActualSize());
			}
		} else {
			list.setList(new NamedParameterJdbcTemplate(dataLoggerCubeDataSource).queryForList(sql, namedParameters));
		}
		return list;
	}

	@Override
	public ListWithCount<Problem> getProblemsByGenderAndAge(String gender,
			int age, String timestart, String timeend, boolean count) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int birthyear = year - age;

		String sql = "select " + (count ? "count(distinct p.id)" : " p.*")
				+ " from facts f left join problems p on f.problem_ref=p.id "
				+ " left join users u on f.user_ref=u.id "
				+ " where p.id is not null " + " and u.gender=:gender"
				+ (age > 0 ? " and u.birthyear=:birthyear " : "")
				+ " and f.timestamp>=:start and f.timestamp<=:end "
				+ " group by f.problem_ref order by count(f.problem_ref) desc";

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("gender", gender);
		namedParameters.put("birthyear", birthyear);
		namedParameters.put("start", TimeUtils.minIfNull(timestart));
		namedParameters.put("end", TimeUtils.maxIfNull(timeend));

		return execute(sql, count, namedParameters, Problem.class);
	}

	@Override
	public List<Map<String, Object>> getFactById(int id) {
		String sql = "select f.id, f.word, f.word_status, f.duration, f.timestamp, "
				+ " u.username, u.gender, u.birthyear, u.language, "
				+ " a.name as app_name, "
				+ " p.category as problem_category, p.idx as problem_index, "
				+ " ls.start as learn_session_start, apps.start as app_session_start, rs.start as app_round_session_start"
				+ " from facts f "
				+ " left join users u on f.user_ref=u.id "
				+ " left join applications a on f.app_ref=a.id "
				+ " left join problems p on f.problem_ref=p.id "
				+ " left join sessions ls on f.learn_session_ref=ls.id "
				+ " left join sessions apps on f.app_session_ref=apps.id "
				+ " left join sessions rs on f.app_round_session_ref=rs.id "
				+ " where f.id=:id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);

		return new NamedParameterJdbcTemplate(dataLoggerCubeDataSource).queryForList(sql, paramMap);
	}

	@Override
	public ListWithCount<Map<String, Object>> getFactsForApplication(int id,
			String timestart, String timeend, boolean count) {
		String fields = count ? " count(distinct f.id) "
				: " f.id, f.word, f.word_status, f.duration, f.timestamp, "
						+ " u.username, u.gender, u.birthyear, u.language, "
						+ " a.name as app_name, "
						+ " p.category as problem_category, p.idx as problem_index, "
						+ " ls.start as learn_session_start, apps.start as app_session_start, rs.start as app_round_session_start ";

		String sql = "select " + fields + " from facts f "
				+ " left join users u on f.user_ref=u.id "
				+ " left join applications a on f.app_ref=a.id "
				+ " left join problems p on f.problem_ref=p.id "
				+ " left join sessions ls on f.learn_session_ref=ls.id "
				+ " left join sessions apps on f.app_session_ref=apps.id "
				+ " left join sessions rs on f.app_round_session_ref=rs.id "
				+ " where a.id=:id "
				+ " and f.timestamp>:start and f.timestamp<:end";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("start", TimeUtils.minIfNull(timestart));
		paramMap.put("end", TimeUtils.maxIfNull(timeend));

		return execute(sql, count, paramMap);
	}

	@Override
	public ListWithCount<Map<String, Object>> getWordsForApplication(int id,
			String status, String timestart, String timeend, boolean count) {
		String fields = count ? " count(distinct f.word) "
				: " f.word, count(f.word) as count ";
		String sql = "select " + fields + " from facts f "
				+ " left join applications a on f.app_ref=a.id "
				+ " where a.id=:id "
				+ " and f.timestamp>:start and f.timestamp<:end ";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (status != null) {
			sql += " and f.word_status=:status ";
			paramMap.put("status", status);
		}
		sql += " group by f.word";

		paramMap.put("id", id);
		paramMap.put("start", TimeUtils.minIfNull(timestart));
		paramMap.put("end", TimeUtils.maxIfNull(timeend));

		return execute(sql, count, paramMap);
	}

	@Override
	public BreakdownResult getSkillBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter, int category, int language) {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("language", language);
			parameterMap.put("category", category);
			String studentFilterString = getStudentFilterString(studentFilter,
					parameterMap);
			String dateFilterString = getDateFilterString(dateFilter,
					parameterMap, "rds_start");
			String sql = "select "
					+ "sum(duration) as timeSpent, "
					+ "sum(word_success) as correctAnswers, "
					+ "sum(word_failed) as incorrectAnswers, "
					+ "sum(word_success_or_failed) as totalAnswers, "
					+ "format_success_rate(sum(word_success), sum(word_success_or_failed)) as successRate "
					+ "from facts_expanded "
					+ "where p_language = :language and "
					+ "category = :category and " + studentFilterString
					+ "and " + dateFilterString + "group by category;";
			return new NamedParameterJdbcTemplate(dataLoggerCubeDataSource)
					.queryForObject(sql, parameterMap,
							new BeanPropertyRowMapper<BreakdownResult>(
									BreakdownResult.class));
		} catch (EmptyResultDataAccessException e) {
			return new BreakdownResult();
		}
	}

	@Override
	public BreakdownResult getActivityBreakdownResult(DateFilter dateFilter,
			StudentFilter studentFilter, String activityName) {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("app_name", activityName);
			String studentFilterString = getStudentFilterString(studentFilter,
					parameterMap);
			String dateFilterString = getDateFilterString(dateFilter,
					parameterMap, "rds_start");
			String sql = "select "
					+ "sum(aps_duration) as timeSpent, "
					+ "sum(word_success) as correctAnswers, "
					+ "sum(word_failed) as incorrectAnswers, "
					+ "sum(word_success_or_failed) as totalAnswers, "
					+ "format_success_rate(sum(word_success), sum(word_success_or_failed)) as successRate "
					+ "from facts_expanded " + "where " + dateFilterString
					+ "and " + studentFilterString
					+ "and app_name = :app_name " + "group by app_name;";
			return new NamedParameterJdbcTemplate(dataLoggerCubeDataSource)
					.queryForObject(sql, parameterMap,
							new BeanPropertyRowMapper<BreakdownResult>(
									BreakdownResult.class));
		} catch (EmptyResultDataAccessException e) {
			return new BreakdownResult();
		}
	}

	@Override
	public OverviewBreakdownResult getOverviewBreakdownResult(
			DateFilter dateFilter, StudentFilter studentFilter) {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			String studentFilterString = getStudentFilterString(studentFilter,
					parameterMap);
			String dateFilterString = getDateFilterString(dateFilter,
					parameterMap, "rds_start");
			String sql = "select count(distinct app_name) as numberOfActivities, "
					+ "coalesce(sum(timeSpent),0) as timeSpent, "
					+ "coalesce(sum(success),0) as correctAnswers, "
					+ "coalesce(sum(failed),0) as incorrectAnswers, "
					+ "coalesce(sum(total),0) as totalAnswers, "
					+ "format_success_rate(sum(success), sum(total)) as successRate "
					+ "from "
					+ "    (select app_name, aps_duration as timeSpent, "
					+ "    sum(word_success) as success, "
					+ "    sum(word_failed) as failed, "
					+ "    sum(word_success_or_failed) as total "
					+ "    from facts_expanded "
					+ "    where "
					+ dateFilterString
					+ "    and "
					+ studentFilterString
					+ "    group by app_name "
					+ "    having total > 0) as overview_breakdown;";

			String sql_category = "select distinct category as categories "
					+ "from facts_expanded "
					+ "where word_success_or_failed > 0 and category != 0 and "
					+ dateFilterString + " and " + studentFilterString;
			List<String> skillsWorkedOn = new NamedParameterJdbcTemplate(
					dataLoggerCubeDataSource).query(sql_category, parameterMap,
					new RowMapper<String>() {
						@Override
						public String mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							return rs.getString(1);
						}
					});
			OverviewBreakdownResult result = new NamedParameterJdbcTemplate(
					dataLoggerCubeDataSource).queryForObject(sql, parameterMap,
					new BeanPropertyRowMapper<OverviewBreakdownResult>(
							OverviewBreakdownResult.class));
			result.setSkillsWorkedOn(skillsWorkedOn);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return new OverviewBreakdownResult();
		}
	}

	private String getDateFilterString(DateFilter dateFilter,
			Map<String, Object> parameterMap, String fieldName) {
		String dateFilterString;
		switch (dateFilter.getType()) {
		case TODAY:
			dateFilterString = "( DATE(" + fieldName + ") = CURDATE()) ";
			break;
		case WEEK:
			dateFilterString = "( DATE(" + fieldName
					+ ") between CURDATE() - INTERVAL 1 WEEK and CURDATE()) ";
			break;
		case MONTH:
			dateFilterString = "( DATE(" + fieldName
					+ ") between CURDATE() - INTERVAL 1 MONTH and CURDATE()) ";
			break;
		case CUSTOM:
			dateFilterString = "( DATE(" + fieldName
					+ ") between DATE(:start) and DATE(:end)) ";
			parameterMap.put("start", dateFilter.getStartDate());
			parameterMap.put("end", dateFilter.getEndDate());
			break;
		case ALL:
		default:
			dateFilterString = "true ";
			break;
		}
		return dateFilterString;
	}

	private String getStudentFilterString(StudentFilter studentFilter,
			Map<String, Object> parameterMap) {
		String studentFilterString;
		switch (studentFilter.getType()) {
		case CLASSROOM:
			studentFilterString = "classroom = :name ";
			parameterMap.put("name", studentFilter.getName());
			break;
		case SCHOOL:
			studentFilterString = "school = :name ";
			parameterMap.put("name", studentFilter.getName());
			break;
		case STUDENT:
			studentFilterString = "username = :name ";
			parameterMap.put("name", studentFilter.getName());
			break;
		case ALL:
		default:
			studentFilterString = "true ";
		}
		return studentFilterString;
	}

}
