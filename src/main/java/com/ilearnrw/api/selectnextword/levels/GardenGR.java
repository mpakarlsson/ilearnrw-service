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
 *	Levels configuration for harvest / sorting potions
 *
 *	 syllables categories are first/second open/closed, confusing 4 letters
 *
 */
public class GardenGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {

		
		return WordSelectionUtils.getTargetWords(LanguageCode.GR, lA, difficulty,4+parameters.batchSize, parameters.wordLevel);			

		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0};

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
		return new int[]{0};

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
		if(lA==LanguageAreasGR.FUNCTION_WORDS){
			return new int[]{4};
		}else if(lA==LanguageAreasGR.SYLLABLE_DIVISION){
			
			return new int[]{6};//words 2 words with the same number of syllables
		}else{
			return new int[]{5};//words 2 words, using the same matched difficulties		
			
		}
		
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
				return true;
			case FUNCTION_WORDS://Confusing letters
				return false;
			case LETTER_SIMILARITY:
				return false;
			default:
				return false;
		
		}
	}
}