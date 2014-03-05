package com.ilearnrw.api.selectnextword;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.Word;

import java.io.Serializable;

public class GameElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isFiller;
	private char theChar;
	private AnnotatedWord annotatedWord;
	private String theSentence;

	public GameElement(boolean isFiller, Word theWord) {
		super();
		this.isFiller = isFiller;
		annotatedWord = new AnnotatedWord(theWord);
	}

	public GameElement(boolean isFiller, Word theWord, int category, int index) {
		super();
		this.isFiller = isFiller;
		annotatedWord = new AnnotatedWord(theWord, category, index);
	}

	public boolean isFiller() {
		return isFiller;
	}

	public void setFiller(boolean isFiller) {
		this.isFiller = isFiller;
	}

	public Word getAnnotatedWord() {
		return annotatedWord;
	}

	public void setAnnotatedWord(Word word) {
		annotatedWord = new AnnotatedWord(word);
	}

	public void setAnnotatedWord(AnnotatedWord word) {
		annotatedWord = word;
	}

}
