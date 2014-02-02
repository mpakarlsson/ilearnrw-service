package com.ilearnrw.common.security.users.dao;

import java.util.List;

import com.ilearnrw.app.usermanager.model.Role;
import com.ilearnrw.app.usermanager.model.User;

public interface RoleDao {

	public List<Role> getRoleList();

	public Role getRole(int id);

	public int insertData(Role role);

	public void updateData(Role role);

	public void deleteData(int id);

	public List<Role> getRoleList(User user);

	public void setRoleList(User user, List<Role> roles);
}
