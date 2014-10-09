package com.ilearnrw.api.selectnextword;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.WordProblemInfo;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public  class WordSelectionUtils {

	static public Word[] getListWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel){
		
		ArrayList<String> words = new ProblemWordListLoader(language, languageArea, difficulty).getItems();
		
		int tmp = words.indexOf("###");
		if(tmp>-1) words.remove(tmp);
		
		if (words.size()==0)
			return new Word[0];
		
		int validEnd = wordLevel+amount;
		if (validEnd>=words.size())
			validEnd = words.size();
		
		int validStart = validEnd-amount;
		if(validStart<0)
			validStart = 0;
		
		Word[] targetWords = new Word[amount];
		for(int i=validStart;i<validEnd;i++){
			
			if(language == LanguageCode.EN)
				targetWords[i-validStart] = new EnglishWord(words.get(i));
			else
				targetWords[i-validStart] = new GreekWord(words.get(i));
				
			
		}
		
		for(int i=validEnd-validStart;i<amount;i++){//if there are not enough words, repeat the first ones
			
			targetWords[i] = targetWords[i-(validEnd-validStart)];
			
		}
						
		return targetWords;
		
	}
	
	static public List<GameElement> getTargetWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel){
		
		return  getTargetWords( language,  languageArea,  difficulty, amount,  wordLevel,false);

	}
	
	static public List<GameElement> getTargetWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,boolean isFiller){
	
		ArrayList<String> words = new ProblemWordListLoader(language, languageArea, difficulty).getItems();
		
		int tmp = words.indexOf("###");
		if(tmp>-1) words.remove(tmp);
		
		if (words.size()==0)
			return new ArrayList<GameElement>();

		int validEnd = wordLevel+amount;
		if (validEnd>=words.size())
			validEnd = words.size();
		
		int validStart = validEnd-amount;
		if(validStart<0)
			validStart = 0;
		
		ArrayList<GameElement> targetWords = new ArrayList<GameElement>();// new GameElement[amount];
		
		for(int i=validStart;i<validEnd;i++){
			
			if(language == LanguageCode.EN)
				targetWords.add(new GameElement(isFiller,new EnglishWord(words.get(i)),languageArea,difficulty));
			else
				targetWords.add(new GameElement(isFiller,new GreekWord(words.get(i)),languageArea,difficulty));

		}
		
		for(int i=validEnd-validStart;i<amount;i++){//if there are not enough words, repeat the first ones
			
			targetWords.add(targetWords.get(i-(validEnd-validStart)));
			
		}
						
		return targetWords;
		
		
	}
	
	static public List<GameElement> getTargetWordsGreaterLengthX(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,int numberSyllables){

		
		ArrayList<String> words = new ProblemWordListLoader(language, languageArea, difficulty).getItems();
		
		int tmp = words.indexOf("###");
		if(tmp>-1) words.remove(tmp);
		
		if (words.size()==0)
			return new ArrayList<GameElement>();

		int validEnd = wordLevel+amount;
		if (validEnd>=words.size())
			validEnd = words.size();
		
		ArrayList<GameElement> targetWords = new ArrayList<GameElement>();// new GameElement[amount];
		
		for(int i=validEnd-1;i>0;i--){
			
			Word w;
			if(language == LanguageCode.EN)
				w = new EnglishWord(words.get(i));
			else
				w = new GreekWord(words.get(i));

			if(w.getNumberOfSyllables()>=numberSyllables){
				targetWords.add(new GameElement(false,w,languageArea,difficulty));
				if (targetWords.size()==amount)
					break;
			}

		}
		
		int foundWords = targetWords.size();
		
		if (foundWords==0)
			return targetWords;
		
		for(int i=foundWords;i<amount;i++){//if there are not enough words, repeat the first ones
			
			targetWords.add(targetWords.get(i-foundWords));
			
		}
						
		return targetWords;
		
		
	}
	
	static public List<GameElement> getTargetWordsLengthX(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,int numberSyllables){

		
		ArrayList<String> words = new ProblemWordListLoader(language, languageArea, difficulty).getItems();
		
		int tmp = words.indexOf("###");
		if(tmp>-1) words.remove(tmp);
		
		if (words.size()==0)
			return new ArrayList<GameElement>();

		int validEnd = wordLevel+amount;
		if (validEnd>=words.size())
			validEnd = words.size();
		
		ArrayList<GameElement> targetWords = new ArrayList<GameElement>();// new GameElement[amount];
		
		for(int i=validEnd-1;i>0;i--){
			
			Word w;
			if(language == LanguageCode.EN)
				w = new EnglishWord(words.get(i));
			else
				w = new GreekWord(words.get(i));

			if(w.getNumberOfSyllables()==numberSyllables){
				targetWords.add(new GameElement(false,w,languageArea,difficulty));
				if (targetWords.size()==amount)
					break;
			}

		}
		
		int foundWords = targetWords.size();
		
		if (foundWords==0)
			return targetWords;
		
		for(int i=foundWords;i<amount;i++){//if there are not enough words, repeat the first ones
			
			targetWords.add(targetWords.get(i-foundWords));
			
		}
						
		return targetWords;
		
		
	}
	
	static public List<GameElement> getTargetWordsDiffPhoneme(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,List<String> phoneme, boolean isFiller){

		
		ArrayList<String> words = new ProblemWordListLoader(language, languageArea, difficulty).getItems();
		
		int tmp = words.indexOf("###");
		if(tmp>-1) words.remove(tmp);

		if (words.size()==0)
			return new ArrayList<GameElement>();

		int validEnd = wordLevel+amount;
		if (validEnd>=words.size())
			validEnd = words.size();
		
		ArrayList<GameElement> targetWords = new ArrayList<GameElement>();// new GameElement[amount];
		
		for(int i=validEnd-1;i>=0;i--){

			Word w;
			if(language == LanguageCode.EN)
				w = new EnglishWord(words.get(i));
			else
				w = new GreekWord(words.get(i));
			
			boolean clean = true;
			
			for(String ph : phoneme){
				if(w.getPhonetics().contains(ph)){
					clean = false;
					break;
				}
			}

			if(clean){

				targetWords.add(new GameElement(isFiller,w,languageArea,difficulty));
				if (targetWords.size()==amount)
					break;
			}
		}

		
		int foundWords = targetWords.size();
		
		if (foundWords==0)
			return targetWords;
		
		for(int i=foundWords;i<amount;i++){//if there are not enough words, repeat the first ones
			
			targetWords.add(targetWords.get(i-foundWords));
			
		}
						
		return targetWords;
		
		
	}
	
	static public List<GameElement> getClusterDistractorsDiffPhoneme(LanguageCode language, int languageArea, int difficulty,int wordsPerDifficulty, int wordLevel,int numberDifficulties){
		
		ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(language);
		
		if(!definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].contains("-"))
			return new ArrayList<GameElement>();
		
		
		String phoneme = definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].split("-")[1];

		int targetCluster = definitions.getProblemDescription(languageArea, difficulty).getCluster();
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		
		for(int ii = 0; ii< definitions.getRowLength(languageArea);ii++){
			
			if(ii!=difficulty)
				if(definitions.getProblemDescription(languageArea, ii).getCluster()==targetCluster)
					candidates.add(ii);
		}
		
		
		if (candidates.size()<numberDifficulties){//When there are no more difficulties within the cluster, use the previous difficulties
			
			for(int ii = 0; ii< difficulty;ii++){
				candidates.add(ii);
			}	
		}
		
		if (candidates.size()<numberDifficulties){//As last resort, use the next difficulties
			
			for(int ii = difficulty+1; ii< definitions.getRowLength(languageArea);ii++){
				candidates.add(ii);
			}	
		}
		
		
