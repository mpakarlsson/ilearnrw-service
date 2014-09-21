package com.ilearnrw.common.security.users.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.User;

@Repository
public class TeacherStudentDaoImpl implements TeacherStudentDao {

	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;

	@Override
	public List<User> getTeacherList() {
		List<User> teacherList = new ArrayList<User>();

		String sql = "select u.* from users u, role_members rm, roles r "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_TEACHER' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		teacherList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return teacherList;
	}

	@Override
	public List<User> getAllStudentsList() {
		List<User> studentList = new ArrayList<User>();

		String sql = "select u.* from users u, role_members rm, roles r "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_STUDENT' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return studentList;
	}

	@Override
	public List<User> getUnassignedStudentsList() {
		List<User> studentList = new ArrayList<User>();

		String sql = "select u.* from users u, roles r, role_members rm "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_STUDENT' "
				+ "and u.id not in (select ts.student_id from teachers_students ts)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(
				User.class));
		return studentList;
	}

	@Override
	public List<User> getStudentList(User teacher) {
		List<User> studentList = new ArrayList<User>();

		String sql = "select u.* from users u, teachers_students ts "
				+ "where u.id = ts.student_id and ts.teacher_id = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		studentList = jdbcTemplate.query(sql, new Object[] { teacher.getId() },
				new BeanPropertyRowMapper<User>(User.class));
		return studentList;
	}

	@Override
	public void setStudentList(final User teacher, final List<User> students) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from teachers_students where teacher_id=?",
				new Object[] { teacher.getId() });

		String sql = "insert into teachers_students (teacher_id, student_id) values (?,?);";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement pStatement, int index)
					throws SQLException {
				User student = students.get(index);
				pStatement.setInt(1, teacher.getId());
				pStatement.setInt(2, student.getId());
			}

			@Override
			public int getBatchSize() {
				if (students == null)
					return 0;
				return students.size();
			}
		});
	}

	@Override
	public void assignStudentToTeacher(User teacher, User student) {
		String sql = "insert into teachers_students (teacher_id, student_id) values (?,?) "
				+ "on duplicate key update teacher_id = VALUES(teacher_id);";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql,
				new Object[] { teacher.getId(), student.getId() });
	}

	@Override
	public boolean isUserStudentOfTeacher(String userName, String teacherName) {
		String sql = "select ts.* from teachers_students ts left join users u on u.id=ts.student_id "
				+ "left join users t on t.id=ts.teacher_id "
				+ "where t.username=:teachername and u.username=:username";
		RowCountCallbackHandler countCallback = new RowCountCallbackHandler();

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("username", userName);
		parameters.put("teachername", teacherName);
		jdbcTemplate.query(sql, parameters, countCallback);

		return (countCallback.getRowCount() > 0);
	}

	@Override
	public User getTeacherOfStudent(User student) {
		try {
			return new JdbcTemplate(dataSource)
					.queryForObject("select t.id, t.username "
							+ "from users t join teachers_students ts on ts.teacher_id = t.id "
							+ "where ts.student_id = ?",
							new Object[] { student.getId() },
							new RowMapper<User>() {
								@Override
								public User mapRow(ResultSet arg0, int arg1)
										throws SQLException {
									User user = new User();
									user.setId(arg0.getInt("id"));
									user.setUsername(arg0.getString("username"));
									return user;
								}
							});
		} catch (EmptyResultDataAccessException e) {
			User unassigned = new User();
			unassigned.setUsername("Unassigned");
			return unassigned;
		}
	}

}
