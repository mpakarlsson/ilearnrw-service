package com.ilearnrw.app.usermanager.form;

import com.ilearnrw.common.security.users.model.User;

public class UserRole {
	
	String role;
	User user;
	
	public UserRole(User user, String role) {
		this.user = user;
		this.role = role;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
