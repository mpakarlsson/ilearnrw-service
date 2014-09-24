package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.WordProblemInfo;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
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
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class MusicHallUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		

		List<GameElement> result = new ArrayList<GameElement>();
		Random rand = new Random();
		
		ArrayList<String> sentences = new ArrayList<String>();
		ArrayList<String> answer = new ArrayList<String>();

		if(LanguageAreasUK.values()[languageArea]==LanguageAreasUK.SYLLABLES){//Syllables
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			ArrayList<String> targetWords = list.getRandom(parameters.batchSize*3, parameters.wordLevel);
			
			
			for(String word : targetWords){
				
				EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
				
				if (ew.getNumberOfSyllables()>1){
				
					int syllable = rand.nextInt(ew.getNumberOfSyllables());
				
					String sentence = "";

				
					for(int i=0;i<ew.getNumberOfSyllables();i++){
						if (i==syllable){
							sentence += "{"+ew.getSyllables()[i]+"}";
							answer.add(ew.getSyllables()[i]);
						}else{
							sentence += ew.getSyllables()[i];
						
						}
					
					}
				
					sentences.add(sentence);
					
					if (sentences.size()==parameters.batchSize)
						break;
				}
								
			}
			
			
		}else{
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
			

			
			for(String word : targetWords){
				
				
				EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
				AnnotatedWord aw = new AnnotatedWord(ew,languageArea,difficulty);
				
				WordProblemInfo correctAnswer = aw.getWordProblems().get(0);
				
				sentences.add(    ew.getWord().substring(0, correctAnswer.getMatched().get(0).getStart())+"{"+
								  ew.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd())+"}"+
								  ew.getWord().substring(correctAnswer.getMatched().get(0).getEnd()));
				
				answer.add(ew.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd()));
						
				
				
			}
			
		}
		if(parameters.fillerType==FillerType.CLUSTER){

				ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

				int targetCluster = definitions.getProblemDescription(languageArea, difficulty).getCluster();
				ArrayList<Integer> candidates = new ArrayList<Integer>();
				
				for(int ii = 0; ii< definitions.getRowLength(languageArea);ii++){
					
					if(ii!=difficulty)
						if(definitions.getProblemDescription(languageArea,ii).getCluster()==targetCluster)
							candidates.add(ii);
				}
				
				if(candidates.size()==0){
					for(int ii = 0; ii< difficulty;ii++){
						candidates.add(ii);
					}					
					
				}
				
				if(candidates.size()==0){
					for(int ii =difficulty+1; ii< definitions.getRowLength(languageArea);ii++){
						candidates.add(ii);
					}					
					
				}
				
				for(int i=0;i<parameters.batchSize;i++){
					
					int randomDifficulty = candidates.get(rand.nextInt(candidates.size()));
					
					ArrayList<String> otherWords = new ProblemWordListLoader(LanguageCode.EN, languageArea, randomDifficulty).getItems();
					EasyHardList otherList = new EasyHardList(otherWords);
					
					ArrayList<String> moreWords = otherList.getRandom(1, parameters.wordLevel);
					if(moreWords.size()==0)
						continue;
					
					String wordFromOtherDifficulty = moreWords.get(0);
					
					EnglishWord ew =  new EnglishWord(wordFromOtherDifficulty.split("\'")[0]);
					AnnotatedWord aw = new AnnotatedWord(ew,languageArea,randomDifficulty);
					
					WordProblemInfo correctAnswer = aw.getWordProblems().get(0);
					
					sentences.add(    ew.getWord().substring(0, correctAnswer.getMatched().get(0).getStart())+"{"+
									  ew.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd())+"}"+
									  ew.getWord().substring(correctAnswer.getMatched().get(0).getEnd()));
					
					answer.add(ew.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd()) );

				}
			}
	
	for (int i=0;i<sentences.size();i++){
		
		ArrayList<String> fillerWords = new ArrayList<String>();
		fillerWords.add(answer.get(i));
		
		for(int j=0;j<answer.size();j++){
			
			if( !fillerWords.contains(answer.get(j))){
				
				fillerWords.add(answer.get(j));
				
				if(fillerWords.size()==(parameters.accuracy+1))
					break;
			}
			
		}
		
		if(fillerWords.size()==1){
			if(!fillerWords.contains("ass"))
				fillerWords.add("ass");
			else
				fillerWords.add("sa");
				
		}
		result.add(new GameElement(new GameSentence(sentences.get(i), fillerWords)));
		

			
		}

		if (result.size()==0){
			result.add(new GameElement(new GameSentence("@@@{@}@@", new ArrayList<String>(){{add("@");}})));

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
