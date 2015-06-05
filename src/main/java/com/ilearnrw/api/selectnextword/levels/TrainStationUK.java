package com.ilearnrw.api.selectnextword.levels;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
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
public class TrainStationUK extends GameLevel {
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		
		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.EN, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				//new ArrayList<String>(),
				false,
				false);
		
		
		
	//	return WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);

		
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
	
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==0){
			return "Separate each syllable of the word";
			
		}else if (param.mode==1){
			return "Separate the suffix from the rest of the word";
			
		}else if(param.mode==2){
			return "Separate the prefix from the rest of the word";
		}else if(param.mode==3){
			return "Separate each sound of the word";
		}
		
		return "Instructions not available";
		
	}
	

}
