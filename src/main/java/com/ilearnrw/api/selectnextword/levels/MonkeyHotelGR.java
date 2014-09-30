package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class MonkeyHotelGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();

			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.GR, lA, difficulty).getItems();
			result.add(new GameElement(false, new GreekWord(words.get(0)), lA, difficulty));			
			
		
		return result;

	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//only one correct

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.CLUSTER};

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//only one correct

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//only one correct

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{1};//only one correct

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		return new int[]{4};
	}

	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
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
				return true;
			default:
				return false;
		
		}
	}

}
