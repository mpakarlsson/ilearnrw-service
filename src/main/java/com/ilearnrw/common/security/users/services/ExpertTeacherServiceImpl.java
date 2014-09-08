package com.ilearnrw.common.security.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.ExpertTeacherDao;
import com.ilearnrw.common.security.users.model.User;

@Service
public class ExpertTeacherServiceImpl implements ExpertTeacherService {
	
	@Autowired
	ExpertTeacherDao expertTeacherDao;
	
	@Override
	public List<User> getExpertsList() {
		return expertTeacherDao.getExpertsList();
	}

	@Override
	public List<User> getAllTeachersList() {
		return expertTeacherDao.getAllTeachersList();
	}
	
	@Override
	public List<User> getUnassignedTeachersList() {
		return expertTeacherDao.getUnassignedTeachersList();
	}

	@Override
	public List<User> getTeacherList(User expert) {
		return expertTeacherDao.getTeacherList(expert);
	}

	@Override
	public void setTeacherList(User expert, List<User> teachers) {
		expertTeacherDao.setTeacherList(expert, teachers);;
	}
	
	@Override
	public void assignTeacherToExpert(User expert, User teacher) {
		expertTeacherDao.assignTeacherToExpert(expert, teacher);
	}

	@Override
	public boolean isUserStudentOfTeacher(String userName, String expertName) {
		return expertTeacherDao.isUserAssignedToExpert(userName, expertName);
	}

}
