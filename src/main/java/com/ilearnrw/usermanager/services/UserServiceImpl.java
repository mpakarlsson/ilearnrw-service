package com.ilearnrw.usermanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ilearnrw.usermanager.dao.UserDao;
import com.ilearnrw.usermanager.model.User;

@Component
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

}
