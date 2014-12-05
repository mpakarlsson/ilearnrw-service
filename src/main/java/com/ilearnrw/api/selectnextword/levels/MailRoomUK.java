package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.textclassification.StringMatchesInfo;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.WordSelectionUtils;


/**
 * 
 * @author hector
 *
 *	Levels configuration for mail sorter/ pelmanism
 *	Need to send batches of 3-4 words (1 target word plus 2-3 distractors)
 *	Consonant/vowels will match phoneme to word; Prefixes/suffixes the part to the word
 *
 */

public class MailRoomUK extends GameLevel {

	


	
	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {
		
		if(parameters.accuracy==2){
			parameters.amountDistractors=TypeAmount.HALF;
		}else if(parameters.accuracy==3){
			parameters.amountDistractors=TypeAmount.MANY;
		}else if((parameters.amountDistractors==TypeAmount.NONE)||(parameters.amountDistractors==TypeAmount.FEW)){
				parameters.amountDistractors=TypeAmount.MANY;
		}
		
		//int difficulties[] = new int[1+parameters.accuracy];
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		List<List<GameElement>> listsWords = new ArrayList<List<GameElement>>();
		
		
		List<GameElement> candidates = null;
		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS)){

			candidates = WordSelectionUtils.getTargetWordsWithDistractorsAndNoPhonemes(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,
					false);	
			
			
			
			HashMap<String,Integer> difficulty2lists = new HashMap<String,Integer>();

			difficulty2lists.put(languageArea+"*"+ difficulty,0);//make sure that first is the target difficulty
			listsWords.add(new ArrayList<GameElement>());
			
			
			for(GameElement word : candidates){
				
				int languageAreaWord =  ( (AnnotatedWord) word.getAnnotatedWord()).getWordProblems().get(0).getCategory();
				int difficultyWord =  ( (AnnotatedWord) word.getAnnotatedWord()).getWordProblems().get(0).getIndex();
				
				if(!difficulty2lists.containsKey(languageAreaWord+"*"+ difficultyWord)){
					difficulty2lists.put(languageAreaWord+"*"+ difficultyWord, listsWords.size());
					listsWords.add(new ArrayList<GameElement>());
				}
				listsWords.get(  difficulty2lists.get(languageAreaWord+"*"+ difficultyWord)).add(word);
			}
			

			if(listsWords.size()!=(parameters.amountDistractors.ordinal()+1)){
				System.err.println("Not the corrrect distractors");
				return new ArrayList<GameElement>();
			}
			
			
			List<GameElement> result = new ArrayList<GameElement>();
			Random rand = new Random();
			
			for(int i = 0;i<parameters.batchSize/4;i++){//4 words per round
				
				for(int j = 0; j<listsWords.size(); j++){
				
					if(i<listsWords.get(j).size())
						result.add(listsWords.get(j).get(i));
					else{
						result.add( listsWords.get(j).get( rand.nextInt(listsWords.get(j).size() ) ) );
					}
				}
			}
			
			return result;
			
			//Find compatible phonetic difficulties
			/*ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

			List<Integer> selectedDifficulties = WordSelectionUtils.findCompatiblePhoneticDifficulties(LanguageCode.EN, languageArea, difficulty,parameters.accuracy);
			
			String[] phonemes = new String[selectedDifficulties.size()];
			
			for(int i =0;i< selectedDifficulties.size();i++){
				System.err.println(selectedDifficulties.get(i));
				phonemes[i] = (definitions.getProblemDescription(languageArea, selectedDifficulties.get(i)).getDescriptions()[0].split("-")[1]);
				
			}
			
			
			for(int i=0;i<phonemes.length;i++){
				
				List<String> copy = new ArrayList<String>();
				
				for(int j = 0;j< phonemes.length;j++){
					
					if (j!=i)
						copy.add(phonemes[j]);
				}
				
					
				listsWords.add(WordSelectionUtils.getTargetWordsWithoutPhonemes(LanguageCode.EN, languageArea, selectedDifficulties.get(i), parameters.batchSize, parameters.wordLevel,i!=0, copy));

			}*/
			

		}else{
			
			candidates = WordSelectionUtils.getTargetWordsWithDistractors(
					LanguageCode.EN, 
					 languageArea, 
					 difficulty,
					 parameters,
					-1,
					false,//begins
					false);
			
			
			HashMap<String,Integer> difficulty2lists = new HashMap<String,Integer>();

			difficulty2lists.put(languageArea+"*"+difficulty,0);//make sure that first is the target difficulty
			listsWords.add(new ArrayList<GameElement>());
			
			
			for(GameElement word : candidates){
				
				int languageAreaWord =  ( (AnnotatedWord) word.getAnnotatedWord()).getWordProblems().get(0).getCategory();
				int difficultyWord =  ( (AnnotatedWord) word.getAnnotatedWord()).getWordProblems().get(0).getIndex();
				
				if(!difficulty2lists.containsKey(languageAreaWord +"*"+ difficultyWord  )){
					difficulty2lists.put(languageAreaWord +"*"+ difficultyWord  , listsWords.size());
					listsWords.add(new ArrayList<GameElement>());
				}
				listsWords.get(  difficulty2lists.get(languageAreaWord +"*"+ difficultyWord  )).add(word);
			}
			
			
			
			List<GameElement> result = new ArrayList<GameElement>();
			
			int numberAttempts = listsWords.get(0).size();
			
			while(result.size()<(parameters.batchSize*(parameters.amountDistractors.ordinal()/4.0))){//batch size is calculated for 4 distractors
				
				List<int[]> batch = new ArrayList<int[]>();
				List<String> bits = new ArrayList<String>();
				
					
				for(int j = 0; j<listsWords.size(); j++){//for each different difficulty (a list of words per difficulty)
						
						if(listsWords.get(j).size()==0)
							continue;
						
						for(int i = 0;i<listsWords.get(j).size();i++){
							
							
							GameElement candidate = listsWords.get(j).get(i);
							StringMatchesInfo match = ( (AnnotatedWord) candidate.getAnnotatedWord()).getWordProblems().get(0).getMatched().get(0);
							String newBit = candidate.getAnnotatedWord().getWord().substring(match.getStart(),match.getEnd());
							
							if(bits.contains(newBit)){//move on to next difficulty without word added
								break;
							}else{//Add provisionally
								batch.add(new int[]{j,i});
								bits.add(newBit);
							}
							
							boolean validBatch = true;
							for(int k=0;k<batch.size();k++){
								
								for(int kk=0;kk<batch.size();kk++){
									
									if(k!=kk){

										String newWord = listsWords.get(batch.get(k)[0]).get(batch.get(k)[1]).getAnnotatedWord().getWord().replace( bits.get(k) ,  bits.get(kk));
										
										if(EnglishLanguageAnalyzer.getInstance().getDictionary().getDictionary().containsKey(newWord)){
											validBatch = false;
											kk = batch.size();
											k = batch.size();
										}								
										
									}
									
								}
							}
							
							if(!validBatch){//remove the provisional addition and test next word
								batch.remove(batch.size()-1);
								bits.remove(bits.size()-1);
							}else{//move on to next difficulty with word added
								break;
							}
							
						}
						
						if(batch.size()==parameters.amountDistractors.ordinal()+1){
							
							break;//No more words need to be added							
						}
						
						
				}
					
				if(batch.size()==parameters.amountDistractors.ordinal()+1){

					System.err.println("VALID BATCH");
					for(int[] index : batch){
						result.add( listsWords.get(index[0]).get(index[1])  );
						listsWords.get(index[0]).remove(index[1]);//It can be removed because each word is from a different list
					}
					numberAttempts = listsWords.get(0).size();
				}else{
					
					if(batch.size()==0){//Not a single word added, lets go
						break;
					}
					
					if(numberAttempts==0)
						break;
					else
						numberAttempts--;
					//Move first word last and try again
					
					int[] firstWord = batch.get(0);
					listsWords.get(firstWord[0]).add(listsWords.get(firstWord[0]).get(firstWord[1]));
					listsWords.get(firstWord[0]).remove(firstWord[1]);
					
				}
					
			}
			
			return result;
						
			
		}


	}
	
	@Override
	public int[] accuracyLevels(int languageArea, int difficulty){
		//Bike shed: number of words per door
		//Music hall: number of alternatives
		return new int[]{2,3};
	}
	
	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS))
			return new int[]{0};//Take phoneme or grapheme on matched difficulty
		else if(lA==LanguageAreasUK.SUFFIXES){
			return new int[]{1};//Suffix
		}else if(lA==LanguageAreasUK.PREFIXES){
			return new int[]{2};//Prefix
		}else if(lA==LanguageAreasUK.BLENDS){
			return new int[]{3};//Prefix
		}else
			return new int[]{0};
	}



	public TypeAmount[] amountDistractors(int languageArea, int difficulty){
		
		return new TypeAmount[]{TypeAmount.HALF,TypeAmount.MANY};//2 or 3 distractors

	}
	
	

	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS))

			return new TtsType[]{TtsType.SPOKEN2WRITTEN};//TtsType.WRITTEN2WRITTEN,

		else

			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
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
			return false;
		default:
			return false;

		}
	}





}
