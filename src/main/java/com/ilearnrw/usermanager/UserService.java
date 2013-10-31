package com.ilearnrw.usermanager;

import java.util.List;

import com.ilearnrw.usermanager.model.User;

public interface UserService {
	public int insertData(User user);

	public List<User> getUserList();

	public void deleteData(int id);

	public User getUser(int id);

	public void updateData(User user);
}
