package com.ilearnrw.api.datalogger.model.result;

public class BreakdownResult {
	String timeSpent = "0";
	int correctAnswers = 0;
	int incorrectAnswers = 0;
	int totalAnswers = 0;
	String successRate = "-";

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
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
