package com.ilearnrw.api.datalogger.model;

public class WordCount {
	private String word;
	private int count;

	public WordCount() {
		this.word = null;
		count = 0;
	}

	public WordCount(String word, int count) {
		super();
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
