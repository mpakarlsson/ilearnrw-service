package com.ilearnrw.common.security.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.SchoolDao;
import com.ilearnrw.common.security.users.dao.TeacherStudentDao;
import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;

@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {
	@Autowired
	SchoolDao schoolDao;

	@Autowired
	TeacherStudentDao teacherStudentDao;

	@Override
	public StudentDetails getStudentDetails(User student) {
		StudentDetails studentDetails = new StudentDetails();
		studentDetails.setStudentId(student.getId());
		Classroom classroom = schoolDao.getStudentClassroom(student);
		studentDetails.setClassRoom(classroom.getName());
		School school = schoolDao.getClassroomSchool(classroom);
		studentDetails.setSchool(school.getName());
		User teacher = teacherStudentDao.getTeacherOfStudent(student);
		studentDetails.setTeacherId(teacher.getId());
		return studentDetails;
	}

	@Override
	public int insertData(StudentDetails sd) {
		User student = new User();
		student.setId(sd.getStudentId());
		School school = schoolDao.getSchool(sd.getSchool());
		if (school == null) {
			school = new School();
			school.setName(sd.getSchool());
			schoolDao.addSchool(school);
		}
		Classroom classroom = schoolDao.getClassroom(sd.getClassRoom());
		if (classroom == null) {
			classroom = new Classroom();
			classroom.setName(sd.getClassRoom());
			schoolDao.addClassroomToSchool(school, classroom);
		}
		schoolDao.assignStudentToClassroom(student, classroom);
		User teacher = new User();
		teacher.setId(sd.getTeacherId());
		teacherStudentDao.assignStudentToTeacher(teacher, student);
		return sd.getStudentId();
	}

	@Override
	public void updateData(StudentDetails sd) {
		insertData(sd);
	}

	@Override
	public void deleteData(int id) {
		// TODO: implement
	}

	@Override
	public List<School> getSchools() {
		return schoolDao.getListOfSchools();
	}

	@Override
	public List<Classroom> getClassRooms(School school) {
		return schoolDao.getClassroomsBySchool(school);
	}

	@Override
	public List<User> getStudentsFromClassRoom(Classroom classroom) {
		return schoolDao.getStudentsFromClassRoom(classroom);
	}
	
	@Override
	public Classroom getStudentClassroom(User student) {
		return schoolDao.getStudentClassroom(student);
	}
	
	@Override
	public School getStudentSchool(User student) {
		return schoolDao.getClassroomSchool(schoolDao.getStudentClassroom(student));
	}
}
