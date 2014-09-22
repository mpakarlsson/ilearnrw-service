package com.ilearnrw.app.screening;

import java.util.ArrayList;

public class WordsInsideCategory {
	private ArrayList<String> words;
	private int category, index;

	public WordsInsideCategory() {
		this.words = null;
		this.category = -1;
		this.index = -1;
	}

	public WordsInsideCategory(int category, int index) {
		this.words = new ArrayList<String>();
		this.category = category;
		this.index = index;
	}

	public WordsInsideCategory(ArrayList<String> words, int category, int index) {
		this.words = words;
		this.category = category;
		this.index = index;
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}

	public void addWord(String word) {
		this.words.add(word);
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}	
}
