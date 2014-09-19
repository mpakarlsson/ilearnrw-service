package com.ilearnrw.common.security.users.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ilearnrw.common.security.users.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;
	
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public int insertData(User user) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
				.withTableName("users").usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("username", user.getUsername());
		parameters.put("password", passwordEncoder.encode(user.getPassword()));
		parameters.put("enabled", user.isEnabled());
		parameters.put("birthdate", user.getBirthdate());
		parameters.put("gender", user.getGender());
		parameters.put("language", user.getLanguage());
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
				"update users set username=?, enabled=?, birthdate=?, gender=?, language=? where id=?",
				user.getUsername(), user.isEnabled(), user.getBirthdate(), user.getGender(), user.getLanguage(), user.getId());

	}

	@Override
	public void setPassword(int userId, String password) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(
				"update users set password=? where id=?",
				passwordEncoder.encode(password), userId);

	}

	@Override
	public User getUser(int id) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		User user = template.queryForObject("select * from users where id=?",
				new Object[] { id },
				new BeanPropertyRowMapper<User>(User.class));
		return user;
	}

	@Override
	public User getUserByUsername(String username) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		User user;
		try {
			user = template.queryForObject("select * from users where username=?",
					new Object[] { username },
					new BeanPropertyRowMapper<User>(User.class));
		} catch (DataAccessException e) {
			user = null;
		}
		return user;
	}

}
