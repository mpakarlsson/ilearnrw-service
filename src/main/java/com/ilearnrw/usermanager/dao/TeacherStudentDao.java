package com.ilearnrw.usermanager.dao;

import java.util.List;

import com.ilearnrw.usermanager.model.User;

public interface TeacherStudentDao {
	
	public List<User> getTeacherList();
	
	public List<User> getStudentList();
	
	public List<User> getStudentList(User teacher);
	
	public void setStudentList(User teacher, List<User> students);

}
