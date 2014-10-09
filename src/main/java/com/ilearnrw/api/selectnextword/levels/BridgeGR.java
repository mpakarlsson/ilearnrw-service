package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class BridgeGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {
		
		return WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel);

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
		return new int[]{10};
	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};
	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{0};//no choice
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};
	}

	@Override
	public int[] modeLevels(int lA, int difficulty) {
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		if (languageArea == LanguageAreasGR.SYLLABLE_DIVISION){//Random syllable
			return new int[]{0};//match 1,2,3.. syllables
		}else if((languageArea == LanguageAreasGR.DERIVATIONAL)||(languageArea == LanguageAreasGR.INFLECTIONAL)){//suffixing
			return new int[]{1};//match last syllable
		}else if(languageArea == LanguageAreasGR.PREFIXES){
			return new int[]{2};//match first syllable
		}else if(languageArea == LanguageAreasGR.LETTER_SIMILARITY){
			return new int[]{6};//match first syllable			
		}else{
			return new int[]{0};
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
				return true;
			case INFLECTIONAL://Syllables
				return true;
			case PREFIXES://Suffixes
				return false;//true;
			case GP_CORRESPONDENCE://Prefixes
				return false;
			case FUNCTION_WORDS://Confusing letters
				return false;
			case LETTER_SIMILARITY:
				return true;
			default:
				return false;
		
		}
		
		
	}

}
