package com.ilearnrw.app.usermanager.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class Birthdate {
	@Range(min = 1, max = 31)
	@NotNull
	Integer date;
	@Range(min = 1, max = 12)
	@NotNull
	Integer month;
	@NotNull
	@Min(1800)
	Integer year;

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
