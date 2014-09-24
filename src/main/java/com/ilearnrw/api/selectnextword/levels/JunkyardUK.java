package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class JunkyardUK implements GameLevel {



	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.NONE};
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
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if(lA==LanguageAreasUK.SYLLABLES)
			return new int[]{2,3,4};//Number syllables
		else
			return new int[]{0};//No choice
			
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN,TtsType.WRITTEN2SPOKEN};

	}
	
	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.SYLLABLES))
			return new int[]{0};//Syllables
		else if(lA==LanguageAreasUK.SUFFIXES)
			return new int[]{1};//Suffixes
		else if(lA==LanguageAreasUK.PREFIXES)
			return new int[]{2};//Prefixes
		else
			return new int[]{3};//Vowels & consonants; use graphemes

	}
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {

		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();

		if (languageArea==LanguageAreasUK.SYLLABLES){//Syllable division
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			List<String> wordList  = list.getRandom(parameters.batchSize*3, parameters.wordLevel);

		
			/*if(parameters.wordLevel==0)
				wordList= list.getEasy();
			else
				wordList= list.getHard();
			*/
			ArrayList<EnglishWord> reserve = new ArrayList<EnglishWord>();
			
			//Add words with the desired number of syllables
			for (int i=0;i<wordList.size();i++){
				EnglishWord aux = new EnglishWord(wordList.get(i).split("\'")[0]);
				
				if (aux.getNumberOfSyllables()==parameters.accuracy){
					result.add(new GameElement(false, aux, lA, difficulty));
				}else if(aux.getNumberOfSyllables()>1){
					reserve.add(aux);
				}
				
				if(result.size()==parameters.batchSize)
					break;
				
			}
			
			//Add words of other lengths if not enough
			if (result.size()!=parameters.batchSize){
				for (EnglishWord ew : reserve){
						result.add(new GameElement(false, ew, lA, difficulty));
						if(result.size()==parameters.batchSize)
							break;
					
				}
			}
			
			
			if(result.size()==0){
				
				result.add(new GameElement(false, new EnglishWord("@@@@@@"), lA, difficulty));
			}
			
			
		}else{
			
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			ArrayList<String> targetWords = list.getRandom(parameters.batchSize*3, parameters.wordLevel);
			
			Random rand = new Random();
			for(String word : targetWords){
				
				EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
				if (ew.getNumberOfSyllables()>1) 
					result.add(new GameElement(false,ew,lA, difficulty));
				
				if(result.size()==parameters.batchSize)
					break;
				
				if(result.size()==0){
					
					result.add(new GameElement(false, new Word("@@@@@@"), lA, difficulty));
				}
				
				/*if(parameters.fillerType==FillerType.PREVIOUS){
				
					int randomDifficulty = rand.nextInt(difficulty);
					ArrayList<String> otherWords = new ProblemWordListLoader(LanguageCode.EN, languageArea, randomDifficulty).getItems();
					EasyHardList otherList = new EasyHardList(otherWords);
					String fillerWord = otherList.getRandom(1, parameters.wordLevel).get(0);
					ew =  new EnglishWord(fillerWord.split("\'")[0]);
					if (ew.getNumberOfSyllables()>1) result.add(new GameElement(true, ew,languageArea, randomDifficulty));

				}*/
				
			}		
			
			
		}
		return result;		
		


			

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
