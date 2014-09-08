package com.ilearnrw.app.usermanager.form;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.ilearnrw.common.security.users.model.User;

public class UserNewForm {
	@Valid
	User user;
	@Valid
	Birthdate birthdate;
	@Valid
	@NotEmpty
	String role;
	
	public Birthdate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Birthdate birthdate) {
		this.birthdate = birthdate;
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
