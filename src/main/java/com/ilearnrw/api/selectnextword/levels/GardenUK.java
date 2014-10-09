package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class GardenUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		
		List<GameElement> result = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);	
		
		if (result.size()==0)
			return result;
		
		
		List<GameElement> fillers = WordSelectionUtils.getClusterDistractors(LanguageCode.EN, languageArea, difficulty,parameters.batchSize, parameters.wordLevel,4);	

		for (GameElement f : fillers)
			result.add(f);		
		
		
		
		/*List<GameElement> resultComplete = new ArrayList<GameElement>();
		
		for(GameElement ge : result){
			
			resultComplete.add(new GameElement(ge.isFiller(),ge.getAnnotatedWord()));
			
		}*/
		

		return result;
	
	
	
	}
	


	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if (languageArea==LanguageAreasUK.CONFUSING){
			return new FillerType[]{FillerType.NONE};
		}else{
			return new FillerType[]{FillerType.CLUSTER};//,FillerType.PREVIOUS};
		}
			
			
	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{5};//5 words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice
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
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		
		if(lA==LanguageAreasUK.SYLLABLES){//for syllabification: open and closed syllables
			return new int[]{0};
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new int[]{2};

		}else
			return new int[]{1};//based on the character
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
				return true;
			case BLENDS://Blends and letter patterns
				return false;
			case SYLLABLES://Syllables
				return true;
			case SUFFIXES://Suffixes
				return true;
			case PREFIXES://Prefixes
				return false;
			case CONFUSING://Confusing letters
				return true;
			default:
				return false;
		
		}
	}
}
