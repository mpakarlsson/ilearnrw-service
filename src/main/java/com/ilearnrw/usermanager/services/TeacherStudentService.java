package com.ilearnrw.usermanager.services;

import java.util.List;

import com.ilearnrw.usermanager.model.User;

public interface TeacherStudentService {

	public List<User> getTeacherList();
	
	public List<User> getStudentList();
	
	public List<User> getStudentList(User teacher);
	
	public void setStudentList(User teacher, List<User> students);

}
