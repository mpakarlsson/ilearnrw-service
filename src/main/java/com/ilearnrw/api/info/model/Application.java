package com.ilearnrw.api.info.model;

public class Application {
	private int id;
	private String name;
	private String appId;
	
	private boolean letters;
	private boolean sentences;
	private boolean words;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLetters() {
		return letters;
	}
	public void setLetters(boolean letters) {
		this.letters = letters;
	}
	public boolean isSentences() {
		return sentences;
	}
	public void setSentences(boolean sentences) {
		this.sentences = sentences;
	}
	public boolean isWords() {
		return words;
	}
	public void setWords(boolean words) {
		this.words = words;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
}
