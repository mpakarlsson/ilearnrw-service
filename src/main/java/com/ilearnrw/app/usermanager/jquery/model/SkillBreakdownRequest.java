package com.ilearnrw.app.usermanager.jquery.model;

public class SkillBreakdownRequest {
	BreakdownFilter filter;
	SkillDataPoint dataPoint;

	public BreakdownFilter getFilter() {
		return filter;
	}

	public void setFilter(BreakdownFilter filter) {
		this.filter = filter;
	}

	public SkillDataPoint getDataPoint() {
		return dataPoint;
	}

	public void setDataPoint(SkillDataPoint dataPoint) {
		this.dataPoint = dataPoint;
	}
}
