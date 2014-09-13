package com.ilearnrw.common.security.users.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;

@Repository
public class StudentDetailsDaoImpl implements StudentDetailsDao {

	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;

	@Override
	public void deleteData(int id) {
		String sql = "delete from student_details where student_id=" + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql);
	}

	@Override
	public StudentDetails getStudentDetails(int id) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		StudentDetails sd = null;
		try {
			sd = template.queryForObject("select * from student_details where student_id=?",
				new Object[] { id },
				new BeanPropertyRowMapper<StudentDetails>(StudentDetails.class));
		}
		catch(EmptyResultDataAccessException e){
			// in case old users don't have these details
			sd = new StudentDetails();
			sd.setStudentId(id);
		}
		return sd;
	}

	@Override
	public int insertData(StudentDetails sd) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
		.withTableName("student_details");

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("student_id", sd.getStudentId());
		parameters.put("school", sd.getSchool());
		parameters.put("classroom", sd.getClassRoom());

		insert.execute(parameters);

		return sd.getStudentId();
	}

	@Override
	public void updateData(StudentDetails sd) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(
				"update student_details set school=?, classroom=? where student_id=?",
				sd.getSchool(), sd.getClassRoom(), sd.getStudentId());

	}

	@Override
	public List<String> getSchools() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> rooms = template.queryForList("select distinct school from student_details",
				String.class);
		
		return rooms;
	}

	@Override
	public List<String> getClassRooms() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> rooms = template.queryForList("select distinct classroom from student_details",
				String.class);
		
		return rooms;

	}

}
