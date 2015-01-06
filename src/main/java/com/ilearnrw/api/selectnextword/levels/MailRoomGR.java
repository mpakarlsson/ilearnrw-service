package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
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
 *	Levels configuration for mail sorter/ pelmanism
 *	Need to send batches of 3-4 words (1 target word plus 2-3 distractors)
 *  For all difficulties, find words from a single difficulty, and reorder
 *   the results to make sure the matching difficulties are different within
 *   the same batch
 *
 */
public class MailRoomGR extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {
		
		
		if(parameters.accuracy==2){
			parameters.amountDistractors=TypeAmount.HALF;
		}else if(parameters.accuracy==3){
			parameters.amountDistractors=TypeAmount.MANY;
		}else if((parameters.amountDistractors==TypeAmount.NONE)||(parameters.amountDistractors==TypeAmount.FEW)){
				parameters.amountDistractors=TypeAmount.MANY;
		}
		
		
		List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.GR, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				false,
				true);//only one pattern
		
		
		//TODO
		
		List<GameElement> results = new ArrayList<GameElement>();
		
		for(int i=0;i<parameters.batchSize/4;i++){
			
			List<String> patterns = new ArrayList<String>();
			boolean invalid = false;
			
			for(int j=0;j<parameters.amountDistractors.ordinal()+1;j++){
				
				int attempts = targetWords.size()-((i*(parameters.amountDistractors.ordinal()+1))+j);
				
				while(attempts>0){
				
					AnnotatedWord word = (AnnotatedWord)targetWords.get((i*(parameters.amountDistractors.ordinal()+1))+j).getAnnotatedWord();
				
					int start =word.getWordProblems().get(0).getMatched().get(0).getStart();
					int end =word.getWordProblems().get(0).getMatched().get(0).getEnd();
				
					String pattern = word.getWord().substring(start, end);
					if(!patterns.contains(pattern)){
						patterns.add(pattern);
						break;
					}else{//move word to the end of the list
						targetWords.add(targetWords.get((i*(parameters.amountDistractors.ordinal()+1))+j));
						targetWords.remove((i*(parameters.amountDistractors.ordinal()+1))+j);
						attempts--;
					}
				}
				
				if (attempts==0){//did not complete the batch
					invalid = true;

				}
			}
					
			if (!invalid){//this batch is correct, copy it to the results
				for(int j=0;j<parameters.amountDistractors.ordinal()+1;j++){
					results.add(targetWords.get((i*(parameters.amountDistractors.ordinal()+1))+j));
				}
				
				System.err.println("GOOD BATCH");

			}else{
				
				System.err.println("INVALID BATCH");
			}
			
			
		}
		
		return results;
		
		
		
	}
	
	/*
	@Override
	public TypeFiller[] fillerTypes(int languageArea, int difficulty){
		return new TypeFiller[]{TypeFiller.NONE};//Distractors within difficulty

	}*/
	
	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		return new TypeAmount[]{TypeAmount.HALF,TypeAmount.MANY};//2 or 3 distractors
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
	
	/* Instructions for the games */
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(languageArea==3)
			return "Send the words to the basket starting the letter cluster";
		else if(languageArea==6){
			return "Send the words to the right suffix basket";
		}else if(languageArea==7)
			return "Send the words to the right suffix basket";

		
		return "Not available";
	}
	
	
	

}
