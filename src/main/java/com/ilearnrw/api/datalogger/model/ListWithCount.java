package com.ilearnrw.api.datalogger.model;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

public class ListWithCount<T> {
	private int count;
	private List<T> list;

	public ListWithCount() {
		super();
	}

	public ListWithCount(int count, List<T> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
		if (list != null) {
			this.count = list.size();
		} else {
			this.count = 0;
		}
	}

}
