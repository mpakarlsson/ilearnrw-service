package com.ilearnrw.api.selectnextword.tools;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.ArrayList;

import ilearnrw.languagetools.extras.WordListLoader;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.utils.LanguageCode;

public class ProblemsWordLists {
	private WordList wordList[][];

	public ProblemsWordLists(LanguageCode lc) {
		ProblemDefinitionIndex probs;
		String path = "game_words_GR/", lan = "GR";
		if (lc == LanguageCode.EN) {
			path = "game_words_EN/";
			lan = "EN";
		}
		probs = new ProblemDefinitionIndex(lc);
		wordList = new WordList[probs.returnIndexLength()][];
		for (int i = 0; i < wordList.length; i++) {
			wordList[i] = new WordList[probs.getRowLength(i)];
			for (int j = 0; j < wordList[i].length; j++) {
				wordList[i][j] = null;
				try {
					WordListLoader ggl = new WordListLoader();
					ggl.load(path + "cat" + i + "/words_" + i + "_" + j + "_"
							+ lan + ".txt");
					wordList[i][j].data = ggl.getWordList();
				} catch (Exception e) {
				}
			}
		}
	}

	public ArrayList<String> get(int i, int j) {
		if (wordList == null || wordList.length <= i || wordList[i].length <= j)
			return null;
		return wordList[i][j].data;
	}
	
	private class WordList{
		ArrayList<String> data;
	}
}
