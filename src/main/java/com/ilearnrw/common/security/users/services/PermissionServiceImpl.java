package com.ilearnrw.common.security.users.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.PermissionDao;
import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionDao permissionDao;

	@Override
	public List<Permission> getPermissionList() {
		return permissionDao.getPermissionList();
	}

	@Override
	public Permission getPermission(int id) {
		try {
			return permissionDao.getPermission(id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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

	@Override
	public Permission getPermission(String name) {
		try {
			return permissionDao.getPermission(name);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
