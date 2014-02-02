package com.ilearnrw.app.usermanager.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;

public class UserForm {
	
	@Valid
	User user;
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
}
