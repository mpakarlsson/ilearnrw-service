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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;

@Repository
public class RoleDaoImpl implements RoleDao {

	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;

	@Override
	public List<Role> getRoleList(User user) {
		List<Role> roleList = new ArrayList<Role>();

		String sql = "select r.id, r.name " + "from roles r, role_members rm "
				+ "where r.id = rm.roles_id and rm.members_id = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		roleList = jdbcTemplate.query(sql, new Object[] { user.getId() },
				new BeanPropertyRowMapper<Role>(Role.class));
		return roleList;
	}

	@Override
	public Role getRole(int id) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Role role = template.queryForObject("select * from roles where id=?",
				new Object[] { id },
				new BeanPropertyRowMapper<Role>(Role.class));
		return role;
	}

	@Override
	public Role getRole(String roleName) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Role role = template.queryForObject("select * from roles where name=?",
				new Object[] { roleName },
				new BeanPropertyRowMapper<Role>(Role.class));
		return role;
	}

	@Override
	public int insertData(Role role) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		return template.update("insert into roles (name) values (?)", role.getName());
	}

	@Override
	public void updateData(Role role) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update("update roles set name=? where id=?", role.getName(), role.getId());
	}

	@Override
	public void deleteData(int id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from roles where id=?", new Object[] { id } );
	}

	@Override
	public List<Role> getRoleList() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from roles", new BeanPropertyRowMapper<Role>(Role.class));
	}

	@Override
	public void setRoleList(final User user, final List<Role> roles) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from role_members where members_id=?", new Object[] { user.getId() });
		
		String sql = "insert into role_members (members_id, roles_id) values (?,?);";
		jdbcTemplate.batchUpdate(sql , new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pStatement, int index) throws SQLException {
				Role role = roles.get(index);
				pStatement.setInt(1, user.getId());
				pStatement.setInt(2, role.getId());
			}
			
			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});
	}

	@Override
	public Map<String, String> getUsersWithRole(String role) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
			"select u.id, u.username from " + 
			"users u " +  
			"left join role_members rm on u.id=rm.members_id " +
			"left join roles r on rm.roles_id=r.id " +
			"where r.name='" + role + "'");
		Map<String, String> result = new HashMap<String, String>();
		for(Map<String, Object> row : rows) {
			result.put(row.get("id").toString(), (String)row.get("username"));
		}
		return result;
	}

}
