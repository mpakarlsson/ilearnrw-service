package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.TypeBasic;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TypeFiller;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;



/**
 * 
 * @author hector
 *
 * Level configuration for bridge reinforcer / eye exam
 * 
 * Syllable division is played with 1/2 syllables; the rest are played with the difficylty
 * 
 */


public class BridgeGR extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
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
	public TypeBasic[] speedLevels(int languageArea, int difficulty) {
		return new TypeBasic[]{TypeBasic.LOW};
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
	public int[] modeLevels(int lA, int difficulty) {
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		if (languageArea == LanguageAreasGR.SYLLABLE_DIVISION){//Random syllable
			return new int[]{0};//match 1,2,3.. syllables
		}else{
			
			return new int[]{6};//matched difficulty is what needs to be highlighted
			//if((languageArea == LanguageAreasGR.DERIVATIONAL)||(languageArea == LanguageAreasGR.INFLECTIONAL)){//suffixing
		}
			/*return new int[]{1};//match last syllable
		}else if(languageArea == LanguageAreasGR.PREFIXES){
			return new int[]{2};//match first syllable
		}else if(languageArea == LanguageAreasGR.LETTER_SIMILARITY){
			return new int[]{6};//match first syllable			
		}else{
			return new int[]{0};
		}*/
			
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
				return true;
			case INFLECTIONAL://Syllables
				return true;
			case PREFIXES://Suffixes
				return true;//true;
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
	
	/* Instructions for the games */
	public String instructions(int languageArea, int difficulty,LevelParameters param){
	
		if(param.mode==0){
			return "Υπογράμμισε τη συλλαβή";//TODO update
		}else if(param.mode==6){
			if(languageArea==2)
				return "άγγιξε το σωστό γράμμα";
			else if(languageArea==5)
				return "άγγιξε το πρώτο μέρος της λέξης, π.χ. αποβολή (άγγιξε το ‘από’)";
			else if(languageArea==6)
				return "άγγιξε την κατάληξη";
			else if(languageArea==7)
				return "άγγιξε την κατάληξη";
			
		}
		
		return "Not available";
	}
	
	

}
