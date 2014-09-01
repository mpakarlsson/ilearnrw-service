package com.ilearnrw.api.selectnextword;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public ArrayList<String> getTargetedWords() {
		ArrayList<String> allMatches = new ArrayList<String>();
		 Matcher m = Pattern.compile("\\{(.*?)\\}",Pattern.DOTALL).matcher(theSentence);
		 while (m.find()) {
		   allMatches.add(m.group().substring(1, m.group().length()-1));
		 }
		return allMatches;
	}
	public void setFillerWords(ArrayList<String> fillerWords) {
		this.fillerWords = fillerWords;
	}
	
}
