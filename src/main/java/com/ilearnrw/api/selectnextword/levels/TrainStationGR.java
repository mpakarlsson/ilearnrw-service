package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;

public class TrainStationGR extends GameLevel {

	
	/**
	 * 
	 * @author hector
	 *
	 *	Levels configuration for train dispatcher
	 *
	 *	prefix/suffix split the problem, syllables split the syllables 
	 *
	 */
	
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {
		
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
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA==LanguageAreasGR.SYLLABLE_DIVISION))
			return new int[]{0};//Syllables
		else if(lA==LanguageAreasGR.PREFIXES)
			return new int[]{2};//Prefixes
		else
			return new int[]{1};//suffixes
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
				return true;//true;
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
			return "Γράψε τις συλλαβές της λέξης σε ξεχωριστά βαγόνια";
		else if(param.mode==1){
			if(languageArea==6)
				return "Γράψε την κατάληξη της λέξης σε ξεχωριστό βαγόνι από την υπόλοιπη λέξη (π.χ. αγαπη-μένος)";
			else if(languageArea==7)
				return "Γράψε την κατάληξη της λέξης σε ξεχωριστό βαγόνι από την υπόλοιπη λέξη (π.χ. αγαπη-μένος)";
		}else if(param.mode==2)
			return "Γράψε το πρώτο μέρος της λέξης (π.χ. υπο-τιμώ) σε ξεχωριστά βαγόνια από την υπόλοιπη λέξη";
		

		return "Not available";
	}

}
