package com.ilearnrw.app.usermanager.form;

import java.util.List;

import com.ilearnrw.common.security.users.model.User;

public class ExpertTeacherForm {

	User expert;
	List<User> allTeachers;
	List<User> selectedTeachers;
	
	public User getExpert() {
		return expert;
	}
	public void setExpert(User expert) {
		this.expert = expert;
	}
	public List<User> getAllTeachers() {
		return allTeachers;
	}
	public void setAllTeachers(List<User> allTeachers) {
		this.allTeachers = allTeachers;
	}
	public List<User> getSelectedTeachers() {
		return selectedTeachers;
	}
	public void setSelectedTeachers(List<User> selectedTeachers) {
		this.selectedTeachers = selectedTeachers;
	}

}
