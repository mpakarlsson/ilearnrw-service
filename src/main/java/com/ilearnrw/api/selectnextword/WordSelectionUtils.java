package com.ilearnrw.api.selectnextword;

import ilearnrw.annotation.AnnotatedWord;
import ilearnrw.resource.ResourceLoader;
import ilearnrw.resource.ResourceLoader.Type;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.utils.LanguageCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
		
		/*for(int i=validEnd-validStart;i<amount;i++){//if there are not enough words, repeat the first ones
			
			targetWords[i] = targetWords[i-(validEnd-validStart)];
			
		}*/
						
		return targetWords;
		
	}
	

	
	/* Provides words with more than the number of syllables specified */
	static public List<GameElement> getTargetWordsWithSyllables(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,int numberSyllables){
		
		return  getTargetWords( language,  languageArea,  difficulty, amount,  wordLevel,true, false,numberSyllables,new ArrayList<String>());
		
	}
	
	/* Provides distractors that do not contain the given phonemes */
	static public List<GameElement> getTargetWordsWithoutPhonemes(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,boolean isFiller, List<String> phoneme){

		return  getTargetWords( language,  languageArea,  difficulty, amount,  wordLevel,false, isFiller,-1,phoneme);

	}
		
	/* Provides words of the specified language area and difficulty */
	static public List<GameElement> getTargetWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel){
		
		return  getTargetWords( language,  languageArea,  difficulty, amount,  wordLevel,true,false,-1,new ArrayList<String>());

	}
	
	/* Provides words of the specified language area and difficulty */
	static public List<GameElement> getTargetWordsBegins(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,boolean begins,boolean single){
		
		return  getTargetWords( language,  languageArea,  difficulty, amount,  wordLevel,true,false,-1,new ArrayList<String>(),begins,single);

	}
	
	/* Provides words of the specified language area and difficulty, and mark them as distractors or not */
	static public List<GameElement> getTargetWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,boolean isFiller){
		
		return  getTargetWords( language,  languageArea,  difficulty, amount,  wordLevel,true, isFiller,-1,new ArrayList<String>());

	}
	
	
	
	static private List<GameElement> getTargetWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,boolean findDistractors,boolean isFiller,int numberSyllables,List<String> phoneme){
		
		return getTargetWords(language, languageArea, difficulty, amount,  wordLevel, findDistractors, isFiller, numberSyllables, phoneme,false,false);
		
	}

	

	/* Provides words satisfying all given constraints */
	static private List<GameElement> getTargetWords(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,boolean findDistractors,boolean isFiller,int numberSyllables,List<String> phoneme,boolean begins,boolean single){
		List<String> words;
		if(language==LanguageCode.EN){
			words = new ProblemWordListLoader(language, languageArea, difficulty).getItems();
		}else{
			words = getListGreekWords(languageArea, difficulty,begins,single);
			
		}
		
		int tmp = words.indexOf("###");
		if(tmp>-1) words.remove(tmp);
		
		if (words.size()==0)
			return new ArrayList<GameElement>();

		int validEnd = wordLevel+amount;
		if (validEnd>=words.size())
			validEnd = words.size();

		ArrayList<GameElement> targetWords = new ArrayList<GameElement>();// new GameElement[amount];

		for(int i=validEnd-1;i>=0;i--){
			
			AnnotatedWord w;
			
			if(language == LanguageCode.EN){	
				w = new AnnotatedWord(new EnglishWord(words.get(i)),languageArea,difficulty);
			}else{
				w = new AnnotatedWord(new GreekWord(words.get(i)),languageArea,difficulty);
			}
			

			if(w.getNumberOfSyllables()<numberSyllables){
				continue;
			}
			
			
			boolean clean = true;
			for(String ph : phoneme){
				if(w.getPhonetics().contains(ph)){
					clean = false;
					break;
				}
			}
			
			if(!clean)
				continue;
			
			GameElement ge = new GameElement(isFiller,w);
			if(  ((AnnotatedWord)ge.getAnnotatedWord()).getWordProblems().size()==0)//If no difficulties found, get out!
				continue;
				
			targetWords.add(ge);


			if (targetWords.size()==amount)
				break;
		}
		
		if (findDistractors &&(targetWords.size()<amount)){
			List<GameElement> distractors = getDistractors( language,  languageArea,  difficulty, amount-targetWords.size() ,  wordLevel, 1,numberSyllables, phoneme).get(0);
			
			for(GameElement ge : distractors)
				targetWords.add(ge);
			
		}
		
		return targetWords;
		
	}
	
	/* Returns several sets of distractors, each with several different difficulties depending on availability of words */ 
