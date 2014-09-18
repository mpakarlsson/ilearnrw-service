package com.ilearnrw.common.security.users.services;

import java.util.List;

import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;

public interface StudentDetailsService {
	public StudentDetails getStudentDetails(User student);

	public int insertData(StudentDetails user);

	public void updateData(StudentDetails user);

	public void deleteData(int id);

	public List<Classroom> getClassRooms(School school);

	public List<School> getSchools();

	public List<User> getStudentsFromClassRoom(Classroom classroom);

}
