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
import com.ilearnrw.api.selectnextword.WordSelectionUtils;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class MonkeyHotelUK implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		
		//When mode==0, only the grapheme or phoneme in matching difficulty is used

		
		List<GameElement> result = WordSelectionUtils.getTargetWords(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel);
		
		if (result.size()==0)
			return result;
		
		
		List<GameElement> distractors;
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)){
			
			distractors = WordSelectionUtils.getClusterDistractorsDiffPhoneme(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel, parameters.accuracy);

		}else if( (lA==LanguageAreasUK.PREFIXES) || (lA==LanguageAreasUK.SUFFIXES)){
			distractors = WordSelectionUtils.getClusterDistractors(LanguageCode.EN, languageArea, difficulty, parameters.batchSize, parameters.wordLevel, parameters.accuracy);

		}else{// if(lA==LanguageAreasUK.CONFUSING){
			distractors = new ArrayList<GameElement>();
			
		}
		
		for(GameElement ge : distractors)
			result.add(ge);
		
		return result;
		
	
		
		
		
		/*if ((languageArea==1)|(languageArea==4)|(languageArea==5)){//Vowels or consonants or letter patterns/blends
			
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);
			Random rand = new Random();
			
			System.out.println("Do we need to add the phonics to that word?");
			
			
			
			Word pattern;
			
			String phoneme = definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].split("-")[1];
			String grapheme = definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].split("-")[0];
			
			if(parameters.ttsType==TtsType.SPOKEN2WRITTEN){
				pattern= new Word(phoneme);//Phoneme
			}else{
				pattern= new Word(grapheme);
				
			}
				
				
			result.add(new GameElement(false, pattern, languageArea, difficulty));
			
			if (parameters.accuracy==0){//Letters and phonemes
				
				for (int i=0;i< parameters.batchSize;i++){
					result.add(new GameElement(false, pattern, languageArea, difficulty));					
				}
				
				for (int i=0;i< parameters.batchSize;i++){
					if (parameters.fillerType==FillerType.PREVIOUS){
						
						int randomDifficulty = rand.nextInt(java.lang.Math.max(2,difficulty));
						pattern = new Word(definitions.getProblemDescription(languageArea, randomDifficulty).getDescriptions()[0].split("-")[0]);
						result.add(new GameElement(true, pattern, languageArea, randomDifficulty));					
				
					}
				}
				
			}else{//words again
				
				
				EasyHardList list = new EasyHardList(new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulty).getItems());
				
				ArrayList<String> wordLists = list.getRandom(parameters.batchSize, parameters.wordLevel);
				
				for(String word : wordLists){
					
					result.add(new GameElement(false, new EnglishWord(word), languageArea, difficulty));					

				}
				
				if(parameters.fillerType==FillerType.PREVIOUS){
					
					int randomDifficulty = rand.nextInt(java.lang.Math.max(2,difficulty));
					ArrayList<String> otherWords = new ProblemWordListLoader(LanguageCode.EN, languageArea, randomDifficulty).getItems();
					EasyHardList otherList = new EasyHardList(otherWords);
					String fillerWord = otherList.getRandom(1, parameters.wordLevel).get(0);

				
					result.add(new GameElement(true, new EnglishWord(fillerWord.split("\'")[0]),languageArea, randomDifficulty));

				}
				
				
			}
			
		}else{
			
			System.err.println("Confusing letters and irregular/sight words");
			
		}*/
		
		
	}
	
	
	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//Easy and hard

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)|(lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)|(lA==LanguageAreasUK.BLENDS)){
			return new FillerType[]{FillerType.CLUSTER};
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new FillerType[]{FillerType.NONE};
			
		}else
			return new FillerType[]{FillerType.NONE};

	}

	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{10};//1o words

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
		
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)){
			
			return new TtsType[]{TtsType.WRITTEN2WRITTEN,TtsType.SPOKEN2WRITTEN};//Only matching difficulty on the monkeys; phoneme on the pattern
			
		}else if((lA==LanguageAreasUK.PREFIXES)|(lA==LanguageAreasUK.SUFFIXES)|(lA==LanguageAreasUK.CONFUSING)){
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
		}else
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
		

	}
	
	

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];
		
		if((lA==LanguageAreasUK.VOWELS)|(lA==LanguageAreasUK.CONSONANTS)){
			
			return new int[]{0,1};//Only matching difficulty on the monkeys; phoneme on the pattern; word on monkeys
			
		}else if((lA==LanguageAreasUK.PREFIXES)){
			return new int[]{3};//prefix to word
		}else if((lA==LanguageAreasUK.SUFFIXES)){
			return new int[]{2};//suffix to word
		}else if(lA==LanguageAreasUK.CONFUSING){
			return new int[]{4};//Just written letters
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
				return true;
			case SYLLABLES://Syllables
				return false;
			case SUFFIXES://Suffixes
				return true;
			case PREFIXES://Prefixes
				return true;
			case CONFUSING://Confusing letters
				return true;
			default:
				return false;
		
		}
	}
}
