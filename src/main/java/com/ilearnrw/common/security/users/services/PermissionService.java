package com.ilearnrw.common.security.users.services;

import java.util.List;

import com.ilearnrw.app.usermanager.model.Permission;
import com.ilearnrw.app.usermanager.model.Role;

public interface PermissionService {

	List<Permission> getPermissionList();
	
	public List<Permission> getPermissionList(Role role);

	public Permission getPermission(int id);

	public int insertData(Permission permission);

	public void updateData(Permission permission);

	public void deleteData(int id);
	
	public void setPermissionList(Role role, List<Permission> permissions);

}
