package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
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

public class BridgeGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();
		
		
		ArrayList<String> words = new ProblemWordListLoader(LanguageCode.GR, lA, difficulty).getItems();
		EasyHardList list = new EasyHardList(words);
		
		ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
		
		for(String word : targetWords){
			
			GreekWord ew =  new GreekWord(word.split("\'")[0]);
			result.add(new GameElement(false,ew,lA, difficulty));
		}
		
		return result;
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};
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
			return new int[]{7};//match last syllable
		}else if(languageArea == LanguageAreasGR.PREFIXES){
			return new int[]{8};//match first syllable
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
				return false;//true;
			case INFLECTIONAL://Syllables
				return false;//true;
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
