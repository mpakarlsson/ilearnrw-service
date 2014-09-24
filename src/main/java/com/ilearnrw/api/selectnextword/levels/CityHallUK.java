package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.Word;
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

public class CityHallUK implements GameLevel {


	@Override
	public int[] modeLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if((languageArea==LanguageAreasUK.VOWELS) | (languageArea==LanguageAreasUK.CONSONANTS)){	
			return new int[]{0};//phoneme of difficulty is the pattern; letter combinations on the tiles
		}else if(languageArea==LanguageAreasUK.CONFUSING){
			return new int[]{1};//tiles have letters, pattern has spoken letter
		}else{//blends and letter patterns
			return new int[]{2};//tiles have words; pattern is a sound from matching problems
			
		}
	}
	
	
	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int lA, int difficulty) {
		return new FillerType[]{FillerType.RANDOM};

/*		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if(languageArea==LanguageAreasUK.VOWELS){			
			return new FillerType[]{FillerType.PREVIOUS};
		}else
			return new FillerType[]{FillerType.NONE,FillerType.PREVIOUS};*/

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//1o words//length of the path

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//No speed

	}

	@Override
	public int[] accuracyLevels(int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];
		
		if(languageArea==LanguageAreasUK.VOWELS){//vowels
			return new int[]{1,2,3};//one or several correct
		}else{
			return new int[]{1};//only one correct
		}
	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.SPOKEN2WRITTEN};

	}
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int lA, int difficulty) {
		
		LanguageAreasUK languageArea = LanguageAreasUK.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();

		if(languageArea==LanguageAreasUK.CONFUSING){
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();
			result.add(new GameElement(false, new Word(words.get(0)), lA, difficulty));			
			
		}else{
	
			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems();

			result.add(new GameElement(false, new Word(words.get(0)), lA, difficulty));
			
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);
			Random rand = new Random();
			
			String phoneme = definitions.getProblemDescription(lA, difficulty).getDescriptions()[0].split("-")[1];
			//Word pattern= new Word(phoneme);//Phoneme
			//result.add(new GameElement(false, pattern, languageArea, difficulty));
			//pattern= new Word(definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].split("-")[0]);//Grapheme
			//result.add(new GameElement(false, pattern, languageArea, difficulty));
			
			//Find filler
			if(parameters.fillerType==FillerType.RANDOM){
				while(true){
					int i = rand.nextInt(definitions.getRowLength(lA));
				
					String newPhoneme = definitions.getProblemDescription(lA, i).getDescriptions()[0].split("-")[1];
					if ( newPhoneme!= phoneme){
						result.add(new GameElement(true, new Word( new ProblemWordListLoader(LanguageCode.EN, lA, difficulty).getItems().get(0) ), lA, i));
						break;
					}
				}
			}else if (parameters.fillerType==FillerType.CLUSTER){
				int targetCluster = definitions.getProblemDescription(lA, difficulty).getCluster();
				ArrayList<Integer> candidates = new ArrayList<Integer>();
				
				for(int ii = 0; ii< definitions.getRowLength(lA);ii++){
					
					if(ii!=difficulty)
						if(definitions.getProblemDescription(lA, ii).getCluster()==targetCluster)
							candidates.add(ii);
				}
				
				int numberFillers = parameters.accuracy;
				while(numberFillers>0){
					int i = rand.nextInt(candidates.size());
					String newPhoneme = definitions.getProblemDescription(lA, candidates.get(i)).getDescriptions()[0].split("-")[1];
					if ( newPhoneme!= phoneme){
						result.add(new GameElement(true, new Word( new ProblemWordListLoader(LanguageCode.EN, lA, candidates.get(i)).getItems().get(0) ), lA, candidates.get(i)));
						numberFillers--;
					}
					
				}
				
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
				return false;
			case SUFFIXES://Suffixes
				return false;
			case PREFIXES://Prefixes
				return false;
			case CONFUSING://Confusing letters
				return true;
			default:
				return false;
		
		}
	}


}
