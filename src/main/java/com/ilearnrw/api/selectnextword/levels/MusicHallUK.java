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
		
		if(LanguageAreasUK.values()[languageArea]==LanguageAreasUK.SYLLABLES){//Syllables
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
			
			Random rand = new Random();

			
			ArrayList<String> sentences = new ArrayList<String>();
			ArrayList<String> answer = new ArrayList<String>();
			
			for(String word : targetWords){
				
				EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
				
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
				
				
				result.add(new GameElement(new GameSentence(sentences.get(i), fillerWords)));
				
			}
			

			
		}else{
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
			
			Random rand = new Random();

			
			ArrayList<String> sentences = new ArrayList<String>();
			ArrayList<String> answer = new ArrayList<String>();
			
			for(String word : targetWords){
				
				EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
				AnnotatedWord aw = new AnnotatedWord(ew,languageArea,difficulty);
				
				WordProblemInfo correctAnswer = aw.getWordProblems().get(0);
				
				sentences.add(    ew.getWord().substring(0, correctAnswer.getMatched().get(0).getStart())+"{"+
								  ew.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd())+"}"+
								  ew.getWord().substring(correctAnswer.getMatched().get(0).getEnd()));
				
				answer.add(correctAnswer.getMatched().get(0).getMatchedPart() );
						
				
				
			}
			
			
			if(parameters.fillerType==FillerType.CLUSTER)
			
			{
				

				ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

				int targetCluster = definitions.getProblemDescription(languageArea, difficulty).getCluster();
				ArrayList<Integer> candidates = new ArrayList<Integer>();
				
				for(int ii = 0; ii< definitions.getRowLength(languageArea);ii++){
					
					if(ii!=difficulty)
						if(definitions.getProblemDescription(languageArea,ii).getCluster()==targetCluster)
							candidates.add(ii);
				}
				
				for(int i=0;i<parameters.accuracy;i++){
					
					int randomDifficulty = candidates.get(rand.nextInt(candidates.size()));
					
					ArrayList<String> otherWords = new ProblemWordListLoader(LanguageCode.EN, languageArea, randomDifficulty).getItems();
					EasyHardList otherList = new EasyHardList(otherWords);
					String wordFromOtherDifficulty = otherList.getRandom(1, parameters.wordLevel).get(0);
					
					EnglishWord ew =  new EnglishWord(wordFromOtherDifficulty.split("\'")[0]);
					AnnotatedWord aw = new AnnotatedWord(ew,languageArea,difficulty);
					
					WordProblemInfo correctAnswer = aw.getWordProblems().get(0);
					
					sentences.add(    ew.getWord().substring(0, correctAnswer.getMatched().get(0).getStart())+"{"+
									  ew.getWord().substring(correctAnswer.getMatched().get(0).getStart(), correctAnswer.getMatched().get(0).getEnd())+"}"+
									  ew.getWord().substring(correctAnswer.getMatched().get(0).getEnd()));
					
					answer.add(correctAnswer.getMatched().get(0).getMatchedPart() );

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
				
				
				result.add(new GameElement(new GameSentence(sentences.get(i), fillerWords)));
				
			}
			
		}

		
		
		return result;
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.CLUSTER};

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
