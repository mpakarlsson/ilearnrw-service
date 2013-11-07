package com.ilearnrw.usermanager.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.ilearnrw.usermanager.model.User;

public class UserDaoImpl implements UserDao {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	private RoleDao roleDao;

	public int insertData(User user) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
				.withTableName("users").usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("username", user.username);
		parameters.put("password", user.password);
		parameters.put("enabled", user.enabled);
		Number newId = insert.executeAndReturnKey(parameters);

		user.setId(newId.intValue());

		return newId.intValue();
	}

	public List<User> getUserList() {
		List<User> userList = new ArrayList<User>();

		String sql = "select * from users";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return userList;
	}

	@Override
	public void deleteData(int id) {
		String sql = "delete from users where id=" + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql);
	}

	@Override
	public void updateData(User user) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(
				"update users set username=?, password=?, enabled=? where id=?",
				user.username, user.password, user.enabled, user.getId());

	}

	@Override
	public User getUser(int id) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		User user = template.queryForObject("select * from users where id=?",
				new Object[] { id },
				new BeanPropertyRowMapper<User>(User.class));
		return user;
	}

}
