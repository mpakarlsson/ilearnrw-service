package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.StringMatchesInfo;
import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;


/**
 * 
 * @author hector
 *
 *	Levels configuration for whack a monkey / whack a mole
 *
 *
 * 
 */

public class MonkeyHotelGR implements GameLevel {

	//TODO
	
	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {

		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		
		if((lA == LanguageAreasGR.PREFIXES)||(lA == LanguageAreasGR.DERIVATIONAL)){
			
			List<GameElement> targetWords =WordSelectionUtils.getTargetWords(LanguageCode.GR, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);
			StringMatchesInfo problem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
			
			String grapheme = targetWords.get(0).getAnnotatedWord().getWord().substring(problem.getStart(), problem.getEnd());


			for(GameElement ge : targetWords)
				if(!ge.getAnnotatedWord().getWord().contains(grapheme)){
					ge.setFiller(true);
				}
			return targetWords;			
			
			
		}else if(lA == LanguageAreasGR.LETTER_SIMILARITY){
			
			if (parameters.mode==4){
				return WordSelectionUtils.getTargetWords(LanguageCode.GR, languageArea, difficulty,1, 0);

			}else{
				
				List<GameElement> targetWords = WordSelectionUtils.getTargetWords(LanguageCode.GR, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);
				
				String correctLetter = targetWords.get(0).getAnnotatedWord().getWord().substring(0, 1);
				for(GameElement ge : targetWords)
					if(ge.getAnnotatedWord().getWord().substring(0, 1)!=correctLetter){
						ge.setFiller(true);
					}
				return targetWords;			
				
				
				
			}
			
		}else{//GP
			
			List<GameElement> targetWords =WordSelectionUtils.getTargetWords(LanguageCode.GR, languageArea, difficulty,parameters.batchSize, parameters.wordLevel);
			StringMatchesInfo problem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
			
			String grapheme = targetWords.get(0).getAnnotatedWord().getWord().substring(problem.getStart(), problem.getEnd());
			String phoneme ="";
			for(int i=0;i<targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().size();i++)
				if(targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().get(i).getGrapheme()==grapheme)
					phoneme = targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().get(i).getPhoneme();
			
			for(GameElement ge : targetWords)
				if(!ge.getAnnotatedWord().getPhonetics().contains(phoneme)){
					ge.setFiller(true);
				}
			return targetWords;			
			
			
		}

	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};

	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {
		return new FillerType[]{FillerType.NONE};

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
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA == LanguageAreasGR.PREFIXES)||(lA == LanguageAreasGR.DERIVATIONAL)||(lA == LanguageAreasGR.LETTER_SIMILARITY))
			
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
		
		else if(lA == LanguageAreasGR.GP_CORRESPONDENCE)
			return new TtsType[]{TtsType.SPOKEN2WRITTEN,TtsType.WRITTEN2WRITTEN};
		else
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};

	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if((lA == LanguageAreasGR.PREFIXES)||(lA == LanguageAreasGR.DERIVATIONAL))
			
			return new int[]{5};//the pattern is a word
		
		else if(lA == LanguageAreasGR.LETTER_SIMILARITY){
			return new int[]{4,6};//two modes, letter-letter and letter-word
		}else{//GP
			return new int[]{7};//one mode, grapheme/phoneme - word

		}

	}

	@Override
	public boolean allowedDifficulty(int languageArea, int difficulty) {
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
			case SYLLABLE_DIVISION://Consonants
				return false;		
			case CONSONANTS://Consonants
				return false;
			case VOWELS://Vowels
				return false;
			case DERIVATIONAL://Blends and letter patterns
				return true;//true;
			case INFLECTIONAL://Syllables
				return false;
			case PREFIXES://Suffixes
				return true;//true;
			case GP_CORRESPONDENCE://Prefixes
				return true;
			case FUNCTION_WORDS://Confusing letters
				return false;//true;
			case LETTER_SIMILARITY:
				return true;
			default:
				return false;
		
		}
	}

}
