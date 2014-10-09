package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.greek.GreekWord;
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

public class CityHallGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();

		ArrayList<String> words = new ProblemWordListLoader(LanguageCode.GR, lA, difficulty).getItems();
		result.add(new GameElement(false, new GreekWord(words.get(0)), lA, difficulty));			
		
		
		if((languageArea==LanguageAreasGR.VOWELS)||(languageArea==LanguageAreasGR.CONSONANTS)){
			
			if(parameters.fillerType==FillerType.CLUSTER){
				
				ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.GR);

				int targetCluster = definitions.getProblemDescription(lA, difficulty).getCluster();
				ArrayList<Integer> candidates = new ArrayList<Integer>();
				
				for(int ii = 0; ii< definitions.getRowLength(lA);ii++){
					
					if(ii!=difficulty)
						if(definitions.getProblemDescription(lA,ii).getCluster()==targetCluster)
							candidates.add(ii);
				}
				
				if(candidates.size()==0){
					
					for(int ii = 0; ii< difficulty;ii++)
								candidates.add(ii);
				}
				if(candidates.size()==0){
					
					for(int ii = difficulty+1; ii< definitions.getRowLength(lA);ii++)
								candidates.add(ii);
				}
				Random rand = new Random();

					
				int randomDifficulty = candidates.get(rand.nextInt(candidates.size()));
					
				ArrayList<String> otherWords = (new EasyHardList(new ProblemWordListLoader(LanguageCode.GR, lA, randomDifficulty).getItems())).getRandom(result.size(), parameters.wordLevel);

					
				GreekWord ew =  new GreekWord(otherWords.get(0));
				result.add(new GameElement(true, ew,lA, randomDifficulty));
				
			}
		}
		
		
		return result;

	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0,1};//only one correct

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.CLUSTER};

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//only one correct

	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {
		return new int[]{0};//only one correct

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{1};//only one correct

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};

	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
		case LETTER_SIMILARITY://Consonants
			return new int[]{1};//confusing	
		case CONSONANTS://Consonants
			return new int[]{2};//words that start with that letter
		case VOWELS://Vowels
			return new int[]{2};//words that start with that letter
		default:
			return new int[]{2};//confusing
		}
		
		
	}

	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
			case SYLLABLE_DIVISION://Consonants
				return false;		
			case CONSONANTS://Consonants
				return true;
			case VOWELS://Vowels
				return true;
			case DERIVATIONAL://Blends and letter patterns
				return false;//true;
			case INFLECTIONAL://Syllables
				return false;//true;
			case PREFIXES://Suffixes
				return false;//true;
			case GP_CORRESPONDENCE://Prefixes
				return false;
			case FUNCTION_WORDS://Confusing letters
				return false;//true;
			case LETTER_SIMILARITY:
				return true;
			default:
				return false;
		
		}
	}

}
