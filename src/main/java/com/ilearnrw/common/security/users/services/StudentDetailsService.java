package com.ilearnrw.common.security.users.services;

import java.util.List;

import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;

public interface StudentDetailsService {
	public StudentDetails getStudentDetails(int id);

	public int insertData(StudentDetails user);

	public void updateData(StudentDetails user);

	public void deleteData(int id);

	public List<String> getSchools();

	public List<String> getClassRooms();

}
