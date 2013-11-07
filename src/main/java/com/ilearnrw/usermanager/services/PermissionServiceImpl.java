package com.ilearnrw.usermanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ilearnrw.usermanager.dao.PermissionDao;
import com.ilearnrw.usermanager.model.Permission;
import com.ilearnrw.usermanager.model.Role;

public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionDao permissionDao;
	
	@Override
	public List<Permission> getPermissionList() {
		return permissionDao.getPermissionList();
	}

	@Override
	public Permission getPermission(int id) {
		return permissionDao.getPermission(id);
	}

	@Override
	public int insertData(Permission permission) {
		return permissionDao.insertData(permission);
	}

	@Override
	public void updateData(Permission permission) {
		permissionDao.updateData(permission);
	}

	@Override
	public void deleteData(int id) {
		permissionDao.deleteData(id);
	}

	@Override
	public List<Permission> getPermissionList(Role role) {
		return permissionDao.getPermissionList(role);
	}

	@Override
	public void setPermissionList(Role role, List<Permission> permissions) {
		permissionDao.setPermissionList(role, permissions);
	}

}
