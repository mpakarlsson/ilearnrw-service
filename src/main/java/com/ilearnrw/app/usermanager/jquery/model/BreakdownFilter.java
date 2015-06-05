package com.ilearnrw.app.usermanager.jquery.model;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.User;

public class BreakdownFilter {
	int dateType;
	String startDate;
	String endDate;
	School school;
	Classroom classroom;
	User student;

	/**
	 * 0 - All 
	 * 1 - Today 
	 * 2 - This week 
	 * 3 - This month 
	 * 4 - Custom
	 */
	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}
}
