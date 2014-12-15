package com.ilearnrw.api.selectnextword.levels;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.StringMatchesInfo;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.TypeFiller;
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

public class MonkeyHotelGR extends GameLevel {

	
	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int languageArea, int difficulty) {

		
		LanguageAreasGR lA = LanguageAreasGR.values()[languageArea];

		
		if((lA == LanguageAreasGR.PREFIXES)||(lA == LanguageAreasGR.DERIVATIONAL)){
						
			int originalSize = parameters.batchSize;
			parameters.batchSize = (10*parameters.batchSize)+1;
			
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.GR, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,
					false);	
				
			
			StringMatchesInfo problem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
			
			String grapheme = targetWords.get(0).getAnnotatedWord().getWord().substring(problem.getStart(), problem.getEnd());

			int correct = 0;
			int filler = 0;

			List<GameElement> result = new ArrayList<GameElement>();
			
			for(GameElement ge : targetWords)
				if(!ge.getAnnotatedWord().getWord().contains(grapheme)){
					if(filler<=(originalSize*parameters.amountDistractors.ordinal()*0.25)){
						ge.setFiller(true);
						filler++;
						result.add(ge);
					}
				}else{
					if(correct<=(originalSize*(4-parameters.amountDistractors.ordinal())*0.25)){
						
						result.add(ge);
						correct++;

					}
					
				}
			return result;			
			
			
		}else if(lA == LanguageAreasGR.LETTER_SIMILARITY){
			
			if (parameters.mode==4){
								
				parameters.batchSize = 1;
				
				return WordSelectionUtils.getTargetWordsWithDistractors(
						LanguageCode.GR, 
						 languageArea, 
						 difficulty,
						 parameters,
						-1,
						false,
						false);				
				

			}else{
				
				int originalSize = parameters.batchSize;
				parameters.batchSize = (10*parameters.batchSize)+1;
				
				List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
						LanguageCode.GR, 
						 languageArea, 
						 difficulty,
						 parameters,
						-1,
						true,
						false);
				
				
				
				
				int correct = 0;
				int filler = 0;

				List<GameElement> result = new ArrayList<GameElement>();
								
				String correctLetter = targetWords.get(0).getAnnotatedWord().getWord().substring(0, 1);
				
				for(GameElement ge : targetWords)
					if(!ge.getAnnotatedWord().getWord().substring(0, 1).equals(correctLetter)){
						
						
						if(filler<=(originalSize*parameters.amountDistractors.ordinal()*0.25)){
							ge.setFiller(true);
							filler++;
							result.add(ge);
						}
					}else{
						if(correct<=(originalSize*(4-parameters.amountDistractors.ordinal())*0.25)){
							
							result.add(ge);
							correct++;

						}						
					}
				return result;			
				
			}
			
		}else{//GP
			
			int originalSize = parameters.batchSize;
			parameters.batchSize = (10*parameters.batchSize)+1;
			
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.GR, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					true,
					false);
			
			
			StringMatchesInfo problem = ((AnnotatedWord)targetWords.get(0).getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
			
			String grapheme = targetWords.get(0).getAnnotatedWord().getWord().substring(problem.getStart(), problem.getEnd());
			String phoneme ="";
			for(int i=0;i<targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().size();i++)
				if(targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().get(i).getGrapheme().equals(grapheme))
					phoneme = targetWords.get(0).getAnnotatedWord().getGraphemesPhonemes().get(i).getPhoneme();
						
			int correct = 0;
			int filler = 0;

			List<GameElement> result = new ArrayList<GameElement>();
			
			for(GameElement ge : targetWords)
				if(!ge.getAnnotatedWord().getPhonetics().contains(phoneme)){
					if(filler<=(originalSize*parameters.amountDistractors.ordinal()*0.25)){
						ge.setFiller(true);
						filler++;
						result.add(ge);
					}
				}else{
					if(correct<=(originalSize*(4-parameters.amountDistractors.ordinal())*0.25)){
						
						result.add(ge);
						correct++;

					}						
				}
			return result;			
			
			
		}

	}

	
	
	@Override
	public TypeFiller[] fillerTypes(int languageArea, int difficulty){

			return new TypeFiller[]{TypeFiller.NONE};//Distractors within difficulty

	}
	

	@Override
	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		

			return new TypeAmount[]{TypeAmount.FEW,TypeAmount.HALF,TypeAmount.MANY};//need some distractors
			
		
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
	
	/* Instructions for the games */
	public String instructions(int languageArea, int difficulty,LevelParameters param){
		
		if(param.mode==4){
			return "Χτύπησε το γράμμα που βλέπεις";
		}else if(param.mode==5){
			if(languageArea==5)
				return "Χτύπησε τις λέξεις με το ίδιο πρόθεμα, όπως το παράδειγμα";
			else if(languageArea==6)
				return "Χτύπησε τις λέξεις με την ίδια κατάληξη, όπως το παράδειγμα";
		}else if(param.mode==6){
			return "Χτύπησε τις λέξεις που αρχίζουν με το γράμμα που βλέπεις";
		}else if(param.mode==7){
			return "Χτύπησε τις λέξεις που αρχίζουν με τον ήχο (που ακούς?)";
		}
		
		
		return "Not available";
		
		
	}
}
