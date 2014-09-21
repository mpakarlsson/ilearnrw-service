package com.ilearnrw.api.datalogger.model.result;

import java.util.ArrayList;
import java.util.List;

public class OverviewBreakdownResult {
	List<String> skillsWorkedOn = new ArrayList<String>();
	String timeSpent = "0";
	int numberOfActivities = 0;
	int correctAnswers = 0;
	int incorrectAnswers = 0;
	int totalAnswers = 0;
	String successRate = "-";

	public List<String> getSkillsWorkedOn() {
		return skillsWorkedOn;
	}

	public void setSkillsWorkedOn(List<String> skillsWorkedOn) {
		this.skillsWorkedOn = skillsWorkedOn;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public int getNumberOfActivities() {
		return numberOfActivities;
	}

	public void setNumberOfActivities(int numberOfActivities) {
		this.numberOfActivities = numberOfActivities;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public int getIncorrectAnswers() {
		return incorrectAnswers;
	}

	public void setIncorrectAnswers(int incorrectAnswers) {
		this.incorrectAnswers = incorrectAnswers;
	}

	public int getTotalAnswers() {
		return totalAnswers;
	}

	public void setTotalAnswers(int totalAnswers) {
		this.totalAnswers = totalAnswers;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}
}
