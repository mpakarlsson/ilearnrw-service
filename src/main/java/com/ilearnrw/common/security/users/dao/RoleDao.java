package com.ilearnrw.common.security.users.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;
import java.util.Map;

import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;

public interface RoleDao {

	public List<Role> getRoleList();

	public Role getRole(int id);

	public int insertData(Role role);

	public void updateData(Role role);

	public void deleteData(int id);

	public List<Role> getRoleList(User user);

	public void setRoleList(User user, List<Role> roles);

	public Role getRole(String roleName);

	public Map<String, String> getUsersWithRole(String role);
}
