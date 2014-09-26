package com.ilearnrw.api.selectnextword.levels;

import java.util.List;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;

public class NotImplemented implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//Easy and hard

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
	public int[] modeLevels(int languageArea, int difficulty) {
		return new int[]{0};//Nothing

	}

	@Override
	public boolean allowedDifficulty(int lA, int difficulty) {
		
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		switch(languageArea){
		
		case SYLLABLE_DIVISION://Consonants
			return false;		
		case CONSONANTS://Consonants
			return false;
		case VOWELS://Vowels
			return false;
		case DERIVATIONAL://Blends and letter patterns
			return false;//true;
		case INFLECTIONAL://Syllables
			return false;//true;
		case PREFIXES://Suffixes
			return false;//true;
		case GP_CORRESPONDENCE://Prefixes
			return false;
		case FUNCTION_WORDS://Confusing letters
			return false;//true;
		case LETTER_SIMILARITY:
			return false;
		default:
			return false;
	
	}
	}

}
