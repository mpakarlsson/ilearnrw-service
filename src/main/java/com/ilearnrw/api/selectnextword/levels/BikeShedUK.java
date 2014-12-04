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
 * Level configuration for knocking maze / endless runner
 * 
 * Only mode is about counting words, no TTS support
 * 
 */

public class BikeShedUK extends GameLevel {

		
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.EN, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				//new ArrayList<String>(),
				false,
				false);
		
	}

	
	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		if(LanguageAreasUK.values()[languageArea] == LanguageAreasUK.SYLLABLES)
			return new int[]{0};//Always is about counting syllables
		else 
			return new int[]{1};//phonemes, for vowels and consonants
	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{1,3,5};//Words per door

	}


	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		
		if(languageArea>=LanguageAreasUK.values().length)
			return false;
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		switch(lA){
		
			case CONSONANTS://Consonants
				return true;
			case VOWELS://Vowels
				return true;
			case BLENDS://Blends and letter patterns
				return false;
			case SYLLABLES://Syllables
				return true;
			case SUFFIXES://Suffixes
				return false;
			case PREFIXES://Prefixes
				return false;
			case CONFUSING://Confusing letters
				return false;
			default:
				return false;
		
		}
	}



}
