package com.ilearnrw.common.security;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
public class RefreshTokenData {
	private String userName;
	private String password;
	private String teacherUser;
	private String teacherPass;

	public RefreshTokenData() {
		this.userName = "";
		this.password = "";
		this.teacherUser = "";
		this.teacherPass = "";
	}

	public RefreshTokenData(String userName, String password, String teacherUser, String teacherPass) {
		this.setUserName(userName);
		this.setPassword(password);
		this.setTeacherUser(teacherUser);
		this.setTeacherPass(teacherPass);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getTeacherUser() {
		return teacherUser;
	}

	public void setTeacherUser(String teacherUser) {
		this.teacherUser = teacherUser;
	}

	public String getTeacherPass() {
		return teacherPass;
	}

	public void setTeacherPass(String teacherPass) {
		this.teacherPass = teacherPass;
	}

}
