package com.ilearnrw.api.selectnextword.tools;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.ArrayList;

import ilearnrw.languagetools.extras.SentenceListLoader;
import ilearnrw.languagetools.extras.WordListLoader;
import ilearnrw.utils.LanguageCode;

public class ProblemWordListLoader {
	private ArrayList<String> items;

	public ProblemWordListLoader() {
		items = null;
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
			items = ggl.getWordList();
		} catch (Exception e) {
			items = null;
		}
	}

	public void loadItems(LanguageCode lc, String fname) {
		String path = "game_words_GR/";
		if (lc == LanguageCode.EN) {
			path = "game_words_EN/";
		}
		try {
			SentenceListLoader ggl = new SentenceListLoader();
			ggl.load(path+fname);
			items = ggl.getSentenceList();
		} catch (Exception e) {
			items = null;
		}
		
	}
	public ArrayList<String> getItems() {
		return items;
	}
	

}
