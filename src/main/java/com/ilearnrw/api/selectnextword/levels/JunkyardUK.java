package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;


/**
 * 
 * @author hector
 *
 * Level configuration for junkyard / drop chops / karate chops
 * 
 * Different modes for prefixes, suffixes, syllables and vowel/consonants
 * 
 * 
 */

public class JunkyardUK extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		int minNumSyllables = -1;

		if (lA == LanguageAreasUK.SYLLABLES){
			minNumSyllables = 1;

		}

		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.EN, 
				languageArea, 
				difficulty,
				parameters,
				minNumSyllables,
				false,
				false);

	}


	@Override
	public int[] modeLevels(int languageArea, int difficulty) {

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.SYLLABLES))
			return new int[]{0};//Syllables
		else if(lA==LanguageAreasUK.SUFFIXES)
			return new int[]{1};//Suffixes
		else if(lA==LanguageAreasUK.PREFIXES)
			return new int[]{2};//Prefixes
		else
			return new int[]{3};//Vowels & consonants; use graphemes

	}


	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {

		if(languageArea>=LanguageAreasUK.values().length)
			return false;

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		switch(lA){			
		case CONSONANTS://Consonants
			return false;
		case VOWELS://Vowels
			return false;
		case BLENDS://Blends and letter patterns
			return false;
		case SYLLABLES://Syllables
			return true;
		case SUFFIXES://Suffixes
			return true;
		case PREFIXES://Prefixes
			return true;
		case CONFUSING://Confusing letters
			return false;
		default:
			return false;

		}
	}
	
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==0){
			return "Split the words into syllables";
			
		}else if (param.mode==1){
			return "Separate the suffix of each word";
			
		}else if(param.mode==2){
			return "Separate the prefix of each word";
		}else if(param.mode==3){
			return "Split the words into sounds";
		}
		
		return "Instructions not available";
		
	}
	


}
