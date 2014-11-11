package com.ilearnrw.api.selectnextword.levels;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
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
 *	Levels configuration for train dispatcher
 *
 *	prefix/suffix split the problem, syllables split the syllables vowels/consonants split the sounds
 *
 */
public class TrainStationUK implements GameLevel {
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		
		int numberTargets = (int)java.lang.Math.ceil(parameters.batchSize/2.0);
		
		List<GameElement> targetWords =  WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, numberTargets, parameters.wordLevel);

		if (targetWords.size()==0)
			return targetWords;
				
		int numberDistractorsPerDifficulty = (int)java.lang.Math.floor(((double)(parameters.batchSize-numberTargets))/parameters.accuracy);
		if (numberDistractorsPerDifficulty==0)
			numberDistractorsPerDifficulty++;
		
		List<List<GameElement>> distractors  = WordSelectionUtils.getDistractors(LanguageCode.EN,  languageArea, difficulty, numberDistractorsPerDifficulty, parameters.wordLevel ,parameters.accuracy,-1,new ArrayList<String>());
		
		for(List<GameElement> lge : distractors){
			for(GameElement ge : lge)
				targetWords.add(ge);
			
		}

		return targetWords;
		
		
		
	//	return WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);

		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.CLUSTER};

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//5 rounds

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{2};//Nothing

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}
	
	
	@Override
	public int[] modeLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if(languageArea==LanguageAreasUK.SYLLABLES)
			return new int[]{0};//Syllables
		else if(languageArea==LanguageAreasUK.SUFFIXES)
			return new int[]{1};//Suffixes
		else if(languageArea==LanguageAreasUK.PREFIXES)
			return new int[]{2};//Prefixes
		else//consonant vowels
			return new int[]{3};//Graphemes

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
