package com.ilearnrw.usermanager.services;

import java.util.List;

import com.ilearnrw.usermanager.model.User;

public interface UserService {
	public List<User> getUserList();

	public User getUser(int id);

	public int insertData(User user);

	public void updateData(User user);

	public void deleteData(int id);


}
