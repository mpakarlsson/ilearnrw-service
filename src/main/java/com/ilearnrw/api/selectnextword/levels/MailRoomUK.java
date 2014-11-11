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
 *	Levels configuration for mail sorter/ pelmanism
 *	Need to send batches of 3-4 words (1 target word plus 2-3 distractors)
 *	Consonant/vowels will match phoneme to word; Prefixes/suffixes the part to the word
 *
 */

public class MailRoomUK implements GameLevel {

	

	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		//int difficulties[] = new int[1+parameters.accuracy];
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		List<List<GameElement>> listsWords = new ArrayList<List<GameElement>>();

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS)){

			//Find compatible phonetic difficulties
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

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

			}
			

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
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS))
			return new int[]{0};//Take phoneme or grapheme on matched difficulty
		else if(lA==LanguageAreasUK.SUFFIXES){
			return new int[]{1};//Suffix
		}else if(lA==LanguageAreasUK.PREFIXES){
			return new int[]{2};//Prefix
		}else if(lA==LanguageAreasUK.BLENDS){
			return new int[]{3};//Prefix
		}else
			return new int[]{0};
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0};//Easy and hard
	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {

		return new FillerType[]{FillerType.CLUSTER};

	}


	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{5};//5 rounds of packages
	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {

		return new int[]{0,1,2};//Slow, medium and fast
	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{2,3};//Number of fillers
	}


	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS))

			return new TtsType[]{TtsType.SPOKEN2WRITTEN};//TtsType.WRITTEN2WRITTEN,

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
			return true;
		case SYLLABLES://Syllables
			return false;
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
