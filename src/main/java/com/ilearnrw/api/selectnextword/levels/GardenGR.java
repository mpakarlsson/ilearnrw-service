package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
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

public class GardenGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {

		
		LanguageAreasGR languageArea = LanguageAreasGR.values()[lA];

		List<GameElement> result = new ArrayList<GameElement>();

			
			ArrayList<String> words = new ProblemWordListLoader(LanguageCode.GR, lA, difficulty).getItems();
			EasyHardList list = new EasyHardList(words);
			
			List<String> wordList  = list.getRandom(parameters.batchSize*3, parameters.wordLevel);

		
			/*if(parameters.wordLevel==0)
				wordList= list.getEasy();
			else
				wordList= list.getHard();
			*/
			ArrayList<GreekWord> reserve = new ArrayList<GreekWord>();
			
			//Add words with the desired number of syllables
			for (int i=0;i<wordList.size();i++){
				GreekWord aux = new GreekWord(wordList.get(i));
				
			//	if (aux.getNumberOfSyllables()>1){
					result.add(new GameElement(false, aux, lA, difficulty));
				//}
				
				if(result.size()==parameters.batchSize)
					break;
				
			}
			
			
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

					for(String w : otherWords){
					
						GreekWord ew =  new GreekWord(w);
						result.add(new GameElement(true, ew,lA, randomDifficulty));
					}
				
			}
			
			
			
			
			
			
			if(result.size()==0){
				
				result.add(new GameElement(false, new GreekWord("@@@@@@"), lA, difficulty));
			}
			
	
		return result;		

		
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0,1};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		if(lA==LanguageAreasGR.FUNCTION_WORDS){
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
		return new int[]{0};//No choice

	}

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		return new TtsType[]{TtsType.WRITTEN2WRITTEN};
	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		if(lA==LanguageAreasGR.FUNCTION_WORDS){
			return new int[]{4};
		}else{
			
			return new int[]{3};//number of syllables
		}
		
	}

	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		if(languageArea>=LanguageAreasGR.values().length)
			return false;
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
			case SYLLABLE_DIVISION://Consonants
				return true;		
			case CONSONANTS://Consonants
				return false;
			case VOWELS://Vowels
				return false;
			case DERIVATIONAL://Blends and letter patterns
				return false;//true;
			case INFLECTIONAL://Syllables
				return false;//true;
			case PREFIXES://Suffixes
				return false;//true;
			case GP_CORRESPONDENCE://Prefixes
				return false;
			case FUNCTION_WORDS://Confusing letters
				return true;
			case LETTER_SIMILARITY:
				return false;
			default:
				return false;
		
		}
	}
}