package com.ilearnrw.common.security;

public class AuthTokenData {
	private String userName;
	private String teacher;

	public AuthTokenData() {
		this.userName = "";
		this.teacher = "";
	}

	public AuthTokenData(String userName, String teacher) {
		this.setUserName(userName);
		this.setTeacher(teacher);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
}
