package com.ilearnrw.common.security.users.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.User;

@Repository
public class SchoolDaoImpl implements SchoolDao {

	@Autowired
	@Qualifier("usersDataSource")
	DataSource dataSource;

	@Override
	public int addSchool(School school) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
				.withTableName("schools").usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("name", school.getName());
		Number newId = insert.executeAndReturnKey(parameters);

		school.setId(newId.intValue());

		return newId.intValue();
	}

	@Override
	public School getSchool(String name) {
		try {
			return new JdbcTemplate(dataSource).queryForObject(
					"select id, name from schools where name = ?",
					new Object[] { name }, new BeanPropertyRowMapper<School>(
							School.class));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int addClassroomToSchool(School school, Classroom classroom) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
				.withTableName("classrooms").usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("school_id", school.getId());
		parameters.put("name", classroom.getName());
		Number newId = insert.executeAndReturnKey(parameters);

		classroom.setId(newId.intValue());

		return newId.intValue();
	}
	
	@Override
	public Classroom getClassroom(String name) {
		try {
			return new JdbcTemplate(dataSource).queryForObject(
					"select id, school_id, name from schools where name = ?",
					new Object[] { name }, new BeanPropertyRowMapper<Classroom>(
							Classroom.class));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void assignStudentToClassroom(User student, Classroom classroom) {
		new JdbcTemplate(dataSource)
				.update("INSERT INTO student_classrooms (student_id, classroom_id) "
						+ "VALUES (?, ?) "
						+ "ON DUPLICATE KEY UPDATE classroom_id = VALUES(classroom_id)",
						student.getId(), classroom.getId());
	}

	@Override
	public List<School> getListOfSchools() {
		return new JdbcTemplate(dataSource).query(
				"select id, name from schools",
				new BeanPropertyRowMapper<School>(School.class));
	}

	@Override
	public List<Classroom> getClassroomsBySchool(School school) {
		return new JdbcTemplate(dataSource).query(
				"select classroom_id, classroom_name "
						+ "from schools_classrooms where school_id=?",
				new Object[] { school.getId() },
				new BeanPropertyRowMapper<Classroom>(Classroom.class));
	}

	@Override
	public Classroom getStudentClassroom(User student) {
		try {
			return new JdbcTemplate(dataSource).queryForObject(
					"select c.id, c.name from student_classrooms sc "
							+ "join classrooms c on c.id = classroom_id "
							+ "where sc.student_id = ?",
					new Object[] { student.getId() },
					new BeanPropertyRowMapper<Classroom>(Classroom.class));
		} catch (EmptyResultDataAccessException e) {
			Classroom unassigned = new Classroom();
			unassigned.setName("Unassigned");
			return unassigned;
		}
	}

	@Override
	public School getClassroomSchool(Classroom classroom) {
		try {
			return new JdbcTemplate(dataSource).queryForObject(
					"select s.id, s.name from classrooms c "
							+ "join schools s on c.school_id = s.id "
							+ "where c.id = ?",
					new Object[] { classroom.getId() },
					new BeanPropertyRowMapper<School>(School.class));
		} catch (EmptyResultDataAccessException e) {
			School unassigned = new School();
			unassigned.setName("Unassigned");
			return unassigned;
		}
	}

	@Override
	public List<User> getStudentsFromClassRoom(Classroom classroom) {
		return new JdbcTemplate(dataSource)
				.query("select id, username "
						+ "from users s join student_classrooms sc on sc.student_id = s.id "
						+ "where sc.classroom_id=?",
						new Object[] { classroom.getId() },
						new BeanPropertyRowMapper<User>(User.class));
	}

}
