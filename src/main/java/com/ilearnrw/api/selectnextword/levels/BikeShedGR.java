package com.ilearnrw.api.selectnextword.levels;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */

import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.TypeFiller;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;


/**
 * 
 * @author hector
 *
 * Level configuration for knocking maze / endless runner
 * 
 * Only mode is about counting words, no TTS support
 * 
 */

public class BikeShedGR extends GameLevel {

	@Override
	public List<GameElement> getWords(
			LevelParameters parameters,
			int languageArea, 
			int difficulty) {
		
		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.GR, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				false,
				false);
	
	}
	
	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{1,3,5};//Words per door
	}

	@Override
	public TypeFiller[] fillerTypes(int languageArea, int difficulty){
		return new TypeFiller[]{TypeFiller.NONE};//Distractors within difficulty

	}
	
	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		return new TypeAmount[]{TypeAmount.FEW};//No control over number of distractors
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
				return false;
			default:
				return false;
		
		}
	}
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==0){
			return "Χτύπησε την πόρτα τόσες φορές όσες και οι συλλαβές της λέξης";
			
		}
		
		return "Instructions not available";
		
	}
	
	
}
