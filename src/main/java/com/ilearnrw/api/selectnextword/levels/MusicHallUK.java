package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.annotation.AnnotatedWord;

import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;

import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.WordProblemInfo;

import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.GameSentence;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;

public class MusicHallUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		

		List<GameElement> result = new ArrayList<GameElement>();
		Random rand = new Random();
		
		ArrayList<String> sentences = new ArrayList<String>();
		ArrayList<String> answers = new ArrayList<String>();

	
		
		List<GameElement> targetWords =  WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel);
			
		if (targetWords.size()==0)
			return targetWords;
		
		
		if(parameters.fillerType==FillerType.CLUSTER){
			
			List<GameElement> distractors  = WordSelectionUtils.getClusterDistractors(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel , parameters.accuracy);
			for(GameElement ge : distractors){
				targetWords.add(ge);
				
			}

		}
				
		System.err.println("Create sentences");
		for(GameElement ge : targetWords){
			
			AnnotatedWord w = (AnnotatedWord) ge.getAnnotatedWord();

			if(LanguageAreasUK.values()[languageArea]==LanguageAreasUK.SYLLABLES){//Syllables

				
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
								

			}else{

				String sentence = "";
				

				WordProblemInfo correctAnswer = w.getWordProblems().get(0);
				
				sentence =    w.getWord().substring(0, correctAnswer.getMatched().get(0).getStart())+"{"+
							  w.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd())+"}"+
							  w.getWord().substring(correctAnswer.getMatched().get(0).getEnd());
				
				answers.add(w.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd()));
						
				
				
				sentences.add(sentence);
				
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
		
		if(fillerWords.size()==1){
			if(!fillerWords.contains("xh"))
				fillerWords.add("xh");
			else
				fillerWords.add("?");
				
		}
		result.add(new GameElement(new GameSentence(sentences.get(i), fillerWords)));
			
		}
		
		return result;
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if(lA==LanguageAreasUK.SYLLABLES){
			return new FillerType[]{FillerType.NONE};
		}else{
			return new FillerType[]{FillerType.CLUSTER};
		}
	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//10 words

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
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};
	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice; complete the word

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

}
