package com.ilearnrw.api.selectnextactivity;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NextActivities implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> activity;
	private List<String> level;
	private int category, index;

	public NextActivities() {
		this.activity = new ArrayList<String>();
		this.category = -1;
		this.index = -1;
		this.level = new ArrayList<String>();
	}
	
	public NextActivities(int category, int index) {
		this.category = category;
		this.index = index;
		this.activity = new ArrayList<String>();
		this.level = new ArrayList<String>();

	}
	
	public NextActivities(List<String> activity,  int category, int index) {
		this.category = category;
		this.index = index;
		this.activity = activity;
		this.level = new ArrayList<String>();
	}

	public NextActivities(List<String> activity,  int category, int index, List<String> level) {
		this.category = category;
		this.index = index;
		this.activity = activity;
		this.level = level;
	}
	
	
	public void setProblem(int category, int index) {
		this.category = category;
		this.index = index;
	}
	
	public void addActivity(String app){
		this.activity.add(app);
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<String> getActivity() {
		return activity;
	}

	public void setActivity(List<String> activity) {
		this.activity = activity;
	}
	
	public void setLevel(List<String> level){
		this.level = level;
	}
	
	public List<String> getLevel(){
		return level;
	}
}
