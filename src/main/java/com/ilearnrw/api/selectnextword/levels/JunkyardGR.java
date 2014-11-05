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
 * Different modes for prefixes, suffixes and 
 * 
 * 
 */


public class JunkyardGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {

		
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		if (languageArea==LanguageAreasGR.SYLLABLE_DIVISION){//Syllable division
			
			//List<List<GameElement>> distractors = WordSelectionUtils.getDistractors(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel ,3,1, new ArrayList<String>());
			//More than one syllable
			return WordSelectionUtils.getTargetWordsWithSyllables(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel,1);

			//return WordSelectionUtils.getTargetWordsLengthX(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel,parameters.accuracy);
			
			
		}else if((languageArea==LanguageAreasGR.PREFIXES)){
			
			return WordSelectionUtils.getTargetWords(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel);

		}else{//INFL SUFF
			
			return WordSelectionUtils.getTargetWords(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel);

			
		}

		
		

		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0};//All

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

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};
	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA==LanguageAreasGR.SYLLABLE_DIVISION))
			return new int[]{0};//Syllables
		else if(lA==LanguageAreasGR.INFLECTIONAL)
			return new int[]{1};//Suffixes
		else //if(lA==LanguageAreasGR.PREFIXES)
			return new int[]{2};//Prefixes
		
	}

	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		if(languageArea>=LanguageAreasGR.values().length)
			return false;
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
			case SYLLABLE_DIVISION://Consonants
				return true;		
			case CONSONANTS://Consonants
				return false;
			case VOWELS://Vowels
				return false;
			case DERIVATIONAL://Blends and letter patterns
				return false;//true;
			case INFLECTIONAL://Syllables
				return true;//true;
			case PREFIXES://Suffixes
				return true;//true;
			case GP_CORRESPONDENCE://Prefixes
				return false;
			case FUNCTION_WORDS://Confusing letters
				return false;
			case LETTER_SIMILARITY:
				return false;
			default:
				return false;
		
		}
		
	}

}
