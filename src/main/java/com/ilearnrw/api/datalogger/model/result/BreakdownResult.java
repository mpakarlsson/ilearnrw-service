package com.ilearnrw.api.datalogger.model.result;

public class BreakdownResult {
	String timeSpent;
	String correctAnswers;
	String incorrectAnswers;
	String successRate;

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(String correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public String getIncorrectAnswers() {
		return incorrectAnswers;
	}

	public void setIncorrectAnswers(String incorrectAnswers) {
		this.incorrectAnswers = incorrectAnswers;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}
}
