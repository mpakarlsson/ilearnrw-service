package com.ilearnrw.api.datalogger.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.datalogger.model.LogEntryFilter;
import com.ilearnrw.api.datalogger.model.LogEntryResult;

@Repository
public class LogEntryDaoImpl implements LogEntryDao {

	private static Logger LOG = Logger.getLogger(LogEntryDaoImpl.class);

	@Autowired
	DataSource dataLoggerDataSource;

	@Override
	public LogEntry getLogEntry(int id) {
		JdbcTemplate template = new JdbcTemplate(dataLoggerDataSource);
		LogEntry entry = template.queryForObject("select * from logs where id=?",
				new Object[] { id },
				new BeanPropertyRowMapper<LogEntry>(LogEntry.class));
		return entry;
	}

	@Override
	public int insertData(LogEntry entry) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataLoggerDataSource)
				.withTableName("logs").usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("username", entry.getUsername());
		parameters.put("applicationId", entry.getApplicationId());
		parameters.put("timestamp", entry.getTimestamp());
		parameters.put("tag", entry.getTag());
		parameters.put("word", entry.getWord());
		parameters.put("problem_category", entry.getProblemCategory());
		parameters.put("problem_index", entry.getProblemIndex());
		parameters.put("duration", entry.getDuration());
		parameters.put("level", entry.getLevel());
		parameters.put("mode", entry.getMode());
		parameters.put("value", entry.getValue());


		Number newId = insert.executeAndReturnKey(parameters);

		return newId.intValue();

	}

	@Override
	public List<String> getUsers() {
		final ArrayList<String> result = new ArrayList<String>();
		String sql = "SELECT DISTINCT username FROM logs";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataLoggerDataSource);
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				result.add(arg0.getString("username"));
			}
		});
		return result;
	}

	@Override
	public LogEntryResult getLogs(LogEntryFilter filter) {
		final int resultLimit = 100;

		final Map<String, Object> debugInfo = new HashMap<String, Object>() {
			private static final long serialVersionUID = 4679762402945674449L;
		};

		/*
		 * Do some quick and dirty input sanitation. We always wan't to set an
		 * end/start date to avoid having to many variations on the SQL Query
		 * since it would be very hard to maintain all the possible
		 * combinations.
		 */

		String timeend = filter.timeend;
		String timestart = filter.timestart;

		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (timestart == null && timeend == null) {
				c.add(Calendar.DATE, 1);
				timeend = df.format(c.getTime());
				c.add(Calendar.YEAR, -10);
				timestart = df.format(c.getTime());
			} else if (timestart == null) {
				c.setTime(df.parse(timeend));
				c.add(Calendar.YEAR, -10);
				timeend = df.format(df.parse(timeend));
				timestart = df.format(c.getTime());
			} else if (timeend == null) {
				c.setTime(df.parse(timestart));
				c.add(Calendar.YEAR, 10);
				timeend = df.format(c.getTime());
				timestart = df.format(df.parse(timestart));
			} else {
				timestart = df.format(df.parse(timestart));
				timeend = df.format(df.parse(timeend));
			}

		} catch (ParseException e) {
			/*
			 * TODO: Figure out how to leave a nice error message. Should use
			 * HTTP status code to indicate the error.
			 */
			e.printStackTrace();
		}

		Integer page = filter.page;
		if (page == null)
			page = 1;

		final StringBuilder query = new StringBuilder(); // Contains the SELECT
															// logs
		final StringBuilder queryWhere = new StringBuilder(); // Contains the
																// WHERE clause
		final StringBuilder queryCount = new StringBuilder(); // Contains the
																// SELECT
																// COUNT(*) for
																// pageination
		query.append("SELECT * FROM logs WHERE ");
		queryCount.append("SELECT COUNT(*) FROM logs WHERE ");

		/*
		 * Add optional parameters to the query.
		 */
		class Filter {
			public Filter(String queryString, Object paramObj,
					Boolean addAndToQuery) {
				query = queryString;
				param = paramObj;
				addAND = addAndToQuery;
			}

			public Filter(String queryString, Object paramObj) {
				query = queryString;
				param = paramObj;
				addAND = true;
			}

			public String query;
			public Object param;
			public Boolean addAND;
		}
		;

		List<Filter> w = new ArrayList<Filter>();
		final List<Object> params = new ArrayList<Object>();
		String tag = filter.tag;
		if (tag != null) {
			w.add(new Filter("tag LIKE ?", tag));
		}
		
		String applicationId = filter.applicationId;
		if (applicationId != null)
			w.add(new Filter("applicationId=?", applicationId));

		for(Filter f : w)
		{
			queryWhere.append(f.query);
			params.add(f.param);
			if( f.addAND )
				queryWhere.append(" AND ");
		}

		/*
		 * Add required parameters to the query.
		 */
		queryWhere.append("userId=? AND timestamp BETWEEN ? AND ?");
		Integer userId = filter.userId;
		params.add(userId);
		params.add(timestart);
		params.add(timeend);

		query.append(queryWhere.toString());
		queryCount.append(queryWhere.toString());
		// Make a copy of params for the Count (we don't what the LIMIT in
		// there)
		final List<Object> paramsCount = new ArrayList<Object>(params);
		query.append(" ORDER BY timestamp LIMIT ?, ?;");
		params.add((page - 1) * resultLimit);
		params.add(resultLimit);

		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataLoggerDataSource);

		/*
		 * Execute the query and return the results.
		 */
		List<LogEntry> results = new ArrayList<LogEntry>() {
			private static final long serialVersionUID = 4679762402945674448L;
			{
				String sql = query.toString();

				debugInfo.put("sql", sql);
				debugInfo.put("sql-params", params);

				jdbcTemplate.query(sql, params.toArray(),
						new RowCallbackHandler() {

							@Override
							public void processRow(ResultSet rs)
									throws SQLException {
								LogEntry log = new LogEntry(
										rs.getString("username"), 
										rs.getString("applicationId"),
										rs.getTimestamp("timestamp"), 
										rs.getString("tag"), 
										rs.getString("word"), 
										rs.getInt("problem_category"),
										rs.getInt("problem_index"),
										rs.getFloat("duration"),
										rs.getString("level"),
										rs.getString("mode"),
										rs.getString("value")
										);
								add(log);
							}
						});
			}
		};

		class PageResult {
			public int totalAmountOfPages;
		}
		;
		final PageResult pageResult = new PageResult();

		String sqlCount = queryCount.toString();
		debugInfo.put("sql-count", sqlCount);
		debugInfo.put("sql-count-params", paramsCount);
		jdbcTemplate.query(sqlCount, paramsCount.toArray(),
				new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {
						int result = rs.getInt("COUNT(*)");
						if (result == 0)
							pageResult.totalAmountOfPages = result;
						else
							pageResult.totalAmountOfPages = (result / resultLimit) + 1;
					}
				});

		LOG.debug(debugInfo.toString());
		
		return new LogEntryResult(page, pageResult.totalAmountOfPages, results,
				debugInfo.toString());

	}

}
