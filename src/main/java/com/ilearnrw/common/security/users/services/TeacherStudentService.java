package com.ilearnrw.common.security.users.services;

import java.util.List;

import com.ilearnrw.common.security.users.model.User;

public interface TeacherStudentService {

	public List<User> getTeacherList();
	
	public List<User> getStudentList();
	
	public List<User> getStudentList(User teacher);
	
	public void setStudentList(User teacher, List<User> students);
	
	public boolean isUserStudentOfTeacher(String userName, String teacherName);

}
