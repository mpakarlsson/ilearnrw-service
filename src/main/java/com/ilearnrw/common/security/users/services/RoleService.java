package com.ilearnrw.common.security.users.services;

import java.util.List;

import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;

public interface RoleService {
	
	public List<Role> getRoleList();
	
	public Role getRole(int id);

	public int insertData(Role role);

	public void updateData(Role role);

	public void deleteData(int id);

	public List<Role> getRoleList(User user);

	public void setRoleList(User user, List<Role> roles);
	
}
