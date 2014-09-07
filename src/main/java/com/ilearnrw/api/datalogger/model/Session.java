package com.ilearnrw.api.datalogger.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Session implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private char sessionType;
	private String name;
	private Timestamp start;
	private String username; 
	private String supervisor; 
	
	public Session(int id, char sessionType, String name, Timestamp start, String username, String supervisor) {
		super();
		this.id = id;
		this.sessionType = sessionType;
		this.name = name;
		this.start = start;
		this.username = username;
		this.setSupervisor(supervisor);
	}
	
	public Session() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public char getSessionType() {
		return sessionType;
	}
	public void setSessionType(char sessionType) {
		this.sessionType = sessionType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	
	
}