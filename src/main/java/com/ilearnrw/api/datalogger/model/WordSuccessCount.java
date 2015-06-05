package com.ilearnrw.api.datalogger.model;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
public class WordSuccessCount extends WordCount{
	protected int succeed, failed;

	public WordSuccessCount() {
		super();
		this.succeed = 0;
		this.failed = 0;
	}

	public WordSuccessCount(int succeed, int failed) {
		super();
		this.succeed = succeed;
		this.failed = failed;
	}

	public WordSuccessCount(String word, int count, int succeed, int failed) {
		super(word, count);
		this.succeed = succeed;
		this.failed = failed;
	}

	public int getSucceed() {
		return succeed;
	}

	public void setSucceed(int succeed) {
		this.succeed = succeed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}
	
}
