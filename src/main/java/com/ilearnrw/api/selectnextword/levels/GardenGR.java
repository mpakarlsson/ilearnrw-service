package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.StringMatchesInfo;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ilearnrw.api.selectnextword.TypeBasic;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;


/**
 * 
 * @author hector
 *
 *	Levels configuration for harvest / sorting potions
 *
 *	 syllables categories are first/second open/closed, confusing 4 letters
 *
 */
public class GardenGR extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {

		int originalSize = parameters.batchSize;
		if(parameters.mode!=4){
			originalSize+=4;//machines need also words
		}
		parameters.batchSize = 4+(parameters.batchSize*10);
		
		List<GameElement> candidates = WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.GR, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				false,//begins
				false);
		List<GameElement> results = new ArrayList<GameElement>();

		HashMap<String,List<GameElement>> sortedCandidates= new HashMap<String,List<GameElement>>();
		
					
			for(GameElement word : candidates){
				
				String key = "";
				if(parameters.mode==4){//FUnction words

					key = word.getAnnotatedWord().getType().name();
					
				}else if(parameters.mode==5){
	
					StringMatchesInfo match = ((AnnotatedWord)word.getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
					key = word.getAnnotatedWord().getWord().substring(match.getStart(),match.getEnd());				
					
				}else{//mode 6
					
					key = ""+word.getAnnotatedWord().getNumberOfSyllables();
				}
					
					
				if(!sortedCandidates.containsKey(key)){
					
					sortedCandidates.put(key , new ArrayList<GameElement>());
				}
				
				sortedCandidates.get(key).add(word);
			}
		
		
		while(sortedCandidates.size()>4){
				
				int minSize = Integer.MAX_VALUE;
				String index = null;
				for(String key : sortedCandidates.keySet()){
					
					if(sortedCandidates.get(key).size()<minSize){
						minSize = sortedCandidates.get(key).size();
						index = key;
					}
					
				}
				sortedCandidates.remove(index);
				
			}
			
			boolean done = false;
			
			while(!done){
				ArrayList<String> remove = new ArrayList<String>();
				for(String key : sortedCandidates.keySet()){
					results.add(sortedCandidates.get(key).get(0));
					sortedCandidates.get(key).remove(0);
					if(sortedCandidates.get(key).size()==0)
						remove.add(key);
					
					if(results.size()==originalSize){		
						done = true;
						break;
					}
				}
				
				for(String key : remove)
					sortedCandidates.remove(key);
				
				if(sortedCandidates.size()==0)
					break;
				
			}
			

		
		return results;
				
	}


	@Override
	public TypeBasic[] speedLevels(int languageArea, int difficulty) {
		return new TypeBasic[]{TypeBasic.LOW};

	}


	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		if(lA==LanguageAreasGR.FUNCTION_WORDS){
			return new int[]{4};
		}else if(lA==LanguageAreasGR.SYLLABLE_DIVISION){
			
			return new int[]{6};//words 2 words with the same number of syllables
		}else{
			return new int[]{5};//words 2 words, using the same matched difficulties		
			
		}
		
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
				return false;//true;
			case INFLECTIONAL://Syllables
				return true;//true;
			case PREFIXES://Suffixes
				return true;//true;
			case GP_CORRESPONDENCE://Prefixes
				return true;
			case FUNCTION_WORDS://Confusing letters
				return false;
			case LETTER_SIMILARITY:
				return false;
			default:
				return false;
		
		}
	}
}