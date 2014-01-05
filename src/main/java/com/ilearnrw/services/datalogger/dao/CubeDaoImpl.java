package com.ilearnrw.services.datalogger.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.ilearnrw.services.datalogger.model.Application;
import com.ilearnrw.services.datalogger.model.ListWithCount;
import com.ilearnrw.services.datalogger.model.Problem;
import com.ilearnrw.services.datalogger.model.Session;
import com.ilearnrw.services.datalogger.model.SessionType;
import com.ilearnrw.services.datalogger.model.User;
import com.ilearnrw.services.datalogger.model.WordCount;

@Component
public class CubeDaoImpl implements CubeDao {

	private static Logger LOG = Logger.getLogger(CubeDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataLoggerCubeDataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataLoggerCubeDataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(
				dataLoggerCubeDataSource);
	}

	@Autowired
	DataSource dataLoggerCubeDataSource;

	@Override
	@Cacheable(value = "cube_applications", unless = "#result == -1")
	public int getApplicationIdByName(String applicationName) {
		LOG.debug("Hitting DB to get application " + applicationName);
		try {

			Application app = jdbcTemplate.queryForObject(
					"select * from applications where name like ? limit 0,1",
					new Object[] { applicationName },
					new BeanPropertyRowMapper<Application>(Application.class));
			return app.getId();
		} catch (Exception ex) {
			LOG.debug("Application not found: " + applicationName);
			return -1;

		}
	}

	@Override
	public int createApplication(String applicationId) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("name", applicationId);

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
			User user = jdbcTemplate.queryForObject(
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
	public int createUser(String username, char gender, int birthyear,
			String language) {

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("username", username);
		parameters.put("gender", gender);
		parameters.put("birthyear", birthyear);
		parameters.put("language", language);

		return insertAndReturnKey("users", parameters);
	}

	@Override
	@Cacheable(value = "cube_problems", unless = "#result == -1")
	public int getProblemByCategoryAndIndex(int problemCategory,
			int problemIndex) {
		LOG.debug(String.format("Hitting DB to get problem [%d,%d]",
				problemCategory, problemIndex));
		try {
			Problem problem = jdbcTemplate
					.queryForObject(
							"select * from problems where `category`=? and `idx`=? limit 0,1",
							new Object[] { problemCategory, problemIndex },
							new BeanPropertyRowMapper<Problem>(Problem.class));
			return problem.getId();
		} catch (Exception ex) {
			LOG.debug(String.format("Problem [%d,%d] not found",
					problemCategory, problemIndex));
			return -1;
		}
	}

	@Override
	public int createProblem(int problemCategory, int problemIndex,
			String description) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", problemCategory);
		parameters.put("idx", problemIndex);
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
			Session session = jdbcTemplate
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
			Timestamp timestamp, String username) {

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("sessiontype", SessionType.toChar(type));
		parameters.put("name", value);
		parameters.put("start", timestamp);
		parameters.put("username", username);

		return insertAndReturnKey("sessions", parameters);
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
		Session session = jdbcTemplate.queryForObject(
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

	private <T> ListWithCount<T> execute(String sql, boolean count,
			Map<String, Object> namedParameters, Class<T> classT) {
		ListWithCount<T> list = new ListWithCount<T>();
		if (count) {
			try {
				list.setCount(namedJdbcTemplate.queryForObject(sql,
						namedParameters, Integer.class));
			} catch (IncorrectResultSizeDataAccessException ex) {
				list.setCount(ex.getActualSize());
			}
		} else {
			list.setList(namedJdbcTemplate.query(sql, namedParameters,
					new BeanPropertyRowMapper<T>(classT)));
		}
		return list;
	}

	private ListWithCount<Map<String, Object>> execute(String sql,
			boolean count, Map<String, Object> namedParameters) {
		ListWithCount<Map<String, Object>> list = new ListWithCount<Map<String, Object>>();
		if (count) {
			try {
				list.setCount(namedJdbcTemplate.queryForObject(sql,
						namedParameters, Integer.class));
			} catch (IncorrectResultSizeDataAccessException ex) {
				list.setCount(ex.getActualSize());
			}
		} else {
			list.setList(namedJdbcTemplate.queryForList(sql, namedParameters));
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

		return namedJdbcTemplate.queryForList(sql, paramMap);
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

}
