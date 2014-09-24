package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
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

public class GardenUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		List<GameElement> result = new ArrayList<GameElement>();

		ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems();
		EasyHardList list = new EasyHardList(words);
		
		ArrayList<String> targetWords = list.getRandom(parameters.batchSize, parameters.wordLevel);
		
		Random rand = new Random();
		for(String word : targetWords){
			
			EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
			result.add(new GameElement(false,ew,languageArea, difficulty));
			
		}
		
		
		if(parameters.fillerType==FillerType.PREVIOUS){
		
			for(String word : targetWords){
			
				int randomDifficulty = rand.nextInt(difficulty+1);
				ArrayList<String> otherWords = new ProblemWordListLoader(LanguageCode.EN, languageArea, randomDifficulty).getItems();
				EasyHardList otherList = new EasyHardList(otherWords);
				String fillerWord = otherList.getRandom(1, parameters.wordLevel).get(0);
				
				EnglishWord ew =  new EnglishWord(fillerWord.split("\'")[0]);
				result.add(new GameElement(true, ew,languageArea, randomDifficulty));

			}
			
		}else if(parameters.fillerType==FillerType.CLUSTER){
			
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

			int targetCluster = definitions.getProblemDescription(languageArea, difficulty).getCluster();
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			
			for(int ii = 0; ii< definitions.getRowLength(languageArea);ii++){
				
				if(ii!=difficulty)
					if(definitions.getProblemDescription(languageArea,ii).getCluster()==targetCluster)
						candidates.add(ii);
			}
			
			for(String word : targetWords){
				
				int randomDifficulty = candidates.get(rand.nextInt(candidates.size()));
				ArrayList<String> otherWords = new ProblemWordListLoader(LanguageCode.EN, languageArea, randomDifficulty).getItems();
				EasyHardList otherList = new EasyHardList(otherWords);
				String fillerWord = otherList.getRandom(1, parameters.wordLevel).get(0);
				
				EnglishWord ew =  new EnglishWord(fillerWord.split("\'")[0]);
				result.add(new GameElement(true, ew,languageArea, randomDifficulty));

			}
			
			
		}

		return result;
	
	
	
	}
	


	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0,1};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if (languageArea==LanguageAreasUK.CONFUSING){
			return new FillerType[]{FillerType.NONE};
		}else{
			return new FillerType[]{FillerType.CLUSTER,FillerType.PREVIOUS};
		}
			
			
	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{5};//5 words

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice
	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{0};//No choice
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		if(languageArea==0){//for syllabification: open and closed syllables
			return new int[]{0};
		}else
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
}
