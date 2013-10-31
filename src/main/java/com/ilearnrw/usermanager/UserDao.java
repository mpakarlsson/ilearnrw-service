package com.ilearnrw.usermanager;

import java.util.List;

import com.ilearnrw.usermanager.model.User;

public interface UserDao {
	public int insertData(User user);

	public List<User> getUserList();

	public void updateData(User user);

	public void deleteData(int id);

	public User getUser(int id);
}
