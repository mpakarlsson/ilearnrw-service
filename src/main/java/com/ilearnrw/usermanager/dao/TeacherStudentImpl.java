package com.ilearnrw.usermanager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ilearnrw.usermanager.model.User;

@Component
public class TeacherStudentImpl implements TeacherStudentDao {
	
	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;
	
	@Override
	public List<User> getTeacherList() {
		List<User> teacherList = new ArrayList<User>();

		String sql = "select u.id, u.username from users u, role_members rm, roles r "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_TEACHER' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		teacherList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
		return teacherList;
	}
	
	@Override
	public List<User> getStudentList() {
		List<User> studentList = new ArrayList<User>();

		String sql = "select u.id, u.username from users u, role_members rm, roles r "
				+ "where u.id = rm.members_id and rm.roles_id = r.id and r.name = 'ROLE_STUDENT' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
		return studentList;
	}
	
	@Override
	public List<User> getStudentList(User teacher) {
		List<User> studentList = new ArrayList<User>();

		String sql = "select u.id, u.username from users u, teachers_students ts "
				+ "where u.id = ts.student_id and ts.teacher_id = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		studentList = jdbcTemplate.query(sql, new Object[] { teacher.getId() },
				new BeanPropertyRowMapper<User>(User.class));
		return studentList;
	}

	@Override
	public void setStudentList(final User teacher, final List<User> students) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from teachers_students where teacher_id=?", new Object[] { teacher.getId() });
		
		String sql = "insert into teachers_students (teacher_id, student_id) values (?,?);";
		jdbcTemplate.batchUpdate(sql , new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pStatement, int index) throws SQLException {
				User student = students.get(index);
				pStatement.setInt(1, teacher.getId());
				pStatement.setInt(2, student.getId());
			}
			
			@Override
			public int getBatchSize() {
				return students.size();
			}
		});
	}

}
