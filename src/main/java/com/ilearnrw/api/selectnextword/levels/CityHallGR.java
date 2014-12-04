package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.StringMatchesInfo;
import ilearnrw.textclassification.WordProblemInfo;
import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.TypeFiller;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;



/**
 * 
 * @author hector
 *
 * Level configuration for Town Square / City Hall / Moving Pathways / Minefield
 * 
 *  vowel/consonants/similar letters plays with letter2letter or letter2word that starts with the letter 
 *  GP correspondence works with the phoneme and words
 * 
 */


public class CityHallGR extends GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {
		
		if (parameters.mode==1){//Need only the difficulty, but as they are mixed, get many words and use the match
			
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.GR, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
				//	new ArrayList<String>(),
					false,//begins
					false);
			
					
			WordProblemInfo correctProblem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0);//Flag as correct only 
			String correctLetter = targetWords.get(0).getAnnotatedWord().getWord().substring(correctProblem.getMatched().get(0).getStart(), correctProblem.getMatched().get(0).getEnd());

			for(GameElement ge : targetWords)
				if(!ge.getAnnotatedWord().getWord().contains(correctLetter)){
					ge.setFiller(true);
				}else{
					ge.setFiller(false);
				}
			
			return targetWords;
			
		}else if(parameters.mode==2){//Need words for consonants or letter similarity
			
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.GR, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					true,//begins
					false);
			
			if(targetWords.size()!=0){
				
				String correctLetter = targetWords.get(0).getAnnotatedWord().getWord().substring(0, 1);//Flag as correct only 
				
				for(GameElement ge : targetWords)
					if(!ge.getAnnotatedWord().getWord().substring(0, 1).equals(correctLetter)){
						ge.setFiller(true);
					}else{
						ge.setFiller(false);
					}
								
			}	

			return targetWords;			

			
		}else if(parameters.mode==3){//Need words for GP correspondence,  with distractors without the sound
				
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.GR, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					true,//begins
					false);
			
			if(targetWords.size()!=0){
				
				
				StringMatchesInfo problem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
				String grapheme = targetWords.get(0).getAnnotatedWord().getWord().substring(problem.getStart(), problem.getEnd());
				String phoneme ="";
				for(int i=0;i<targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().size();i++)
					if(targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().get(i).getGrapheme()==grapheme)
						phoneme = targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().get(i).getPhoneme();
				
				for(GameElement ge : targetWords)
					if(!ge.getAnnotatedWord().getPhonetics().contains(phoneme)){
						ge.setFiller(true);
					}else
						ge.setFiller(false);


				
			}
			
			return targetWords;			

		}else{//Vowels, contain letter
			
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.GR, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,//begins
					true);//only one difficulty
			
			if(targetWords.size()!=0){
				
				StringMatchesInfo problem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
				String grapheme = targetWords.get(0).getAnnotatedWord().getWord().substring(problem.getStart(), problem.getEnd());
				
				for(GameElement ge : targetWords)
					if(!ge.getAnnotatedWord().getWord().contains(grapheme)){
						ge.setFiller(true);
					}else{
						ge.setFiller(false);
					}
			}	

			return targetWords;	
						
		}
	
	}
	
	
	@Override
	public TypeFiller[] fillerTypes(int languageArea, int difficulty){
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		if(lA==LanguageAreasGR.VOWELS)
			return new TypeFiller[]{TypeFiller.CLUSTER};//Distractors from other difficulties
		else
			return new TypeFiller[]{TypeFiller.NONE};//Distractors within difficulty

	}
	
	

	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		if(lA==LanguageAreasGR.VOWELS)
			return new TypeAmount[]{TypeAmount.FEW,TypeAmount.HALF,TypeAmount.MANY};//number of alternative difficulties
		else{
			return new TypeAmount[]{TypeAmount.FEW};//No control over number of distractors
		}		
		
	}
	
	

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		if(lA==LanguageAreasGR.LETTER_SIMILARITY){
			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
		}else{
			return new TtsType[]{TtsType.SPOKEN2WRITTEN};
		}
	}

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];
		
		switch(lA){
		
		case LETTER_SIMILARITY://Consonants
			return new int[]{1,2};//confusing	with letters only or letter to word
		case CONSONANTS://Consonants
			return new int[]{1,2};//words that start with that letter or letter to letter
		case VOWELS://Vowels
			return new int[]{1,4};//words that contain that letter or letter to letter
		default://GP_CORRESPONDENCE
			return new int[]{3};//confusing
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
