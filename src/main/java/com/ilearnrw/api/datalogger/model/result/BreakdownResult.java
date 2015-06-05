package com.ilearnrw.api.datalogger.model.result;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
public class BreakdownResult {
	String timeSpent = "0";
	int correctAnswers = 0;
	int incorrectAnswers = 0;
	int totalAnswers = 0;
	String successRate = "-";
	int nrOfApps = 0;

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

	public int getNrOfApps() {
		return nrOfApps;
	}

	public void setNrOfApps(int nrOfApps) {
		this.nrOfApps = nrOfApps;
	}
}
