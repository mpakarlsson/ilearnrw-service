package com.ilearnrw.api.selectnextword.tools;

import java.util.ArrayList;

import ilearnrw.languagetools.extras.SentenceListLoader;
import ilearnrw.languagetools.extras.WordListLoader;
import ilearnrw.utils.LanguageCode;

public class ProblemWordListLoader {
	private ArrayList<String> sentenceList;

	public ProblemWordListLoader() {
		sentenceList = null;
	}

	public ProblemWordListLoader(LanguageCode lc, int category, int index) {
		String path = "game_words_GR/", lan = "GR";
		if (lc == LanguageCode.EN) {
			path = "game_words_EN/";
			lan = "EN";
		}
		try {
			WordListLoader ggl = new WordListLoader();
			ggl.load(path + "cat" + category + "/words_" + category + "_"
					+ index + "_" + lan + ".txt");
			sentenceList = ggl.getWordList();
		} catch (Exception e) {
			sentenceList = null;
		}
	}

	public void loadSentences(LanguageCode lc) {
		String path = "game_words_GR/";
		if (lc == LanguageCode.EN) {
			path = "game_words_EN/";
		}
		try {
			SentenceListLoader ggl = new SentenceListLoader();
			ggl.load(path + "/sentences.txt");
			sentenceList = ggl.getSentenceList();
		} catch (Exception e) {
			sentenceList = null;
		}
		
	}
	public ArrayList<String> getSentenceList() {
		return sentenceList;
	}
	

}
