package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.user.problems.ProblemDefinitionIndex;
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
 * Level configuration for Town Square / City Hall / Moving Pathways / Minefield
 * 
 * Consonant/vowels use TTS and confusing letters us TTS; ; vowel/consonants find words with the sound; confusing find letter with the sound
 * 
 */
public class CityHallUK implements GameLevel {

	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];


		if(languageArea==LanguageAreasUK.CONFUSING){//Only the letters on the difficulty will be used
			
			return WordSelectionUtils.getTargetWords(LanguageCode.EN, lA, difficulty,1, 0);
		
			
		}else{
	
			//Find compatible phonetic difficulties
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

			List<Integer> selectedDifficulties = WordSelectionUtils.findCompatiblePhoneticDifficulties(LanguageCode.EN, lA, difficulty,parameters.accuracy);
			
			String[] phonemes = new String[selectedDifficulties.size()];
			
			for(int i =0;i< selectedDifficulties.size();i++){
				
				phonemes[i] = (definitions.getProblemDescription(lA, i).getDescriptions()[0].split("-")[1]);
				
			}
			List<GameElement> result = new ArrayList<GameElement>();
			
			for(int i=0;i<phonemes.length;i++){
				
				List<String> copy = new ArrayList<String>();
				
				for(int j = 0;j< phonemes.length;j++){
					
					if (j!=i)
						copy.add(phonemes[j]);
				}
				
					
				List<GameElement> aux =	WordSelectionUtils.getTargetWordsWithoutPhonemes(LanguageCode.EN, lA, selectedDifficulties.get(i), parameters.batchSize, parameters.wordLevel,i!=0, copy);
				
				for(GameElement ge : aux)
					result.add(ge);
				
			}
			

			//Word pattern= new Word(phoneme);//Phoneme
			//result.add(new GameElement(false, pattern, languageArea, difficulty));
			//pattern= new Word(definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].split("-")[0]);//Grapheme
			//result.add(new GameElement(false, pattern, languageArea, difficulty));

			return result;
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
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int lA, int difficulty) {
		return new FillerType[]{FillerType.CLUSTER};

/*		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if(languageArea==LanguageAreasUK.VOWELS){			
			return new FillerType[]{FillerType.PREVIOUS};
		}else
			return new FillerType[]{FillerType.NONE,FillerType.PREVIOUS};*/

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{1,2,5};//words per difficulty

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0,1,2};//Sizes of the map

	}

	@Override
	public int[] accuracyLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		if(languageArea==LanguageAreasUK.CONFUSING)
			return new int[]{0};//irrelevant
		else{
			return new int[]{1,2,3};//number of alternative words
		}
		
		/*if(languageArea==LanguageAreasUK.VOWELS){//vowels
			return new int[]{1,2,3};//one or several correct
		}else if(languageArea==LanguageAreasUK.BLENDS){
			return new int[]{1,5,10};//one or several correct
			
		}else{
		
			return new int[]{1};//only one correct
		}*/
	}

	@Override
	public TtsType[] TTSLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		if(languageArea==LanguageAreasUK.BLENDS){
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
		}else
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};

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
				return true;
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


}
