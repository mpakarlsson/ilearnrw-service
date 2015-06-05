package com.ilearnrw.common.security.users.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ilearnrw.common.security.users.model.User;

@Repository
public class ExpertTeacherDaoImpl implements ExpertTeacherDao {
	
	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;
	
	@Override
	public List<User> getExpertsList() {
		List<User> expertList = new ArrayList<User>();

		String sql = "select u.* from users u, role_members rm, roles r "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_EXPERT' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		expertList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return expertList;
	}
	
	@Override
	public List<User> getAllTeachersList() {
		List<User> teacherList = new ArrayList<User>();

		String sql = "select u.* from users u, role_members rm, roles r "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_TEACHER' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		teacherList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return teacherList;
	}
	
	@Override
	public List<User> getUnassignedTeachersList() {
		List<User> teacherList = new ArrayList<User>();

		String sql = "select u.* from users u, roles r, role_members rm "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_TEACHER' "
				+ "and u.id not in (select et.teacher_id from experts_teachers et)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		teacherList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return teacherList;
	}
	
	@Override
	public List<User> getTeacherList(User expert) {
		List<User> studentList = new ArrayList<User>();

		String sql = "select u.* from users u, experts_teachers et "
				+ "where u.id = et.teacher_id and et.expert_id = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		studentList = jdbcTemplate.query(sql, new Object[] { expert.getId() },
				new BeanPropertyRowMapper<User>(User.class));
		return studentList;
	}
	
	@Override
	public void setTeacherList(final User expert, final List<User> teachers) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from experts_teachers where expert_id=?",
				new Object[] { expert.getId() });

		String sql = "insert into experts_teachers (expert_id, teacher_id) values (?,?);";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement pStatement, int index)
					throws SQLException {
				User student = teachers.get(index);
				pStatement.setInt(1, expert.getId());
				pStatement.setInt(2, student.getId());
			}

			@Override
			public int getBatchSize() {
				if (teachers == null)
					return 0;
				return teachers.size();
			}
		});
	}
	
	@Override
	public void assignTeacherToExpert(User expert, User teacher) {
		String sql = "insert into experts_teachers (expert_id, teacher_id) values (?,?);";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, new Object[] { expert.getId(), teacher.getId() });
	}

	@Override
	public boolean isUserAssignedToExpert(String userName, String expertName) {
		String sql = "select et.* from experts_teachers et left join users u on u.id=et.teacher_id "
				+ "left join users e on e.id=et.expert_id "
				+ "where e.username=:expertname and u.username=:username";
		RowCountCallbackHandler countCallback = new RowCountCallbackHandler();

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("username", userName);
		parameters.put("expertname", expertName);
		jdbcTemplate.query(sql, parameters, countCallback);
		
		return (countCallback.getRowCount() > 0);

	}
}
