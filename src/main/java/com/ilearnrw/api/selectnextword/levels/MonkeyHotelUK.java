package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 *	Levels configuration for whack a monkey / whack a mole
 *
 *	prefix/suffix to words, confusing letters TTS to letters, vowel/consonant sounds to words and blends written-phoneme to words
 *
 *  confusing letters with sound untested
 */
public class MonkeyHotelUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		List<List<GameElement>> listsWords = new ArrayList<List<GameElement>>();

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS)| (lA==LanguageAreasUK.BLENDS)){

			//Find compatible phonetic difficulties
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

			List<Integer> selectedDifficulties = WordSelectionUtils.findCompatiblePhoneticDifficulties(LanguageCode.EN, languageArea, difficulty,parameters.accuracy);
			
			String[] phonemes = new String[selectedDifficulties.size()];
			
			for(int i =0;i< selectedDifficulties.size();i++){
				System.err.println(selectedDifficulties.get(i));
				phonemes[i] = (definitions.getProblemDescription(languageArea, i).getDescriptions()[0].split("-")[1]);
				
			}
			
			
			for(int i=0;i<phonemes.length;i++){
				
				List<String> copy = new ArrayList<String>();
				
				for(int j = 0;j< phonemes.length;j++){
					
					if (j!=i)
						copy.add(phonemes[j]);
				}
				
					
				listsWords.add(WordSelectionUtils.getTargetWordsWithoutPhonemes(LanguageCode.EN, languageArea, selectedDifficulties.get(i), parameters.batchSize, parameters.wordLevel,i!=0, copy));

			}
			

		}else if(lA==LanguageAreasUK.CONFUSING){
			
			return WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel);
			
		}else{
			
			
			List<GameElement> target = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel);
			
			if (target.size()==0){
				System.err.println("Empty difficulty "+languageArea+"_"+difficulty);
				return target;
			}
			
			 listsWords = WordSelectionUtils.getDistractors(LanguageCode.EN, languageArea, difficulty,parameters.batchSize, parameters.wordLevel ,parameters.accuracy,0,new ArrayList<String>());
			 
			 listsWords.add(target);
			
			
		}
		
		
		List<GameElement> result = new ArrayList<GameElement>();
		Random rand = new Random();
		
		for(int i = 0;i<parameters.batchSize;i++){
			for(int j = 0; j<listsWords.size(); j++){
			
				if(i<listsWords.get(j).size())
					result.add(listsWords.get(j).get(i));
				else{
					if(listsWords.get(j).size()==0) {
						System.err.println("Empty difficulty ");
						return new ArrayList<GameElement>();
					}else{
						result.add( listsWords.get(j).get( rand.nextInt(listsWords.get(j).size() ) ) );
					}
				}
			}
		}
		
		return result;
		
		
		
	

		
		
	}
	
	
	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)|(lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)|(lA==LanguageAreasUK.BLENDS)){
			return new FillerType[]{FillerType.CLUSTER};
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new FillerType[]{FillerType.NONE};
			
		}else
			return new FillerType[]{FillerType.NONE};

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//1o words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0,1,2};//Slow, medium and fast

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {

			return new int[]{1,2,3};//Number of fillers
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)){
			
			return new TtsType[]{TtsType.SPOKEN2WRITTEN,TtsType.WRITTEN2WRITTEN};
			
		}else if((lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)){
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
			
		}else if((lA==LanguageAreasUK.CONFUSING)){
			return new TtsType[]{TtsType.SPOKEN2WRITTEN,TtsType.WRITTEN2WRITTEN};
		}else
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
		

	}
	
	

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)){//mode 0 removed for now
			
			return new int[]{1};//Only matching difficulty on the monkeys; phoneme on the pattern; word on monkeys
			
		}else if((lA==LanguageAreasUK.PREFIXES)){
			return new int[]{3};//prefix to word
		}else if((lA==LanguageAreasUK.SUFFIXES)){
			return new int[]{2};//suffix to word
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new int[]{4};//letter to letters
		}else//BLENDS
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
				return true;
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
