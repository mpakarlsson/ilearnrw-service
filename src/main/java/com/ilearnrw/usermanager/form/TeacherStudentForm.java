package com.ilearnrw.usermanager.form;

import java.util.LinkedList;
import java.util.List;

import com.ilearnrw.usermanager.model.User;

public class TeacherStudentForm {

	User teacher;
	List<User> allStudents;
	List<User> selectedStudents;

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public List<User> getAllStudents() {
		return allStudents;
	}

	public void setAllStudents(List<User> allStudents) {
		this.allStudents = allStudents;
	}

	public List<User> getSelectedStudents() {
		if (selectedStudents == null)
			return new LinkedList<User>();
		return selectedStudents;
	}

	public void setSelectedStudents(List<User> selectedStudents) {
		this.selectedStudents = selectedStudents;
	}

}
