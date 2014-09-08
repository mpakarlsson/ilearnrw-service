package com.ilearnrw.common.security.users.dao;

import java.util.List;

import com.ilearnrw.common.security.users.model.User;

public interface TeacherStudentDao {
	
	public List<User> getTeacherList();
	
	public List<User> getAllStudentsList();
	
	public List<User> getUnassignedStudentsList();
	
	public List<User> getStudentList(User teacher);
	
	public void setStudentList(User teacher, List<User> students);
	
	public void assignStudentToTeacher(User teacher, User student);

	public boolean isUserStudentOfTeacher(String userName, String teacherName);

}