static public List<List<GameElement>> getDistractors(LanguageCode language, int languageArea, int originalDifficulty,int wordsPerDifficulty, int wordLevel ,int numberDifficulties,int numberSyllables,List<String> phoneme){
		
		
		ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(language);
		
		HashMap<Integer,ArrayList<Integer>> differentClusters = new HashMap<Integer,ArrayList<Integer>>();

		int maxCluster = 0;
		for(int i = 0; i< definitions.getRowLength(languageArea);i++){
			
			if (i==originalDifficulty)
				continue;
		
			int nextCluster = definitions.getProblemDescription(languageArea, i).getCluster();
			if (nextCluster> maxCluster)
				maxCluster = nextCluster;
			
			if(!differentClusters.containsKey(nextCluster)){
				ArrayList<Integer> singleItem = new ArrayList<Integer>();
				singleItem.add(i);
				differentClusters.put(nextCluster, singleItem);
			}else{
				
				differentClusters.get(nextCluster).add(i);
			}
		}
		
		int originalCluster = definitions.getProblemDescription(languageArea, originalDifficulty).getCluster();
		
		List<List<GameElement>> distractors = new ArrayList<List<GameElement>>();
		for(int i=0;i<numberDifficulties;i++)
			distractors.add(new ArrayList<GameElement>());
		
		int currentDifficulty = 0;
		
		for(int cluster=originalCluster;cluster>=0;cluster--){
			
			if (differentClusters.containsKey(cluster)){
				
				for(int newDifficulty : differentClusters.get(cluster)){
					
					List<GameElement> aux =  getTargetWords( language,  languageArea, newDifficulty,wordsPerDifficulty-distractors.get(currentDifficulty).size(), wordLevel,false,true, numberSyllables, phoneme);
					for(GameElement ge : aux)
						distractors.get(currentDifficulty).add(ge);
					
					if (distractors.get(currentDifficulty).size()==wordsPerDifficulty)
						currentDifficulty++;
					
					if(currentDifficulty==numberDifficulties)
						return distractors;
				}
			}
		}
		
		for(int cluster=originalCluster+1;cluster<=maxCluster;cluster++){
			
			if (differentClusters.containsKey(cluster)){
				
				for(int newDifficulty : differentClusters.get(cluster)){
					
					List<GameElement> aux =  getTargetWords( language,  languageArea, newDifficulty,wordsPerDifficulty-distractors.get(currentDifficulty).size(), wordLevel,false,true, numberSyllables, phoneme);
					for(GameElement ge : aux)
						distractors.get(currentDifficulty).add(ge);
					
					if (distractors.get(currentDifficulty).size()==wordsPerDifficulty)
						currentDifficulty++;
					
					if(currentDifficulty==numberDifficulties)
						return distractors;
					
				}
			}	
			
		}
		
		return distractors;
		
	}


	
static public List<Integer> findDifferentCharacter(LanguageCode language, int languageArea, int originalDifficulty,int numberDifficulties){
	
	List<Integer> compatibleDifficulties = new ArrayList<Integer>();
	ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(language);

	List<String> characters = new ArrayList<String>();
	
	compatibleDifficulties.add(originalDifficulty);
	
	characters.add(definitions.getProblemDescription(languageArea, originalDifficulty).getCharacter());
	
	
	for(int i = 0; i< definitions.getRowLength(languageArea);i++){

		if (!characters.contains(definitions.getProblemDescription(languageArea, i).getCharacter())){
			characters.add(definitions.getProblemDescription(languageArea, i).getCharacter());
			compatibleDifficulties.add(i);
		}
		
	}
	return compatibleDifficulties;
	
	
	
}

