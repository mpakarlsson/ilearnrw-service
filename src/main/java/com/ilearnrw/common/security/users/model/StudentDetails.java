package com.ilearnrw.common.security.users.model;

import javax.validation.constraints.Size;

public class StudentDetails {
	private Integer studentId;

	@Size(min = 2, max = 45)
	private String school;

	@Size(min = 2, max = 45)
	private String classRoom;

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}	
	
}