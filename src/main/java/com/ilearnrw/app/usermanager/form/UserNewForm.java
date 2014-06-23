package com.ilearnrw.app.usermanager.form;

import javax.validation.Valid;

import com.ilearnrw.common.security.users.model.User;

public class UserNewForm {
	@Valid
	User user;
	boolean teacher;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean getTeacher() {
		return teacher;
	}
	public void setTeacher(boolean isTeacher) {
		this.teacher = isTeacher;
	}
}
