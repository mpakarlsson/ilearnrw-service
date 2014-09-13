package com.ilearnrw.common.security.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilearnrw.common.security.users.dao.StudentDetailsDao;
import com.ilearnrw.common.security.users.model.StudentDetails;

@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {
	@Autowired
	StudentDetailsDao sdDao;


	@Override
	public StudentDetails getStudentDetails(int id) {
		return sdDao.getStudentDetails(id);
	}

	@Override
	public int insertData(StudentDetails sd) {
		return sdDao.insertData(sd);
	}

	@Override
	public void updateData(StudentDetails sd) {
		sdDao.updateData(sd);
	}

	@Override
	public void deleteData(int id) {
		sdDao.deleteData(id);
		
	}

	@Override
	public List<String> getSchools() {
		return sdDao.getSchools();
	}

	@Override
	public List<String> getClassRooms() {
		return sdDao.getClassRooms();
	}

}
