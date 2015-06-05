package com.ilearnrw.common.security.users.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.RoleDao;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;
	
	@Override
	public List<Role> getRoleList() {
		return roleDao.getRoleList();
	}

	@Override
	public Role getRole(int id) {
		try {
			return roleDao.getRole(id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public int insertData(Role role) {
		return roleDao.insertData(role);
	}

	@Override
	public void updateData(Role role) {
		roleDao.updateData(role);
	}

	@Override
	public void deleteData(int id) {
		roleDao.deleteData(id);
	}
	
	@Override
	public List<Role> getRoleList(User user) {
		return roleDao.getRoleList(user);
	}

	@Override
	public void setRoleList(User user, List<Role> roles) {
		roleDao.setRoleList(user, roles);
	}

	@Override
	public Role getRole(String roleName) {
		try {
			return roleDao.getRole(roleName);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Map<String, String> getUsersWithRole(String role) {
		return roleDao.getUsersWithRole(role);
	}

}
