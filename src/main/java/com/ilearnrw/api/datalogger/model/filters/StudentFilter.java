package com.ilearnrw.api.datalogger.model.filters;

public class StudentFilter {
	public enum Type {
		ALL, SCHOOL, CLASSROOM, STUDENT;
	}

	Type type;
	String name;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
