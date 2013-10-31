package com.ilearnrw.usermanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ilearnrw.usermanager.model.User;

public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userdao;

	@Override
	public int insertData(User user) {
		return userdao.insertData(user);
	}

	@Override
	public List<User> getUserList() {
		return userdao.getUserList();
	}

	@Override
	public void deleteData(int id) {
		userdao.deleteData(id);

	}

	@Override
	public User getUser(int id) {
		return userdao.getUser(id);
	}

	@Override
	public void updateData(User user) {
		userdao.updateData(user);

	}
}
