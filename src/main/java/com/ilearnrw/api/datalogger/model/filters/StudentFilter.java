package com.ilearnrw.api.datalogger.model.filters;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
public class StudentFilter {
	public enum StudentFilterType {
		ALL, SCHOOL, CLASSROOM, STUDENT;
	}

	StudentFilterType type;
	String name;

	public StudentFilterType getType() {
		return type;
	}

	public void setType(StudentFilterType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
