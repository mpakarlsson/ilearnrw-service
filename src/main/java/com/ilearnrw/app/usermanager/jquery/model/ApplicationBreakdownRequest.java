package com.ilearnrw.app.usermanager.jquery.model;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
public class ApplicationBreakdownRequest {
	BreakdownFilter filter;
	ApplicationDataPoint dataPoint;

	public BreakdownFilter getFilter() {
		return filter;
	}

	public void setFilter(BreakdownFilter filter) {
		this.filter = filter;
	}

	public ApplicationDataPoint getDataPoint() {
		return dataPoint;
	}

	public void setDataPoint(ApplicationDataPoint dataPoint) {
		this.dataPoint = dataPoint;
	}
}
