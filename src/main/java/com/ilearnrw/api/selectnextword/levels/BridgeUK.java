package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.WordProblemInfo;
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

public class BridgeUK implements GameLevel {
	
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {

		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();

		
		 if( (languageArea==LanguageAreasUK.VOWELS) | (languageArea==LanguageAreasUK.CONSONANTS)){//vowel or consonant sounds// Use matching difficulty
			 
			 ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();
				EasyHardList list = new EasyHardList(words);
				
				ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
				
				
				for(String word : targetWords){
					EnglishWord ew =  new EnglishWord(word.split("\'")[0]);

					result.add(new GameElement(false, ew, lA, difficulty));					
					
				}
			 
		 }else if(languageArea==LanguageAreasUK.SYLLABLES){//desired number of syllables
			 
				ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();
				EasyHardList list = new EasyHardList(words);
				
				List<String> wordList;
			
				wordList= list.getRandom(parameters.batchSize*3, parameters.wordLevel);
				
				
				ArrayList<EnglishWord> reserve = new ArrayList<EnglishWord>();
				
				//Add words with the desired number of syllables
				for (int i=0;i<wordList.size();i++){
					EnglishWord aux = new EnglishWord(wordList.get(i).split("\'")[0]);
					
					if (aux.getNumberOfSyllables()==parameters.accuracy){
						result.add(new GameElement(false, aux, lA, difficulty));
					}else{
						reserve.add(aux);
					}
					if(result.size()==parameters.batchSize)
						break;
					
				}
				
				//Add words of other lengths if not enough
				if (result.size()!=parameters.batchSize){
					for (EnglishWord ew : reserve){
						if (ew.getNumberOfSyllables()>1){
							result.add(new GameElement(true, ew, lA, difficulty));
							if(result.size()==parameters.batchSize)
								break;
						}
						
					}
				}			 
			 
			 
		 }else{//any syllables
			 
				ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();
				EasyHardList list = new EasyHardList(words);
				
				ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
				
				for(String word : targetWords){
					
					EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
					result.add(new GameElement(false,ew,lA, difficulty));
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
		return new FillerType[]{FillerType.NONE};
	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//10 words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice

	}

	@Override
	public int[] accuracyLevels(int lA, int difficulty) {
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if(languageArea==LanguageAreasUK.SYLLABLES)
			return new int[]{2,3,4};//Number syllables
		else
			return new int[]{0};//No choice

	}

	@Override
	public TtsType[] TTSLevels(int lA, int difficulty) {
		
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if( (languageArea==LanguageAreasUK.CONSONANTS) | (languageArea==LanguageAreasUK.VOWELS)){//vowel or consonant
			
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};
			
		}else
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};


		
	}

	@Override
	public int[] modeLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		if (languageArea == LanguageAreasUK.SYLLABLES){//Random syllable
			return new int[]{0};//match 1,2,3.. syllables
		}else if(languageArea == LanguageAreasUK.SUFFIXES){//suffixing
			return new int[]{1};//match last syllable
		}else if(languageArea == LanguageAreasUK.PREFIXES){
			return new int[]{2};//match first syllable
		}else if( (languageArea==LanguageAreasUK.CONSONANTS) | (languageArea==LanguageAreasUK.VOWELS)){
			return new int[]{4};//match vowel or consonant sound
		}else
			return new int[]{0};
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
				return false;
			case CONFUSING://Confusing letters
				return false;
			default:
				return false;
		
		}
	}

}
