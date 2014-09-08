package com.ilearnrw.common.security.users.services;

import java.util.List;

import com.ilearnrw.common.security.users.model.User;

public interface ExpertTeacherService {

	public abstract List<User> getExpertsList();

	public abstract List<User> getAllTeachersList();

	public abstract List<User> getUnassignedTeachersList();

	public abstract List<User> getTeacherList(User expert);

	public abstract void setTeacherList(User expert, List<User> teachers);
	
	public abstract void assignTeacherToExpert(User expert, User teacher);

	public abstract boolean isUserStudentOfTeacher(String userName,
			String expertName);

}