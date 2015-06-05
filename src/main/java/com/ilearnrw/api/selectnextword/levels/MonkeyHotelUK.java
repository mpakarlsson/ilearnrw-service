package com.ilearnrw.api.selectnextword.levels;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */

import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.TypeAmount;
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
 *	Levels configuration for whack a monkey / whack a mole
 *
 *	prefix/suffix to words, confusing letters TTS to letters, vowel/consonant sounds to words and blends written-phoneme to words
 *
 *  confusing letters with sound untested
 */
public class MonkeyHotelUK extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		//List<List<GameElement>> listsWords = new ArrayList<List<GameElement>>();

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS)| (lA==LanguageAreasUK.BLENDS)){

			
			return WordSelectionUtils.getTargetWordsWithDistractorsAndNoPhonemes(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,
					false);	
			
			//Find compatible phonetic difficulties
			/*ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

			List<Integer> selectedDifficulties = WordSelectionUtils.findCompatiblePhoneticDifficulties(LanguageCode.EN, languageArea, difficulty,parameters.accuracy);
			
			String[] phonemes = new String[selectedDifficulties.size()];
			
			for(int i =0;i< selectedDifficulties.size();i++){
				System.err.println(selectedDifficulties.get(i));
				phonemes[i] = (definitions.getProblemDescription(languageArea, selectedDifficulties.get(i)).getDescriptions()[0].split("-")[1]);
				
			}
			
			
			for(int i=0;i<phonemes.length;i++){
				
				List<String> copy = new ArrayList<String>();
				
				for(int j = 0;j< phonemes.length;j++){
					
					if (j!=i)
						copy.add(phonemes[j]);
				}
				
					
				listsWords.add(WordSelectionUtils.getTargetWordsWithoutPhonemes(LanguageCode.EN, languageArea, selectedDifficulties.get(i), parameters.batchSize, parameters.wordLevel,i!=0, copy));

			}*/
			

		}else{
			
				
			return WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,
					false);
			
			//return WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel);
			
		}
		
		/*else{
			
			
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
		
		return result;*/

		
	}
	
	

	@Override
	public TypeFiller[] fillerTypes(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)|(lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)|(lA==LanguageAreasUK.BLENDS)){
			return new TypeFiller[]{TypeFiller.CLUSTER};
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new TypeFiller[]{TypeFiller.NONE};
			
		}else
			return new TypeFiller[]{TypeFiller.NONE};

	}


	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)|(lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)|(lA==LanguageAreasUK.BLENDS)){
			return new TypeAmount[]{TypeAmount.FEW,TypeAmount.HALF,TypeAmount.MANY};
		}else		
			return new TypeAmount[]{TypeAmount.NONE};//need some distractors
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)){
			
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};
			
		}else if((lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)){
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
			
		}else if((lA==LanguageAreasUK.CONFUSING)){
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};//,TtsType.WRITTEN2WRITTEN};
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
			return new int[]{4,7};//letter to letters
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
	
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==0){//not used
			return "Hit the letters that are read as the sound";
			
		}else if (param.mode==1){
			if(param.ttsType == TtsType.WRITTEN2WRITTEN){
				return "Hit the words that contain the phoneme";
			}else{
				return "Hit the words that contain the sound";
			}
			
		}else if(param.mode==2){
			return "Hit the words that end with the group of letters";
		}else if(param.mode==3){
			return "Hit the words that start with the group of letters";
		}else if(param.mode ==4){
			return "Hit the letters equal to the given letter";
		}else if(param.mode==6){//not used
			if(param.ttsType == TtsType.WRITTEN2WRITTEN){
				return "Hit the words that start with the letter";
			}else{
				return "Hit the words that start with the sound";
			}
		}else if(param.mode==7){
			if(param.ttsType == TtsType.WRITTEN2WRITTEN){
				return "Hit the words that contain with the letter";
			}else{
				return "Hit the words that contain with the sound";
			}
		}
		
		return "Instructions not available";
		

	
	}
}
