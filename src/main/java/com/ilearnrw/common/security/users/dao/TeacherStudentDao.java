package com.ilearnrw.common.security.users.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
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

	public User getTeacherOfStudent(User student);

}
