package com.ilearnrw.api.selectnextword.levels;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import com.ilearnrw.api.selectnextword.TypeFiller;
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

}
