package com.ilearnrw.api.selectnextactivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NextActivities implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> activity;
	private int category, index;

	public NextActivities() {
		this.activity = new ArrayList<String>();
		this.category = -1;
		this.index = -1;
	}
	
	public NextActivities(int category, int index) {
		this.category = category;
		this.index = index;
		this.activity = new ArrayList<String>();
	}
	
	public NextActivities(List<String> activity,  int category, int index) {
		this.category = category;
		this.index = index;
		this.activity = activity;
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
	
}
