package com.ilearnrw.common.security.users.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

import com.ilearnrw.common.security.users.model.User;

public interface UserService {
	public List<User> getUserList();

	public User getUser(int id);

	public int insertData(User user);

	public void updateData(User user);

	public void deleteData(int id);

	public User getUserByUsername(String username);
	
	public void setPassword(int userId, String password);


}
