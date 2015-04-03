package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;



/**
 * 
 * @author hector
 *
 * Level configuration for Town Square / City Hall / Moving Pathways / Minefield
 * 
 * Consonant/vowels use TTS and confusing letters us TTS; ; vowel/consonants find words with the sound; confusing find letter with the sound
 * 
 */
public class CityHallUK extends GameLevel {

	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];


		if(lA==LanguageAreasUK.CONFUSING){//Only the letters on the difficulty will be used
			
			parameters.amountDistractors = TypeAmount.NONE;
			parameters.batchSize = 1;
			
			return WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					//new ArrayList<String>(),
					false,
					false);
			
		
			
		}else{
	
			
			return WordSelectionUtils.getTargetWordsWithDistractorsAndNoPhonemes(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,
					false);			
			

		}
		

	}

	@Override
	public int[] modeLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if((languageArea==LanguageAreasUK.VOWELS) | (languageArea==LanguageAreasUK.CONSONANTS)){	
			return new int[]{2};//phoneme of difficulty is the pattern; letter combinations on the tiles//removed mode 0 for now
		}else if(languageArea==LanguageAreasUK.CONFUSING){
			return new int[]{1};//tiles have letters, pattern has spoken letter
		}else{//blends and letter patterns
			return new int[]{2};//tiles have words; pattern is a sound from matching problems
			
		}
	}
	
	
	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		if(lA==LanguageAreasUK.CONFUSING)
			return new TypeAmount[]{TypeAmount.NONE};//irrelevant
		else{
			return new TypeAmount[]{TypeAmount.FEW,TypeAmount.HALF,TypeAmount.MANY};//number of alternative difficulties
		}		
		
	}
	
	
	@Override
	public int[] wordLevels(int languageArea, int difficulty){
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		if(lA==LanguageAreasUK.CONFUSING)
			return new int[]{0};
		else{
			return new int[]{0,4,9};
		}
	}

	
	@Override
	public int[] batchSizes(int languageArea, int difficulty){
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		if(lA==LanguageAreasUK.CONFUSING)
			return new int[]{1};
		else{
			return new int[]{12,16,20,24};
		}	
	}
	
	
	

	@Override
	public TtsType[] TTSLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if((languageArea==LanguageAreasUK.CONSONANTS)|(languageArea==LanguageAreasUK.VOWELS)|(languageArea==LanguageAreasUK.CONFUSING))
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};
		else
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};

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
				return false;
			case PREFIXES://Prefixes
				return false;
			case CONFUSING://Confusing letters
				return true;
			default:
				return false;
		
		}
	}
	
	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{2,3,4};//Number of paths
	}

	
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==0){
			return "Follow the tiles with the spelling of the sound";
		}else if(param.mode==1){
			if(param.ttsType==TtsType.WRITTEN2WRITTEN){
				return "Follow the tiles with the given letter";
			}else{
				return "Follow the tiles with the letter read aloud";
			}
		}else if(param.mode==2){
			return "Follow the tiles with a word containing the sound";
		}

		return "Instructions not available";
		
		
	}


}
