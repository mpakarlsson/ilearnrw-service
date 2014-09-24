package com.ilearnrw.api.selectnextword.levels;
import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;


public class TrainStationUK implements GameLevel {


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
		return new int[]{5};//5 packages

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0,1,2};//Slow, medium and fast

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{0};//Nothing

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}
	
	
	@Override
	public int[] modeLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if(languageArea==LanguageAreasUK.SYLLABLES)
			return new int[]{0};//Syllables
		else if(languageArea==LanguageAreasUK.SUFFIXES)
			return new int[]{1};//Suffixes
		else if(languageArea==LanguageAreasUK.PREFIXES)
			return new int[]{2};//Prefixes
		else
			return new int[]{3};//Graphemes

	}
	
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		List<GameElement> result = new ArrayList<GameElement>();

		ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems();
		EasyHardList list = new EasyHardList(words);
		
		ArrayList<String> targetWords = list.getRandom(parameters.batchSize*3, parameters.wordLevel);
		
		for(String word : targetWords){
			
			EnglishWord ew =  new EnglishWord(word.split("\'")[0]);
			if (ew.getNumberOfSyllables()>1) {
				result.add(new GameElement(false,ew,languageArea, difficulty));
				if(result.size()==parameters.batchSize)
					break;
			}
		
		}
		
		if(targetWords.size()==0)
			result.add(new GameElement(false,new EnglishWord("@@@@@"),languageArea, difficulty));
		
		if(result.size()==0)
			result.add(new GameElement(false,new EnglishWord("#####"),languageArea, difficulty));

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
				return false;
			case CONFUSING://Confusing letters
				return false;
			default:
				return false;
		
		}
	}

}
