package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.annotation.AnnotatedWord;

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
 *	Levels configuration for mail sorter/ pelmanism
 *	Need to send batches of 3-4 words (1 target word plus 2-3 distractors)
 *  For all difficulties, find words from a single difficulty, and reorder
 *   the results to make sure the matching difficulties are different within
 *   the same batch
 *
 */
public class MailRoomGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {

		List<GameElement> targetWords = WordSelectionUtils.getTargetWordsBegins(LanguageCode.GR, languageArea, difficulty,parameters.batchSize*(parameters.accuracy+1), parameters.wordLevel,false,true);
		List<GameElement> results = new ArrayList<GameElement>();
		
		for(int i=0;i<parameters.batchSize;i++){
			
			List<String> patterns = new ArrayList<String>();
			boolean invalid = false;
			
			for(int j=0;j<parameters.accuracy+1;j++){
				
				int attempts = targetWords.size()-((i*(parameters.accuracy+1))+j);
				
				while(attempts>0){
				
					AnnotatedWord word = (AnnotatedWord)targetWords.get((i*(parameters.accuracy+1))+j).getAnnotatedWord();
				
					int start =word.getWordProblems().get(0).getMatched().get(0).getStart();
					int end =word.getWordProblems().get(0).getMatched().get(0).getEnd();
				
					String pattern = word.getWord().substring(start, end);
					if(!patterns.contains(pattern)){
						patterns.add(pattern);
						break;
					}else{//move word to the end of the list
						targetWords.add(targetWords.get((i*(parameters.accuracy+1))+j));
						targetWords.remove((i*(parameters.accuracy+1))+j);
						attempts--;
					}
				}
				
				if (attempts==0){//did not complete the batch
					invalid = true;

				}
			}
					
			if (!invalid){//this batch is correct, copy it to the results
				for(int j=0;j<parameters.accuracy+1;j++){
					results.add(targetWords.get((i*(parameters.accuracy+1))+j));
				}
				
				System.err.println("GOOD BATCH");

			}else{
				
				System.err.println("INVALID BATCH");
			}
			
			
		}
		
		return results;
		
		
		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0};
	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {

		return new FillerType[]{FillerType.NONE};

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

		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA==LanguageAreasGR.GP_CORRESPONDENCE))
			return new TtsType[]{TtsType.WRITTEN2SPOKEN};
		else
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
	}


	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		return new int[]{3};
		
		/*LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA==LanguageAreasGR.CONSONANTS)| (lA==LanguageAreasGR.VOWELS))
			return new int[]{0};//Take phoneme or grapheme on matched difficulty
		else if((lA==LanguageAreasGR.INFLECTIONAL)||(lA==LanguageAreasGR.DERIVATIONAL)){
			return new int[]{1};//Suffix
		}else if(lA==LanguageAreasGR.PREFIXES){
			return new int[]{2};//Prefix
		}else
			return new int[]{3};//GP correspondence
			*/
	}

	
	@Override
	public boolean allowedDifficulty(int lA, int difficulty) {
		
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		switch(languageArea){
		
		case SYLLABLE_DIVISION://Consonants
			return false;		
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
			return true;
		case FUNCTION_WORDS://Confusing letters
			return false;//true;
		case LETTER_SIMILARITY:
			return false;
		default:
			return false;
	
	}
	}

}
