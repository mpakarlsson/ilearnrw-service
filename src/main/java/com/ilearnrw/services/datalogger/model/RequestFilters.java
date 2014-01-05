package com.ilearnrw.services.datalogger.model;

public class RequestFilters {
	private String timestart;
	private String timeend;
	private boolean count;

	public RequestFilters() {

	}

	public RequestFilters(String timestart, String timeend, boolean count) {
		super();
		this.timestart = timestart;
		this.timeend = timeend;
		this.count = count;
	}

	public String getTimestart() {
		return timestart;
	}

	public void setTimestart(String timestart) {
		this.timestart = timestart;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	public boolean isCount() {
		return count;
	}

	public void setCount(boolean count) {
		this.count = count;
	}

}
