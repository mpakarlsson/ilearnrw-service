package com.ilearnrw.api.selectnextactivity;

import java.util.List;

import ilearnrw.user.problems.ProblemDescription;

public class NextActivities {

	private ProblemDescription problemDescription;
	private int problemSeverity;
	private List<String> activities;
	
	
	public ProblemDescription getProblemDescription() {
		return problemDescription;
	}
	public void setProblemDescription(ProblemDescription problemDescription) {
		this.problemDescription = problemDescription;
	}
	public int getProblemSeverity() {
		return problemSeverity;
	}
	public void setProblemSeverity(int problemSeverity) {
		this.problemSeverity = problemSeverity;
	}
	public List<String> getActivities() {
		return activities;
	}
	public void setActivities(List<String> activities) {
		this.activities = activities;
	}
	
}
