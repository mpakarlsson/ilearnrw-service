package com.ilearnrw.services.datalogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 
 * @author David Johansson
 * 
 * The guide that i've used to get spring-jdbc running:
 * 
 *     http://www.beingjavaguys.com/2013/07/spring-jdbc-template-with-spring-mvc.html
 *     
 *     
 * 
 * Database
 * --------
 * 
 * You need the mysql-connector-java driver: http://dev.mysql.com/downloads/connector/j/
 * 
 * Database name: datalogs
 * 
 * 
 * 
 * Create the Logs table:
 * 
 * 		CREATE TABLE logs (
 * 			id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 * 			userId VARCHAR(32),
 * 			tag VARCHAR(32),
 * 			value VARCHAR(512),
 * 			applicationId VARCHAR(32),
 * 			timestamp DATETIME,
 * 			sessionId VARCHAR(32)
 * 		);
 * 
  		CREATE TABLE logs(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, userId VARCHAR(32), tag VARCHAR(32), value VARCHAR(512), applicationId VARCHAR(32), timestamp DATETIME, sessionId VARCHAR(32));
 * 		
 * 
 * 				Logs
 * ----------------------------------
 *            id | PK, AUTO_INCREMENT
 *        userId | VarChar(32)
 *           tag | VarChar(32)
 *         value | VarChar(512)
 * applicationId | VarChar(32)
 *     timestamp | DateTime
 *     sessionId | VarChar(32)
 *
 * Insert Procedure
 * 
 * DELIMITER //
 * CREATE PROCEDURE addLog
 * BEGIN
 * 		SET @userId := "";
 * 
 */

@Controller
public class DataloggerController {


	private static Logger LOG = Logger
			.getLogger(DataloggerController.class);

	@Autowired
	DataSource dataLoggerDataSource;

