package com.ilearnrw.api.selectnextword;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.Word;

import java.io.Serializable;
import java.util.ArrayList;

public class GameElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isFiller;
	private AnnotatedWord annotatedWord;
	private GameSentence annotatedSentence;

	public GameElement(boolean isFiller, Word theWord) {
		super();
		this.isFiller = isFiller;
		annotatedWord = new AnnotatedWord(theWord);
		this.annotatedSentence = null;
	}

	
	public GameElement(boolean isFiller, AnnotatedWord theWord) {
		super();
		this.isFiller = isFiller;
		annotatedWord = theWord;
		this.annotatedSentence = null;
	}
	
	public GameElement(boolean isFiller, Word theWord, int category, int index) {
		super();
		this.isFiller = isFiller;
		annotatedWord = new AnnotatedWord(theWord, category, index);
		this.annotatedSentence = null;
	}

	public GameElement(GameSentence sentence) {
		super();
		this.isFiller = false;
		annotatedWord = null;
		this.annotatedSentence = sentence;
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

	public GameSentence getAnnotatedSentence() {
		return annotatedSentence;
	}

	public void setAnnotatedSentence(GameSentence annotatedSentence) {
		this.annotatedSentence = annotatedSentence;
	}

}
