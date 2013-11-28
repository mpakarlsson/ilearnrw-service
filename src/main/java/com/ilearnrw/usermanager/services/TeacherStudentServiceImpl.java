package com.ilearnrw.usermanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ilearnrw.usermanager.dao.TeacherStudentDao;
import com.ilearnrw.usermanager.model.User;

@Component
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

}
