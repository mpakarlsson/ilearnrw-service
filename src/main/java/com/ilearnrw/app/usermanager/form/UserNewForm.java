package com.ilearnrw.app.usermanager.form;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;

public class UserNewForm {
	@Valid
	User user;
	@Valid
	@NotEmpty
	String role;
	
	StudentDetails studentDetails;
	
	
	public UserNewForm() {
	}

	public UserNewForm(User user, String role, StudentDetails sd) {
		super();
		this.user = user;
		this.role = role;
		this.studentDetails = sd;
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
	public StudentDetails getStudentDetails() {
		return studentDetails;
	}
	public void setStudentDetails(StudentDetails studentDetails) {
		this.studentDetails = studentDetails;
	}
}
