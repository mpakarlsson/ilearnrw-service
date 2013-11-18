package com.ilearnrw.usermanager.dao;

import java.util.List;

import com.ilearnrw.usermanager.model.Permission;
import com.ilearnrw.usermanager.model.Role;

public interface PermissionDao {

	public List<Permission> getPermissionList();

	public Permission getPermission(int id);

	public int insertData(Permission permission);

	public void updateData(Permission permission);

	public void deleteData(int id);
	
	public List<Permission> getPermissionList(Role role);

	public void setPermissionList(Role role, List<Permission> permissions);
}
