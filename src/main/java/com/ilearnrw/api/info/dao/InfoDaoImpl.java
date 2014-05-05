package com.ilearnrw.api.info.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.model.Problem;

@Repository
public class InfoDaoImpl implements InfoDao {

	private static Logger LOG = Logger.getLogger(InfoDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	@Qualifier("usersDataSource")
	private DataSource dSource;

	
	@Autowired
	public void setDataSource(DataSource dataLoggerCubeDataSource) {
		this.jdbcTemplate = new JdbcTemplate(dSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(
				dSource);
	}
	@Override
	public Application getApplication(int id) {
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("id", id);
		List<Application> entry = namedJdbcTemplate.query("select * from applications where id=:id", 
				namedParameters,
				new BeanPropertyRowMapper<Application>(Application.class));
		if (entry.isEmpty()) {
			return null;
		}
		return entry.get(0);
	}
	@Override
	public List<Application> getApps() {
		List<Application> results = jdbcTemplate.query("select * from applications", 
				new BeanPropertyRowMapper<Application>(Application.class)
		);
		
		return results;
	}
	@Override
	public List<Problem> getProblems(String language) {
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("lang", language);
		List<Problem> results = namedJdbcTemplate.query("select * from problems where language=:lang", 
				namedParameters, new BeanPropertyRowMapper<Problem>(Problem.class)
		);
		return results;
	}
	@Override
	public List<int[]> getAppsProblemCorrespondence(
			String language) {
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("lang", language);

		List<Map<String, Object>> rows = namedJdbcTemplate.queryForList("select ap.id_application, ap.id_problem from apps_problems ap"
				+ " left join problems p on p.id=ap.id_problem"
				+ " where p.language=:lang", namedParameters);
		List<int[]> results = new ArrayList<int[]>();
		for(Map row: rows) {
			results.add(new int[] { (Integer)row.get("id_application"), (Integer)row.get("id_problem") } );
		}
		return results;
	}

}
