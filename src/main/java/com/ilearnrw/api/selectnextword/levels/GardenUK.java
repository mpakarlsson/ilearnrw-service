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
 *	Levels configuration for harvest / sorting potions
 *
 *	prefix/suffix/vowels categories are characters, syllables categories are first/second open/closed, confusing 4 letters
 *
 */

public class GardenUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		int numberTargets = (int)java.lang.Math.ceil(parameters.batchSize/2.0);
		int numberDistractorsPerDifficulty = (int)java.lang.Math.floor(((double)(parameters.batchSize-numberTargets))/parameters.accuracy);
		if (numberDistractorsPerDifficulty==0)
			numberDistractorsPerDifficulty++;
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		List<GameElement> result = new ArrayList<GameElement>();

		
		if(lA==LanguageAreasUK.SYLLABLES){
			
			
			result = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,numberTargets, parameters.wordLevel);	
		
			if (result.size()==0)
				return result;
		
		
			List<List<GameElement>> distractors = WordSelectionUtils.getDistractors(LanguageCode.EN, languageArea, difficulty,numberDistractorsPerDifficulty, parameters.wordLevel,parameters.accuracy,-1,new ArrayList<String>());	
		
			for(List<GameElement> lge : distractors)
				for (GameElement f : lge)
					result.add(f);	
			
		}else if(lA==LanguageAreasUK.CONFUSING){
			
			result = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,numberTargets, parameters.wordLevel);	
			
			if (result.size()==0)
				return result;
		
		
			List<List<GameElement>> distractors = WordSelectionUtils.getDistractors(LanguageCode.EN, languageArea, difficulty,numberDistractorsPerDifficulty, parameters.wordLevel,parameters.accuracy,-1,new ArrayList<String>());	
		
			for(List<GameElement> lge : distractors)
				for (GameElement f : lge)
					result.add(f);					
			
		}else{
			//TODO routine to pull words from different characters
			
			List<Integer> difficulties = WordSelectionUtils.findDifferentCharacter(LanguageCode.EN,  languageArea, difficulty,3);
			
			for(int i = 0; i<difficulties.size();i++){
				
				List<GameElement> aux = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulties.get(i),parameters.batchSize, parameters.wordLevel,i!=0);	
				for(GameElement ge : aux)
					result.add(ge);
			}	
			
		}
		
		
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
		
		return new FillerType[]{FillerType.CLUSTER};//,FillerType.PREVIOUS};

		/*		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if (languageArea==LanguageAreasUK.CONFUSING){
			return new FillerType[]{FillerType.NONE};
		}else{
			return new FillerType[]{FillerType.CLUSTER};//,FillerType.PREVIOUS};
		}*/
			
			
	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//5 words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice
	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {

		if(LanguageAreasUK.values()[languageArea]==LanguageAreasUK.CONFUSING)
			return new int[]{1};//just one distractor as this difficulty has 2 letter each
		return new int[]{3};//Number of distractors
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		
		if(lA==LanguageAreasUK.SYLLABLES){//for syllabification: open and closed syllables
			return new int[]{0,6};
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new int[]{2};

		}else//Prefix Suffix & vowels
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
