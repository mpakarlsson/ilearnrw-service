package com.ilearnrw.app.usermanager.form;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;

public class RoleForm {

	@Valid
	Role role;
	List<Permission> allPermissions;
	List<Permission> selectedPermissions;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Permission> getAllPermissions() {
		return allPermissions;
	}

	public void setAllPermissions(List<Permission> allPermissions) {
		this.allPermissions = allPermissions;
	}

	public List<Permission> getSelectedPermissions() {
		if (selectedPermissions == null)
			return new LinkedList<Permission>();
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<Permission> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}

}