	@RequestMapping(value = "/log/users", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getUsers()
	{
		final ArrayList<String> result = new ArrayList<String>();
		String sql = "SELECT DISTINCT userId FROM logs";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataLoggerDataSource);
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				result.add(arg0.getString("userId"));
			}
		}); 
		return result;
	}

	@RequestMapping(value = "/log/ping", method = RequestMethod.GET)
	public @ResponseBody
	String ping() {
		return "\"pong\"";
	}
	
	/**
	 */
	@RequestMapping(headers = {"Accept=application/json"},
					value = "/log", 
					method = RequestMethod.POST
					)
	public @ResponseBody
	String addLog(
			@RequestBody LogEntry log
			) {
		
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataLoggerDataSource)
			.withProcedureName("addLog");
		jdbcCall.addDeclaredParameter(new SqlParameter("inUserId", Types.VARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("inTag", Types.VARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("inValue", Types.VARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("inApplicationId", Types.VARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("inSessionId", Types.VARCHAR));
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("inUserId", log.getUserId());
		args.put("inTag", log.getTag());
		args.put("inValue", log.getValue());
		args.put("inApplicationId", log.getApplicationId());
		args.put("inSessionId", log.getSessionId());

		jdbcCall.execute(args);
		
		return "\"Added\"";
	}
	
	/**
	 * /logs/<userId>?timestart=<>&timeend=<>&tags=<>;<>;<>&applicationId=<>;sessionId=<>
	 * 
	 * We also need some pagination here as we don't always wan't to return all the results.
	 * 
	 * @return A list of log entries.
	 */
	@RequestMapping(value = "/logs/{userId}", method = RequestMethod.GET)
	public @ResponseBody
	LogEntryResult getLogs(
			@PathVariable String userId,
			/* Following parameters are "semi-required" if they are not set, defaults will be used
			 */
			@RequestParam(value = "timestart", required = false) String timestart,
			@RequestParam(value = "timeend", required = false) String timeend,
			@RequestParam(value = "page", required = false) Integer page,
			/* Optional parameters. If omitted they will be ignored.
			 */
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "applicationId", required = false) String applicationId,
			@RequestParam(value = "sessionId", required = false) String sessionId

			)
	{

		final int resultLimit = 100;

		final Map<String, Object> debugInfo = new HashMap<String, Object>() {
			private static final long serialVersionUID = 4679762402945674449L;
		};

		/* Do some quick and dirty input sanitation.
		 * We always wan't to set an end/start date
		 * to avoid having to many variations on the
		 * SQL Query since it would be very
		 * hard to maintain all the possible combinations.
		 */

		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if( timestart == null && timeend == null ) {
				c.add(Calendar.DATE, 1);
				timeend = df.format(c.getTime());
				c.add(Calendar.YEAR, -10);
				timestart = df.format(c.getTime());
			} else if(timestart == null) {
				c.setTime(df.parse(timeend));
				c.add(Calendar.YEAR, -10);
				timeend = df.format(df.parse(timeend));
				timestart = df.format(c.getTime());
			} else if(timeend == null) {
				c.setTime(df.parse(timestart));
				c.add(Calendar.YEAR, 10);
				timeend = df.format(c.getTime());
				timestart = df.format(df.parse(timestart));
			} else {
				timestart = df.format(df.parse(timestart));
				timeend = df.format(df.parse(timeend));
			}
				
		} catch (ParseException e) {
			/* TODO: Figure out how to leave a nice error message.
			 * 	 	 Should use HTTP status code to indicate the error.
			 */
			e.printStackTrace();
		}
		if(page == null)
			page = 1;
		
		final StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM logs WHERE ");

		
		/* Add optional parameters to the query.
		 */
		class Filter
		{
			public Filter(String queryString, Object paramObj,
						  Boolean addAndToQuery)
			{
				query = queryString;
				param = paramObj;
				addAND = addAndToQuery;
			}
			public Filter(String queryString, Object paramObj)
			{
				query = queryString;
				param = paramObj;
				addAND = true;
			}
			public String query;
			public Object param;
			public Boolean addAND;
		};

		List<Filter> w = new ArrayList<Filter>();
		final List<Object> params = new ArrayList<Object>();
		if( tags != null )
		{
			List<String> tagList = Arrays.asList(tags.split(";"));
			//List<String> tagList = new ArrayList<String>(tags.split(";"));
			if(tagList.size() == 1)
				w.add(new Filter("tag=?", tagList.get(0)));
			else if(tagList.size() > 1 )
			{
				Boolean first = true;
				Iterator<String> tagItr = tagList.iterator();
			    while (tagItr.hasNext()) {
			    	String tag = tagItr.next();
					if( first ) {
						first = false;
						w.add(new Filter("tag IN (?,", tag, false));
					} else if( tagItr.hasNext())
				    	w.add(new Filter("?,", tag, false));
			    	else
				    	w.add(new Filter("?)", tag));
			    }
			}
			/*else ignore*/
		}
		if( applicationId != null )
			w.add(new Filter("applicationId=?", applicationId));
		if( sessionId != null )
			w.add(new Filter("sessionId=?", sessionId));
		
		for(Filter f : w)
		{
			query.append(f.query);
			params.add(f.param);
			if( f.addAND )
				query.append(" AND ");
		}

		/* Add required parameters to the query.
		 */
		query.append("userId=? AND timestamp BETWEEN ? AND ? LIMIT ?, ?;");
		params.add(userId);
		params.add(timestart);
		params.add(timeend);
		params.add((page-1) * resultLimit);
		params.add(page * resultLimit);
		
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataLoggerDataSource);

		List<LogEntry> results = new ArrayList<LogEntry>() {
			private static final long serialVersionUID = 4679762402945674448L;
		{
			String sql = query.toString();

			debugInfo.put("sql", sql);
			debugInfo.put("sql-params", params);

			jdbcTemplate.query(sql, params.toArray(), new RowCallbackHandler() {
				
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					LogEntry log = new LogEntry(rs.getString("userId"),
												rs.getString("tag"),
												rs.getString("value"),
												rs.getString("applicationId"),
												rs.getTimestamp("timestamp"),
												rs.getString("sessionId"));
					add(log);
				}
			});
		}};
		
		return new LogEntryResult(page, results, debugInfo.toString());
	}



	
	
}
