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
 * Level configuration for junkyard / drop chops / karate chops
 * 
 * Different modes for prefixes, suffixes, syllables and vowel/consonants
 * 
 * 
 */

public class JunkyardUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {

		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if (languageArea==LanguageAreasUK.SYLLABLES){//Syllable division
			
			return WordSelectionUtils.getTargetWordsWithSyllables(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel,1);

			//return WordSelectionUtils.getTargetWordsLengthX(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel,parameters.accuracy);
			
			
		}else if((languageArea==LanguageAreasUK.SUFFIXES)||(languageArea==LanguageAreasUK.PREFIXES)){
			
			//return WordSelectionUtils.getTargetWordsGreaterLengthX(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel,1);
			return WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel);

		}else{//VOWELS & CONSONANTS 
			
			return WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel);

			
		}
			
	}
	

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.NONE};
	}


	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//10 words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0,1,2};//Slow, medium and fast

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice
		
		/*LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if(lA==LanguageAreasUK.SYLLABLES)
			return new int[]{2,3,4};//Number syllables
		else
			return new int[]{0};//No choice
			*/
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN,TtsType.WRITTEN2SPOKEN};

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
	
}
