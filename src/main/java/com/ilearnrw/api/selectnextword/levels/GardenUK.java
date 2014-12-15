package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.TypeBasic;
import com.ilearnrw.api.selectnextword.TypeFiller;
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
 *	prefix/suffix/vowels categories are characters, syllables categories are first/second open/closed, confusing 4 letters
 *
 */

public class GardenUK extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		
		return WordSelectionUtils.getTargetWordsWithDistractors(
				LanguageCode.EN, 
				 languageArea, 
				 difficulty,
				 parameters,
				-1,
				false,//begins
				false);
		
		
		/*int numberTargets = (int)java.lang.Math.ceil(parameters.batchSize/2.0);
		int numberDistractorsPerDifficulty = (int)java.lang.Math.floor(((double)(parameters.batchSize-numberTargets))/parameters.accuracy);
		if (numberDistractorsPerDifficulty==0)
			numberDistractorsPerDifficulty++;
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		List<GameElement> result = new ArrayList<GameElement>();

		
		if(lA==LanguageAreasUK.SYLLABLES){
			
			
			result = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,numberTargets, parameters.wordLevel);	
		
			if (result.size()==0)
				return result;
		
		
			List<List<GameElement>> distractors = WordSelectionUtils.getDistractors(LanguageCode.EN, languageArea, difficulty,numberDistractorsPerDifficulty, parameters.wordLevel,parameters.accuracy,-1,new ArrayList<String>());	
		
			for(List<GameElement> lge : distractors)
				for (GameElement f : lge)
					result.add(f);	
			
		}else if(lA==LanguageAreasUK.CONFUSING){
			
			result = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty,numberTargets, parameters.wordLevel);	
			
			if (result.size()==0)
				return result;
		
		
			List<List<GameElement>> distractors = WordSelectionUtils.getDistractors(LanguageCode.EN, languageArea, difficulty,numberDistractorsPerDifficulty, parameters.wordLevel,parameters.accuracy,-1,new ArrayList<String>());	
		
			for(List<GameElement> lge : distractors)
				for (GameElement f : lge)
					result.add(f);					
			
		}else{
			//TODO routine to pull words from different characters
			
			List<Integer> difficulties = WordSelectionUtils.findDifferentCharacter(LanguageCode.EN,  languageArea, difficulty,3);
			
			for(int i = 0; i<difficulties.size();i++){
				
				List<GameElement> aux = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulties.get(i),parameters.batchSize, parameters.wordLevel,i!=0);	
				for(GameElement ge : aux)
					result.add(ge);
			}	
			
		}
		
		return result;
		
		*/
		
		
		/*List<GameElement> resultComplete = new ArrayList<GameElement>();
		
		for(GameElement ge : result){
			
			resultComplete.add(new GameElement(ge.isFiller(),ge.getAnnotatedWord()));
			
		}*/
		

	
	}
	

	@Override
	public TypeFiller[] fillerTypes(int languageArea, int difficulty){
		if((LanguageAreasUK.values()[languageArea]==LanguageAreasUK.CONFUSING)||(LanguageAreasUK.values()[languageArea]==LanguageAreasUK.SYLLABLES))
			return new TypeFiller[]{TypeFiller.CLUSTER};
		else
			return new TypeFiller[]{TypeFiller.CHARACTER};

	}
	

	@Override
	public TypeBasic[] speedLevels(int languageArea, int difficulty) {
		return new TypeBasic[]{TypeBasic.LOW};//No choice
	}

	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		
		if(LanguageAreasUK.values()[languageArea]==LanguageAreasUK.CONFUSING){
			return new TypeAmount[]{TypeAmount.FEW,TypeAmount.HALF,TypeAmount.MANY};//At least one distractor
		}else{
			return new TypeAmount[]{TypeAmount.MANY};//3 distractors

		}
	}
	

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		
		if(lA==LanguageAreasUK.SYLLABLES){//for syllabification: open and closed syllables
			return new int[]{0,6};
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new int[]{2};

		}else//Prefix Suffix & vowels
			return new int[]{1};//based on the character
	}
	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		
		
		if(languageArea>=LanguageAreasUK.values().length)
			return false;
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		switch(lA){		
			case CONSONANTS://Consonants
				return false;
			case VOWELS://Vowels
				return true;
			case BLENDS://Blends and letter patterns
				return false;
			case SYLLABLES://Syllables
				return true;
			case SUFFIXES://Suffixes
				return true;
			case PREFIXES://Prefixes
				return false;
			case CONFUSING://Confusing letters
				return true;
			default:
				return false;
		
		}
	}
	
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode<2){
			return "Select the categories that correspond to each word";
			
		}else if (param.mode==2){
			return "Select the machines with letters contained in each word";
			
		}else if(param.mode==6){
			return "Select the machine with the right number of syllables";
		}
		
		return "Instructions not available";
		
	}
	
	
}
