package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
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



/**
 * 
 * @author hector
 *
 *	Levels configuration for serenade hero / fix the footpath / bridge builder
 *
 *	prefix/suffix/vowels/consonants difficulty missing, syllables one random syllable missing
 *
 */

public class MusicHallUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		

		List<GameElement> result = new ArrayList<GameElement>();
		Random rand = new Random();
		
		ArrayList<String> sentences = new ArrayList<String>();
		ArrayList<String> answers = new ArrayList<String>();

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		List<GameElement> targetWords;
		
		if(lA==LanguageAreasUK.SYLLABLES){
			
			targetWords =  WordSelectionUtils.getTargetWordsWithSyllables(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel,1);
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
				
			}
			
		}else{
		
			targetWords =  WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel);

			if (targetWords.size()==0)
				return targetWords;
					
		System.err.println(parameters.accuracy);
			List<List<GameElement>> distractors  = WordSelectionUtils.getDistractors(LanguageCode.EN,  languageArea, difficulty, parameters.batchSize, parameters.wordLevel ,parameters.accuracy,-1,new ArrayList<String>());
				
					
			for(List<GameElement> lge : distractors){
				for(GameElement ge : lge)
					targetWords.add(ge);
				
			}

				

			for(GameElement ge : targetWords){
			
				AnnotatedWord w = (AnnotatedWord) ge.getAnnotatedWord();

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
			
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!fillerWords.contains("ad")){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", "ad");
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add("ad");
				}
				
			}
		}
		
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!fillerWords.contains("tion")){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", "tion");
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add("tion");
				}
				
			}
		}
		
		if(fillerWords.size()<parameters.accuracy+1){//weird combination of letters
			if(!fillerWords.contains("ww")){

				String newWord = sentences.get(i).replace("{"+answers.get(i)+"}", "ww");
				if(!EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
					
					fillerWords.add("ww");
				}
				
			}
		}
		
		
		result.add(new GameElement(new GameSentence(sentences.get(i), fillerWords)));
			
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
		return new int[]{1,2,3};//Number of fillers and number of difficulties from which take distractors (when needed)

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
