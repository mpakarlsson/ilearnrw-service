package com.ilearnrw.app.usermanager.form;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;

public class UserRolesForm {
	
	@Valid
	User user;
	@Valid
	Date birthdate;
	List<Role> allRoles;
	List<Role> selectedRoles;

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public List<Role> getSelectedRoles() {
		if (selectedRoles == null)
			return new ArrayList<Role>();
		return selectedRoles;
	}

	public void setSelectedRoles(List<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
}
