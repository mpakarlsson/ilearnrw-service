package com.ilearnrw.api.selectnextword.levels;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.textclassification.WordProblemInfo;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.GameSentence;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;



/**
 * 
 * @author hector
 *
 *	Levels configuration for serenade hero / fix the footpath / bridge builder
 *
 *	prefix/suffix/vowels/consonants difficulty missing, syllables one random syllable missing
 *
 */

public class MusicHallUK extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		

		List<GameElement> result = new ArrayList<GameElement>();
		Random rand = new Random();
		
		ArrayList<String> sentences = new ArrayList<String>();
		ArrayList<String> answers = new ArrayList<String>();

		ArrayList<Integer> languageAreas = new ArrayList<Integer>();
		ArrayList<Integer> difficulties = new ArrayList<Integer>();
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		List<GameElement> targetWords;
		
		if(lA==LanguageAreasUK.SYLLABLES){
			

			targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.EN, 
					languageArea, 
					difficulty,
					parameters,
					1,
					false,
					false);
			
			
			
			if (targetWords.size()==0)
				return targetWords;
			
			
			for(GameElement ge : targetWords){
				AnnotatedWord w = (AnnotatedWord) ge.getAnnotatedWord();
				int syllable = rand.nextInt(w.getNumberOfSyllables());
				
				String sentence = "";
				
				for(int j=0;j<w.getNumberOfSyllables();j++){
					
					if (j==syllable){
							sentence += "{"+w.getSyllables()[j]+"}";
							answers.add(w.getSyllables()[j]);
					}else{
							sentence += w.getSyllables()[j];
						
					}
					
				}
				
				
				sentences.add(sentence);
				languageAreas.add(((AnnotatedWord)ge.getAnnotatedWord()).getWordProblems().get(0).getCategory());
				difficulties.add(((AnnotatedWord)ge.getAnnotatedWord()).getWordProblems().get(0).getIndex());
				
			}
			
		}else{
			
			targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.EN, 
					languageArea, 
					difficulty,
					parameters,
					-1,
					false,
					false);				

			for(GameElement ge : targetWords){
			
				AnnotatedWord w = (AnnotatedWord) ge.getAnnotatedWord();

				String sentence = "";
				
				WordProblemInfo correctAnswer = w.getWordProblems().get(0);
				
				sentence =    w.getWord().substring(0, correctAnswer.getMatched().get(0).getStart())+"{"+
							  w.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd())+"}"+
							  w.getWord().substring(correctAnswer.getMatched().get(0).getEnd());
				
				answers.add(w.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd()));

				sentences.add(sentence);
				languageAreas.add(((AnnotatedWord)ge.getAnnotatedWord()).getWordProblems().get(0).getCategory());
				difficulties.add(((AnnotatedWord)ge.getAnnotatedWord()).getWordProblems().get(0).getIndex());
				
			}
			
			
		}
		
		
	for (int i=0;i<sentences.size();i++){
		
		ArrayList<String> fillerWords = new ArrayList<String>();
		
		fillerWords.add(answers.get(i));
		
		for(int j=0;j<answers.size();j++){
			
			if( !fillerWords.contains(answers.get(j))){
				
				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", answers.get(j));
				
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
				
					fillerWords.add(answers.get(j));
				
					if(fillerWords.size()==(parameters.accuracy+1))
						break;
				}
				
			}
			
		}
		
		
		
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!answers.contains(answers.get(i).replace("d","t"))){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", answers.get(i).replace("d","t"));
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add(answers.get(i).replace("d","t"));
				}
				
			}
		}
		
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!answers.contains(answers.get(i).replace("e","i"))){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", answers.get(i).replace("e","i"));
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add(answers.get(i).replace("e","i"));
				}
				
			}
		}
		
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!answers.contains(answers.get(i).replace("s","r"))){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", answers.get(i).replace("s","r"));
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add(answers.get(i).replace("s","r"));
				}
				
			}
		}
		
		for(String weird : new String[]{"ou","min","lap","ack","ad","tion","str","ww"}){
			
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!fillerWords.contains(weird)){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", weird);
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add(weird);
				}
				
			}
		}
		}
		
		
		
		result.add(new GameElement(new GameSentence(sentences.get(i), fillerWords, languageAreas.get(i),difficulties.get(i))));
			
		}
		
		return result;
	}




	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{1,2,3};//Number of alternatives

	}


	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice; complete the word

	}

	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		
			return new TypeAmount[]{TypeAmount.FEW,TypeAmount.HALF,TypeAmount.MANY};//At least one distractor
		
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
				return true;
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
	
	/* Instructions for the games */
	@Override
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==0){
			return "Fill in the missing parts of the word";
			
		}
		
		return "Instructions not available";
		
	}

}
