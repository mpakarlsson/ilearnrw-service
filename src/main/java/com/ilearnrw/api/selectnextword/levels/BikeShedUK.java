package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
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

public class BikeShedUK implements GameLevel {

	

	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		
		return WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);		
		
		/*
		if(result.length==0)
			return 
			result.add(new GameElement(false,new EnglishWord("@@@@@"),languageArea, difficulty));
			
		return result;*/
		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//As many as difficulties

	}
	
	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		return new int[]{0};//Always is about counting syllables
	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.NONE};

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{5};//5 packages

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0,1,2};//Slow, medium and fast

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{0};//Nothing

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

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
