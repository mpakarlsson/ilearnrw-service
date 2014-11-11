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
 * Level configuration for bridge reinforcer / eye exam
 * 
 * Consonant/vowels use TTS; different modes for syllables (highlight particular syllable), prefix and suffix (highlight problem), vowel/consonants (highlight sound) and blends (highlight grapheme?)
 * 
 */
public class BridgeUK implements GameLevel {
	
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {

		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if( (languageArea==LanguageAreasUK.CONFUSING)){
			return WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel);
			
		
		}
		
		
		int numberTargets = (int)java.lang.Math.ceil(parameters.batchSize/2.0);
		int numberDistractorsPerDifficulty = (int)java.lang.Math.floor(((double)(parameters.batchSize-numberTargets))/parameters.accuracy);
		if (numberDistractorsPerDifficulty==0)
			numberDistractorsPerDifficulty++;

		
		
		List<GameElement> targetWords = WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,numberTargets, parameters.wordLevel);
		
		if (targetWords.size()==0)
			return targetWords;
		
		
		List<List<GameElement>> distractors  = WordSelectionUtils.getDistractors(LanguageCode.EN,  lA, difficulty, numberDistractorsPerDifficulty, parameters.wordLevel ,parameters.accuracy,-1,new ArrayList<String>());
			
				
		for(List<GameElement> lge : distractors){
			for(GameElement ge : lge)
				targetWords.add(ge);
			
		}

		return targetWords;
		
		
		/*LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

			 
		if(languageArea==LanguageAreasUK.SYLLABLES){//desired number of syllables
			 
			 return WordSelectionUtils.getTargetWordsLengthX(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel,parameters.accuracy);
			 
			 
		 }else{//any syllables
			 //or if( (languageArea==LanguageAreasUK.VOWELS) | (languageArea==LanguageAreasUK.CONSONANTS)| (languageArea==LanguageAreasUK.BLENDS)){//vowel or consonant sounds// Use matching difficulty

			return WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,parameters.batchSize, parameters.wordLevel);
			 
		 }*/
		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		
		return new int[]{0};//Any level

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		
		
		return new FillerType[]{FillerType.CLUSTER};
	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//10 words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice

	}

	@Override
	public int[] accuracyLevels(int lA, int difficulty) {

		return new int[]{2};//No choice

/*		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if(languageArea==LanguageAreasUK.SYLLABLES)
			return new int[]{2,3,4};//Number syllables
		else
			return new int[]{0};//No choice
*/
	}

	@Override
	public TtsType[] TTSLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if( (languageArea==LanguageAreasUK.CONSONANTS) | (languageArea==LanguageAreasUK.VOWELS)){//vowel or consonant
			
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};
			
		}else
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};


	}

	@Override
	public int[] modeLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if (languageArea == LanguageAreasUK.SYLLABLES){//Random syllable
			return new int[]{0};//match a syllable//NOT USED ANYMORE
		}else if(languageArea == LanguageAreasUK.SUFFIXES){//suffixing
			return new int[]{1};//match last part
		}else if(languageArea == LanguageAreasUK.PREFIXES){
			return new int[]{2};//match first part
		}else if( (languageArea==LanguageAreasUK.CONSONANTS) | (languageArea==LanguageAreasUK.VOWELS)){
			return new int[]{4};//match vowel or consonant sound
		}else if( (languageArea==LanguageAreasUK.CONFUSING)){//No more for blends
			return new int[]{5};//match vowel or consonant sound//sound not available, can be merged with suffix and prefix
		}else
			return new int[]{0};
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
				return false;
			case SUFFIXES://Suffixes
				return true;
			case PREFIXES://Prefixes
				return true;
			case CONFUSING://Confusing letters
				return true;
			default:
				return false;
		
		}
	}

}
