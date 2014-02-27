package com.ilearnrw.api.selectnextword;

import ilearnrw.textclassification.Word;

import java.io.Serializable;

public class GameElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isFiller;
	private char theChar;
	private Word theWord;
	private String theSentence;

	public GameElement(boolean isFiller, Word theWord) {
		super();
		this.isFiller = isFiller;
		this.theWord = theWord;
	}

	public boolean isFiller() {
		return isFiller;
	}

	public void setFiller(boolean isFiller) {
		this.isFiller = isFiller;
	}

	public Word getTheWord() {
		return theWord;
	}

	public void setTheWord(Word theWord) {
		this.theWord = theWord;
	}

}
