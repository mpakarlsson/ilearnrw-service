package com.ilearnrw.api.selectnextword.levels;


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
 * Level configuration for junkyard / drop chops / karate chops
 * 
 * Different modes for prefixes, suffixes and 
 * 
 * 
 */


public class JunkyardGR extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {

		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		int minNumSyllables = -1;
		
		if (lA == LanguageAreasGR.SYLLABLE_DIVISION){
			minNumSyllables = 1;

		}
		
		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.GR, 
				 languageArea, 
				 difficulty,
				 parameters,
				 minNumSyllables,
				//new ArrayList<String>(),
				false,
				false);

		
	}


	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA==LanguageAreasGR.SYLLABLE_DIVISION))
			return new int[]{0};//Syllables
		else if(lA==LanguageAreasGR.INFLECTIONAL)
			return new int[]{1};//Suffixes
		else //if(lA==LanguageAreasGR.PREFIXES)
			return new int[]{2};//Prefixes
		
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
				return true;//true;
			case PREFIXES://Suffixes
				return true;//true;
			case GP_CORRESPONDENCE://Prefixes
				return false;
			case FUNCTION_WORDS://Confusing letters
				return false;
			case LETTER_SIMILARITY:
				return false;
			default:
				return false;
		
		}
		
	}

	/* Instructions for the games */
	public String instructions(int languageArea, int difficulty,LevelParameters param){

		if(param.mode==0)
			return "Χώρισε τις συλλαβές";
		else if(param.mode==1)
			return "Βρες τις καταλήξεις";
		else if(param.mode==2)
			return "Βρες τα προθέματα";
		
		
		return "Not available";
	}
	
	
}
