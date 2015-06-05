package com.ilearnrw.common.security.users.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;

public interface PermissionService {

	List<Permission> getPermissionList();
	
	public List<Permission> getPermissionList(Role role);

	public Permission getPermission(int id);

	public int insertData(Permission permission);

	public void updateData(Permission permission);

	public void deleteData(int id);
	
	public void setPermissionList(Role role, List<Permission> permissions);

	public Permission getPermission(String name);

}
