package com.ilearnrw.common.security.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.TeacherStudentDao;
import com.ilearnrw.common.security.users.model.User;

@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

	@Autowired
	TeacherStudentDao teacherStudentDao;
	
	@Override
	public List<User> getTeacherList() {
		return teacherStudentDao.getTeacherList();
	}

	@Override
	public List<User> getAllStudentsList() {
		return teacherStudentDao.getAllStudentsList();
	}
	
	@Override
	public List<User> getUnassignedStudentsList() {
		return teacherStudentDao.getUnassignedStudentsList();
	}

	@Override
	public List<User> getStudentList(User teacher) {
		return teacherStudentDao.getStudentList(teacher);
	}

	@Override
	public void setStudentList(User teacher, List<User> students) {
		teacherStudentDao.setStudentList(teacher, students);
	}
	
	@Override
	public void assignStudentToTeacher(User teacher, User student) {
		teacherStudentDao.assignStudentToTeacher(teacher, student);
	}

	@Override
	public boolean isUserStudentOfTeacher(String userName, String teacherName) {
		return teacherStudentDao.isUserStudentOfTeacher(userName, teacherName);
	}

	@Override
	public User getTeacherOfStudent(User student) {
		return teacherStudentDao.getTeacherOfStudent(student);
	}

}
