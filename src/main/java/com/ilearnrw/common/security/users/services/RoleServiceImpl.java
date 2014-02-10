package com.ilearnrw.common.security.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		return roleDao.getRole(id);
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
		return roleDao.getRole(roleName);
	}

}
