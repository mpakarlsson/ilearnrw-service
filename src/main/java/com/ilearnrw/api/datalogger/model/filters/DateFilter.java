package com.ilearnrw.api.datalogger.model.filters;

public class DateFilter {
	public enum DateFilterType {
		ALL, TODAY, WEEK, MONTH, CUSTOM
	}
	DateFilterType type;
	String startDate;
	String endDate;
	
	public DateFilterType getType() {
		return type;
	}

	public void setType(DateFilterType type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}