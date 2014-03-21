package com.ilearnrw.api.textannotation;

import ilearnrw.annotation.UserBasedAnnotatedWordsSet;

public class ResponsePackage {
	private UserBasedAnnotatedWordsSet wordSet;
	private String html;
	
	public ResponsePackage() {
		this.wordSet = new UserBasedAnnotatedWordsSet();
		this.html = "";
	}
	public ResponsePackage(UserBasedAnnotatedWordsSet wordSet, String html) {
		super();
		this.wordSet = wordSet;
		this.html = html;
	}
	public UserBasedAnnotatedWordsSet getWordSet() {
		return wordSet;
	}
	public void setWordSet(UserBasedAnnotatedWordsSet wordSet) {
		this.wordSet = wordSet;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	};
	
}
