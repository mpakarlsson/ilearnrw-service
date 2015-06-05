package com.ilearnrw.common.security.users.model;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StudentDetails {
	private Integer studentId;

	@Size(min = 2, max = 45)
	private String school;

	@Size(min = 2, max = 45)
	private String classRoom;
	
	private Integer teacherId;

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

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}	
	
}