/* Creates a list that alternates words with different patters */
static private List<String> getListGreekWords(int languageArea, int difficulty,boolean begins,boolean single){
	
	ArrayList<String> words = new ArrayList<String>();;
	
	try {
		
		String jsonFile = "";
		
		InputStreamReader in = new InputStreamReader(ResourceLoader.getInstance().getInputStream(Type.DATA,"game_words_GR/cat"+languageArea+"/words_"+languageArea+"_"+difficulty+"_GR.json"), "UTF-8");
		BufferedReader buf = new BufferedReader(in);
		String line = null;
		while((line=buf.readLine())!=null) {
			jsonFile += line;
		}
		buf.close();
		
		HashMap<String, ArrayList<String>> hm = new Gson().fromJson(jsonFile, new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType());

		HashMap<String, ArrayList<String>> validWords = new HashMap<String, ArrayList<String>>();
		
		//List<String> validKeys = new ArrayList<String>();
		
		if(begins&single){
			
			
			for(String key : hm.keySet()){
				
				if (hm.get(key).size()==0)
					continue;
				if(key.contains("BEGINS")&!key.contains(" ")){
					validWords.put(key, hm.get(key));
				}
			}
			
			//No sorting of the keys required

			
		}else if(begins){
			for(String key : hm.keySet()){
				
				if (hm.get(key).size()==0)
					continue;
				
				
				if(key.contains("BEGINS")){
					
					String shortKey = key.split(" ")[0];
						
					if(!validWords.containsKey(shortKey)){
							validWords.put(shortKey, hm.get(key));
					}else{
							ArrayList<String> reference = validWords.get(shortKey);
							for(String word : hm.get(key)){
								reference.add(word);
							}								
							
					}
													

				}
			}
			
		}else{
			
			for(String key : hm.keySet()){
				
				if (hm.get(key).size()==0)
					continue;
				
				List<String> patterns = new ArrayList<String>();
				if (key.contains("BEGIN")){
					patterns.add(key.split(" ")[0].replace("BEGINS:", ""));
					
				}
				
				if (key.contains("ENDS")){
					String nextPattern = key.split("ENDS:")[1];
					if(!patterns.contains(nextPattern))
						patterns.add(nextPattern);
				}
				
				if(key.contains("(")){
					String[] nextPatterns = key.replace("*","").split("\\(")[1].split("\\)")[0].split(" ");
					for(String nextPattern : nextPatterns)
						if(!patterns.contains(nextPattern))
							patterns.add(nextPattern);
				}
				
				if(single){
					if(patterns.size()==1){
						String shortKey = patterns.get(0);
					
						if(!validWords.containsKey(shortKey)){
							validWords.put(shortKey, hm.get(key));
						}else{
							ArrayList<String> reference = validWords.get(shortKey);
							for(String word : hm.get(key)){
								reference.add(word);
							}								
						
						}				
					
					}
				 }else{
					 for(String shortKey : patterns){
						 if(!validWords.containsKey(shortKey)){
								validWords.put(shortKey, new ArrayList<String>());
						 }
					 }
					 
					 int nextIndex = 0;
					 for(String word : hm.get(key)){//distribute the words along the different patterns

						 validWords.get(patterns.get(nextIndex)).add(word);
						 nextIndex = (nextIndex+1)%patterns.size();
					 }
					 
				 }
			}	
		}
		

		int maxSize = 0;
		HashMap<String,Integer> indexes = new HashMap<String,Integer>();
		
		for(String key:validWords.keySet()){
			if(validWords.get(key).size()==0){
				continue;	
			}
			indexes.put(key, 0);
			if(validWords.get(key).size()>maxSize)
				maxSize = validWords.get(key).size();
		}
		
		
		for(int i=0;i<maxSize;i++){
			
			for(String key : validWords.keySet()){
				if(validWords.get(key).size()==0){
					continue;	
				}
				int nextIndex = indexes.get(key);
				if(nextIndex>=validWords.get(key).size()){
					
					nextIndex = validWords.get(key).size()-(maxSize-indexes.get(key));
					if(nextIndex<0)
						nextIndex = 0;
					
				}
				

				
				words.add(validWords.get(key).get(nextIndex));
				indexes.put(key, nextIndex+1);
				
			}
			
		}

		
		} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	
	return words;
			
}

	
static public List<Integer> findCompatiblePhoneticDifficulties(LanguageCode language, int languageArea, int originalDifficulty,int numberDifficulties){
		
		List<Integer> compatibleDifficulties = new ArrayList<Integer>();
		ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(language);
		
		String[] descriptions = definitions.getProblemDescription(languageArea, originalDifficulty).getDescriptions();
		
		if(!descriptions[0].contains("-"))
			return compatibleDifficulties;//If this difficulty does not contain a phoneme, wth
				
		List<String> phonemes = new ArrayList<String>();
		compatibleDifficulties.add(originalDifficulty);
		phonemes.add(descriptions[0].split("-")[1]);
		
		HashMap<Integer,ArrayList<Integer>> differentClusters = new HashMap<Integer,ArrayList<Integer>>();

		int maxCluster = 0;
		for(int i = 0; i< definitions.getRowLength(languageArea);i++){
			
			if (i==originalDifficulty)
				continue;
		
			int nextCluster = definitions.getProblemDescription(languageArea, i).getCluster();
			if (nextCluster> maxCluster)
				maxCluster = nextCluster;
			
			if(!differentClusters.containsKey(nextCluster)){
				ArrayList<Integer> singleItem = new ArrayList<Integer>();
				singleItem.add(i);
				differentClusters.put(nextCluster, singleItem);
			}else{
				
				differentClusters.get(nextCluster).add(i);
			}
		}
		
		int originalCluster = definitions.getProblemDescription(languageArea, originalDifficulty).getCluster();
		
		
		for(int cluster=originalCluster;cluster>=0;cluster--){
			
			if (differentClusters.containsKey(cluster)){
				
				for(int newDifficulty : differentClusters.get(cluster)){
					
					String[] newDescriptions = definitions.getProblemDescription(languageArea, newDifficulty).getDescriptions();
					
					if(!newDescriptions[0].contains("-"))
						continue;
					
					String newPhoneme = newDescriptions[0].split("-")[1];
					
					boolean clean = true;
					
					for(String phoneme : phonemes){
						if (phoneme.contains(newPhoneme) || newPhoneme.contains(phoneme)){
							clean = false;
							break;
						}
					}
					
					if(clean){
						compatibleDifficulties.add(newDifficulty);
						phonemes.add(newPhoneme);
						
						if(compatibleDifficulties.size()==numberDifficulties+1)
							return compatibleDifficulties;
					}
					
				}
			}
		}
		
		for(int cluster=originalCluster+1;cluster<=maxCluster;cluster++){
			
			if (differentClusters.containsKey(cluster)){
				
				for(int newDifficulty : differentClusters.get(cluster)){
					
					String[] newDescriptions = definitions.getProblemDescription(languageArea, newDifficulty).getDescriptions();
					
					if(!newDescriptions[0].contains("-"))
						continue;
					
					String newPhoneme = newDescriptions[0].split("-")[1];
					
					boolean clean = true;
					
					for(String phoneme : phonemes){
						if (phoneme.contains(newPhoneme) || newPhoneme.contains(phoneme)){
							clean = false;
							break;
						}
					}
					
					if(clean){
						compatibleDifficulties.add(newDifficulty);
						phonemes.add(newPhoneme);
						
						if(compatibleDifficulties.size()==numberDifficulties+1)
							return compatibleDifficulties;
					}
					
				}
			}	
			
		}
		
		return compatibleDifficulties;
}
	

	/*static public List<GameElement> getTargetWordsLengthX(LanguageCode language, int languageArea, int difficulty,int amount, int wordLevel,int numberSyllables){

		
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
		
		
	}*/
	

	
	/*static public List<GameElement> getClusterDistractorsDiffPhoneme(LanguageCode language, int languageArea, int difficulty,int wordsPerDifficulty, int wordLevel,int numberDifficulties){
		
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
				
				List<GameElement> aux = getTargetWordsWithoutPhonemes( language, languageArea, newDifficulty, adjustedWordsPerDifficulty, wordLevel+5,phonemesList);//+5 to the word level to facilitate word without matches
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

	}*/
	
	
	
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
