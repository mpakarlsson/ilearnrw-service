package com.ilearnrw.common.security.users.dao;

import java.util.List;

import com.ilearnrw.app.usermanager.model.User;

public interface TeacherStudentDao {
	
	public List<User> getTeacherList();
	
	public List<User> getStudentList();
	
	public List<User> getStudentList(User teacher);
	
	public void setStudentList(User teacher, List<User> students);

	public boolean isUserStudentOfTeacher(String userName, String teacherName);

}
