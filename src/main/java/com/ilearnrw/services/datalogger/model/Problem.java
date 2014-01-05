package com.ilearnrw.services.datalogger.model;

import java.io.Serializable;

public class Problem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int category;
	private int idx;
	private String description;

	public Problem(int id, int category, int index, String description) {
		super();
		this.id = id;
		this.category = category;
		this.idx = index;
		this.description = description;
	}
	
	public Problem() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}