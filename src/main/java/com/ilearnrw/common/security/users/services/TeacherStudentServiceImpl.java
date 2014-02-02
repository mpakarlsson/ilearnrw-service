package com.ilearnrw.common.security.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.app.usermanager.model.User;
import com.ilearnrw.common.security.users.dao.TeacherStudentDao;

@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

	@Autowired
	TeacherStudentDao teacherStudentDao;
	
	@Override
	public List<User> getTeacherList() {
		return teacherStudentDao.getTeacherList();
	}

	@Override
	public List<User> getStudentList() {
		return teacherStudentDao.getStudentList();
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
	public boolean isUserStudentOfTeacher(String userName, String teacherName) {
		return teacherStudentDao.isUserStudentOfTeacher(userName, teacherName);
	}


}
