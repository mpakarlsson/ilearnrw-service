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
 * Level configuration for Town Square / City Hall / Moving Pathways / Minefield
 * 
 *  vowel/consonants/similar letters plays with letter2letter or letter2word that starts with the letter 
 *  GP correspondence works with the phoneme and words
 * 
 */


public class CityHallGR implements GameLevel {

	@Override
	public List<GameElement> getWords(LevelParameters parameters,
			int lA, int difficulty) {
		
		if (parameters.mode==1){//Need only the difficulty
			
			return WordSelectionUtils.getTargetWords(LanguageCode.GR, lA, difficulty,1, 0);
			
		}else if(parameters.mode==2){//Need words for vowels, consonants or letter similarity
			
			
			System.err.println("Hey");
			List<GameElement> targetWords = WordSelectionUtils.getTargetWordsBegins(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel,true,false);
			System.err.println("Hoy "+targetWords.size()+"  "+parameters.batchSize);

			String correctLetter = targetWords.get(0).getAnnotatedWord().getWord().substring(0, 1);
			for(GameElement ge : targetWords)
				if(!ge.getAnnotatedWord().getWord().substring(0, 1).equals(correctLetter)){
					ge.setFiller(true);
					System.err.println(ge.getAnnotatedWord().getWord()+" Filler");
				}else{
					ge.setFiller(false);
					System.err.println(ge.getAnnotatedWord().getWord()+" No filler");

				}
			return targetWords;			
			/*if(languageArea == LanguageAreasGR.LETTER_SIMILARITY){

				List<GameElement> targetWords = new ArrayList<GameElement>();
				int numberFillers = 0;
				int difficultyOffset = 0;
				while(numberFillers == 0){
					
					targetWords = WordSelectionUtils.getTargetWords(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel+difficultyOffset);
					
					String correctLetter = targetWords.get(0).getAnnotatedWord().getWord().substring(0, 1);
					for(GameElement ge : targetWords)
						if(ge.getAnnotatedWord().getWord().substring(0, 1)!=correctLetter){
							ge.setFiller(true);
							numberFillers ++;
						}
					difficultyOffset+=10;
					
					if (difficultyOffset>100)
						break;
				}
				
				return targetWords;
				
			}else{
			
				List<GameElement> targetWords = WordSelectionUtils.getTargetWords(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel);
				List<List<GameElement>> distractors = WordSelectionUtils.getDistractors(LanguageCode.GR, lA, difficulty, parameters.batchSize, parameters.wordLevel, parameters.accuracy, 0, new ArrayList<String>());
				for(List<GameElement> lge : distractors)
					for(GameElement ge : lge)
						targetWords.add(ge);
				
				return targetWords;
			}*/
			
			
		}else{//Need words for GP correspondence,  with distractors without the sound
						
			List<GameElement> targetWords =WordSelectionUtils.getTargetWordsBegins(LanguageCode.GR, lA, difficulty,parameters.batchSize, parameters.wordLevel,false,true);
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
			
			/*//Find compatible phonetic difficulties
			ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.GR);

			List<Integer> selectedDifficulties = WordSelectionUtils.findCompatiblePhoneticDifficulties(LanguageCode.GR, lA, difficulty,parameters.accuracy);
			
			String[] phonemes = new String[selectedDifficulties.size()];
			
			for(int i =0;i< selectedDifficulties.size();i++){
				
				String[] desc = definitions.getProblemDescription(lA, selectedDifficulties.get(i)).getDescriptions();
				phonemes[i] = (definitions.getProblemDescription(lA, selectedDifficulties.get(i)).getDescriptions()[0].split("-")[1]);
				
			}
			List<GameElement> result = new ArrayList<GameElement>();
			
			for(int i=0;i<phonemes.length;i++){
				
				List<String> copy = new ArrayList<String>();
				
				for(int j = 0;j< phonemes.length;j++){
					
					if (j!=i)
						copy.add(phonemes[j]);
				}
				
					
				List<GameElement> aux =	WordSelectionUtils.getTargetWordsWithoutPhonemes(LanguageCode.GR, lA, selectedDifficulties.get(i), parameters.batchSize, parameters.wordLevel,i!=0, copy);
				
				for(GameElement ge : aux)
					result.add(ge);
				
			}
			

			//Word pattern= new Word(phoneme);//Phoneme
			//result.add(new GameElement(false, pattern, languageArea, difficulty));
			//pattern= new Word(definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].split("-")[0]);//Grapheme
			//result.add(new GameElement(false, pattern, languageArea, difficulty));

			return result;		*/	
			
			
			
			
		}
	

	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {
		return new int[]{0};//only one correct

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
		return new int[]{0,1,2};//Sizes of the map

	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{0};//irrelevant

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
			//TODO add also 1
			return new int[]{2};//words that start with that letter or letter to letter
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
