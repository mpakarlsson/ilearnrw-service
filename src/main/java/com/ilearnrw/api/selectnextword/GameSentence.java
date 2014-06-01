package com.ilearnrw.api.selectnextword;

import java.util.ArrayList;

public class GameSentence {
	private String theSentence;
	private ArrayList<String> fillerWords;
	
	public GameSentence(String theSentence, ArrayList<String> fillerWords) {
		this.theSentence = theSentence;
		this.fillerWords = fillerWords;
	}
	
	public GameSentence() {
		this.theSentence = null;
		this.fillerWords = null;
	}
	public String getTheSentence() {
		return theSentence;
	}
	public void setTheSentence(String theSentence) {
		this.theSentence = theSentence;
	}
	public ArrayList<String> getFillerWords() {
		return fillerWords;
	}
	public void setFillerWords(ArrayList<String> fillerWords) {
		this.fillerWords = fillerWords;
	}
	
}
