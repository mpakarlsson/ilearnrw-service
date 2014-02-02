package com.ilearnrw.common.security.users.dao;

import java.util.List;

import com.ilearnrw.app.usermanager.model.User;

public interface UserDao {

	public List<User> getUserList();

	public User getUser(int id);

	public int insertData(User user);

	public void updateData(User user);

	public void deleteData(int id);

	public User getUserByUsername(String username);
}
