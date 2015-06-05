package com.ilearnrw.common.security.users.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.UserDao;
import com.ilearnrw.common.security.users.model.User;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	@Override
	public List<User> getUserList() {
		return userDao.getUserList();
	}

	@Override
	public User getUser(int id) {
		return userDao.getUser(id);
	}

	@Override
	public int insertData(User user) {
		return userDao.insertData(user);
	}

	@Override
	public void updateData(User user) {
		userDao.updateData(user);

	}

	@Override
	public void deleteData(int id) {
		userDao.deleteData(id);
	}

	@Override
	public User getUserByUsername(String username) {
		return userDao.getUserByUsername(username);
	}

	@Override
	public void setPassword(int userId, String password) {
		userDao.setPassword(userId, password);
	}
}
