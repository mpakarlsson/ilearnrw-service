package com.ilearnrw.api.datalogger.model.result;

public class ReaderComparisonResult {
	String school;
	String classroom;
	String username;
	String timeSpentReading;
	String daysRead;
	String textsRead;
	String settingsUsed;
	String textToSpeechUsed;

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTimeSpentReading() {
		return timeSpentReading;
	}

	public void setTimeSpentReading(String timeSpentReading) {
		this.timeSpentReading = timeSpentReading;
	}

	public String getDaysRead() {
		return daysRead;
	}

	public void setDaysRead(String daysRead) {
		this.daysRead = daysRead;
	}

	public String getTextsRead() {
		return textsRead;
	}

	public void setTextsRead(String textsRead) {
		this.textsRead = textsRead;
	}

	public String getSettingsUsed() {
		return settingsUsed;
	}

	public void setSettingsUsed(String settingsUsed) {
		this.settingsUsed = settingsUsed;
	}

	public String getTextToSpeechUsed() {
		return textToSpeechUsed;
	}

	public void setTextToSpeechUsed(String textToSpeechUsed) {
		this.textToSpeechUsed = textToSpeechUsed;
	}

}
