package com.ilearnrw.api.selectnextword;

import java.util.ArrayList;

public class SentenceLoaderControler {
	public static GameSentence fromString(String str){
		int firstSqBracket = str.indexOf('[');
		int secondSqBracket = str.indexOf(']');
		String sentence = str.substring(0, firstSqBracket).trim();
		String filler[] = str.substring(firstSqBracket+1, secondSqBracket).split(", ");
		ArrayList<String> fillerWords = new ArrayList<String>();
		for (String f:filler)
			fillerWords.add(f);
		GameSentence gs = new GameSentence(sentence, fillerWords);
		return gs;
	}
}