//		int numberFillers = parameters.accuracy;
		
		Random rand = new Random();
		List<GameElement> result = new ArrayList<GameElement>();
		int numberFillers = numberDifficulties;

		int adjustedWordsPerDifficulty = wordsPerDifficulty;
		
		//if (candidates.size()<numberDifficulties){
			
		//	adjustedWordsPerDifficulty = (int) Math.ceil(wordsPerDifficulty*numberDifficulties/candidates.size());
		//}
		
		List<String> phonemesList = new ArrayList<String>();
		
		
		phonemesList.add(phoneme);
		
		
		while((numberFillers>0)&&(candidates.size()>0)){
			
			System.err.println(candidates.size());

			int index = rand.nextInt(candidates.size());
			int newDifficulty = candidates.get(index);
						
			if(!definitions.getProblemDescription(languageArea, difficulty).getDescriptions()[0].contains("-")){
				
				candidates.remove(index);
				continue;
			}
			
			String newPhoneme = definitions.getProblemDescription(languageArea, newDifficulty).getDescriptions()[0].split("-")[1];
			System.err.println(phoneme+" vs "+newPhoneme);
			
			boolean clean = true;
			for(String ph : phonemesList){
				if(ph.contains(newPhoneme) ||newPhoneme.contains(ph) ){
					clean = false;
				}
			}
			
			if (clean){
				
				List<GameElement> aux = getTargetWordsDiffPhoneme( language, languageArea, newDifficulty, adjustedWordsPerDifficulty, wordLevel+5,phonemesList,true);//+5 to the word level to facilitate word without matches
				if (aux.size()>0){
					numberFillers--;
					phonemesList.add(newPhoneme);
					for(GameElement ge : aux)
						result.add(ge);
					
				}
				
			}
			
			candidates.remove(index);
			
		}
		
		return result;

	}
	
	static public List<GameElement> getClusterDistractors(LanguageCode language, int languageArea, int difficulty,int wordsPerDifficulty, int wordLevel,int numberDifficulties){

		
		
		ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(language);
		
		int targetCluster = definitions.getProblemDescription(languageArea, difficulty).getCluster();
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		
		for(int ii = 0; ii< definitions.getRowLength(languageArea);ii++){
			
			if(ii!=difficulty)
				if(definitions.getProblemDescription(languageArea, ii).getCluster()==targetCluster){
				
					if(getTargetWords( language, languageArea, ii, 1, wordLevel,true).size()>0)
						candidates.add(ii);
					
				}
		}
		
		
		if (candidates.size()<numberDifficulties){//When there are no more difficulties within the cluster, use the previous difficulties
			
			for(int ii = 0; ii< difficulty;ii++){
				if(getTargetWords( language, languageArea, ii, 1, wordLevel,true).size()>0)

					candidates.add(ii);
			}	
		}
		
		if (candidates.size()<numberDifficulties){//As last resort, use the next difficulties
			
			for(int ii = difficulty+1; ii< definitions.getRowLength(languageArea);ii++){
				if(getTargetWords( language, languageArea, ii, 1, wordLevel,true).size()>0)

					candidates.add(ii);
			}	
		}
		
				
		Random rand = new Random();
		List<GameElement> result = new ArrayList<GameElement>();
		int numberFillers = numberDifficulties;

		int adjustedWordsPerDifficulty = wordsPerDifficulty;
		
	//	if (candidates.size()<numberDifficulties){
			
		//	adjustedWordsPerDifficulty = (int) Math.ceil(wordsPerDifficulty*numberDifficulties/candidates.size());
	//	}
		
		while((numberFillers>0)&&(candidates.size()>0)){
			

			int index = rand.nextInt(candidates.size());
			int newDifficulty = candidates.get(index);
						

			List<GameElement> aux = getTargetWords( language, languageArea, newDifficulty, adjustedWordsPerDifficulty, wordLevel,true);

			if (aux.size()>0)
				numberFillers--;
				
			for(GameElement ge : aux)
				result.add(ge);
			candidates.remove(index);

		}
					
		return result;	
	}
}
