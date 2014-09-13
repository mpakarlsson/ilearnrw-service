package com.ilearnrw.common.security.users.dao;


import java.util.List;

import com.ilearnrw.common.security.users.model.StudentDetails;

public interface StudentDetailsDao {

	public StudentDetails getStudentDetails(int id);

	public int insertData(StudentDetails user);

	public void updateData(StudentDetails user);

	public void deleteData(int id);
	
	public List<String> getSchools();
	public List<String> getClassRooms();
}
