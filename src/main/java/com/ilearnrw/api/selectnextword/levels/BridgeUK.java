package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.TypeBasic;
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
public class BridgeUK extends GameLevel {
	
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.EN, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				false,
				false);
		
		/*LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if( (languageArea==LanguageAreasUK.CONFUSING)){
			
			return WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					new ArrayList<String>(),
					false,
					false);
			
		
		}*/	
		
	}


	@Override
	public TypeBasic[] speedLevels(int languageArea, int difficulty) {
		return new TypeBasic[]{TypeBasic.LOW};//No choice

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
