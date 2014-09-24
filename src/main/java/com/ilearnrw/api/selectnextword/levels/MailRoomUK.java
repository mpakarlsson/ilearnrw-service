package com.ilearnrw.api.selectnextword.levels;


import ilearnrw.languagetools.english.EnglishDictionary;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.GraphemePhonemePair;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.WordVsProblems;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.ilearnrw.api.selectnextactivity.TestSelectNextWordsController;
import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;


/**
 * 
 * @author Hector P. Martinez
 *
 *	Levels are independent of language area
 *	Need to send batches of 2-4 words (1 target word plus 1-3 fillers)
 *	Words must contain the sound corresponding to the difficulty in the matching difficulty information
 *
 */

public class MailRoomUK implements GameLevel {

	private static Logger LOG = Logger
			.getLogger(MailRoomUK.class);

	@Override
	public int[] modeLevels(int languageArea, int difficulty) {
		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS))
			return new int[]{0};//Take phoneme or grapheme on matched difficulty
		else if(lA==LanguageAreasUK.SUFFIXES){
			return new int[]{1};//Suffix
		}else if(lA==LanguageAreasUK.PREFIXES){
			return new int[]{2};//Prefix
		}else
			return new int[]{0};
	}

	@Override
	public int[] wordLevels(int languageArea, int difficulty) {

		return new int[]{0,1};//Easy and hard
	}

	@Override
	public FillerType[] fillerTypes(int languageArea, int difficulty) {

		return new FillerType[]{FillerType.CLUSTER};

	}


	@Override
	public int[] batchSizes(int languageArea, int difficulty) {
		return new int[]{5};//5 rounds of packages
	}

	@Override
	public int[] speedLevels(int languageArea, int difficulty) {

		return new int[]{0,1,2};//Slow, medium and fast
	}

	@Override
	public int[] accuracyLevels(int languageArea, int difficulty) {
		return new int[]{2,3};//Number of fillers
	}


	@Override
	public TtsType[] TTSLevels(int languageArea, int difficulty) {

		LanguageAreasUK lA = LanguageAreasUK.values()[languageArea];

		if((lA==LanguageAreasUK.CONSONANTS)| (lA==LanguageAreasUK.VOWELS))

			return new TtsType[]{TtsType.WRITTEN2WRITTEN,TtsType.SPOKEN2WRITTEN};

		else

			return new TtsType[]{TtsType.WRITTEN2WRITTEN};
	}


	@Override
	public List<GameElement> getWords(LevelParameters parameters, int languageArea, int difficulty) {

		int difficulties[] = new int[1+parameters.accuracy];


		ProblemDefinitionIndex definitions = new ProblemDefinitionIndex(LanguageCode.EN);

		if (parameters.fillerType==FillerType.CLUSTER){


			int targetCluster = definitions.getProblemDescription(languageArea, difficulty).getCluster();
			ArrayList<Integer> candidates = new ArrayList<Integer>();

			for(int ii = 0; ii< definitions.getRowLength(languageArea);ii++){

				if(ii!=difficulty)
					if(definitions.getProblemDescription(languageArea,ii).getCluster()==targetCluster)
						candidates.add(ii);
			}

			if(candidates.size()<parameters.accuracy){

				for(int ii = 0; ii< difficulty;ii++)//add previous
					candidates.add(ii);


			}

			if(candidates.size()<parameters.accuracy){//add next

				for(int ii = difficulty+1; ii< definitions.getRowLength(languageArea);ii++)
					candidates.add(ii);


			}

			Random rand = new Random();

			difficulties[0] = difficulty;
			for (int i=1;i<difficulties.length;i++){

				int ind = rand.nextInt(candidates.size());
				difficulties[i] = candidates.get(ind);
				candidates.remove(ind);

			}

		}


		ArrayList<ArrayList<String>> wordLists = new ArrayList<ArrayList<String>>();

		for(int  i = 0;i<1+parameters.accuracy;i++){

			wordLists.add(new ArrayList<String>());

			int attempts = 3;
			while(attempts>0){

				attempts--;	

				WordVsProblems wp = new WordVsProblems(EnglishLanguageAnalyzer.getInstance());

				ProblemWordListLoader pwll = new ProblemWordListLoader(LanguageCode.EN, languageArea, difficulties[i]);
				EasyHardList list = new EasyHardList(pwll.getItems());
				ArrayList<String> aux = list.getRandom(parameters.batchSize, parameters.wordLevel);

				if (aux.size()==0){
					//No words
					
					for(int j=wordLists.get(i).size();j<parameters.batchSize;j++){

						wordLists.get(i).add("@@@@@");
						
					}
					attempts = 0;
					break;
					
				}
				
				for(String word : aux){

					boolean clean = true;

					if(parameters.mode==0){
						for(int j : difficulties){

							if(j!=difficulties[i]){//assume that j==difficulties[i] will always have the difficulty

								wp.insertWord(new EnglishWord(word), languageArea, j);
								if(wp.getMatchedProbs().size()!=0){
									clean = false;
									break;
								}

							}

						}

						if (clean){//need to check that the phonemes do not exist by chance
							EnglishDictionary dic = EnglishLanguageAnalyzer.getInstance().getDictionary();

							for(int j : difficulties){

								if(j!=difficulties[i]){//assume that j==difficulties[i] will always have the difficulty

									EnglishWord w = dic.getWord(word);

									for(GraphemePhonemePair gp : w.getGraphemesPhonemes()){
										String phoneme = definitions.getProblemDescription(languageArea, j).getDescriptions()[0].split("-")[1];
										if (gp.getPhoneme()==phoneme){
											clean = false;
											break;
										}

									}


								}
							}
						}

					}else if(parameters.mode==1){


						for(int j : difficulties){

							if(j!=difficulties[i]){//assume that j==difficulties[i] will always have the difficulty

								String suffix = definitions.getProblemDescription(languageArea, j).getDescriptions()[0].split("-")[0];
								LOG.debug(String.format("-%s vs %s",suffix,word));

								if(word.endsWith(suffix)){
									clean = false;
									break;
								}

							}

						}

					}else if (parameters.mode==2){
						for(int j : difficulties){

							if(j!=difficulties[i]){//assume that j==difficulties[i] will always have the difficulty

								String prefix = definitions.getProblemDescription(languageArea, j).getDescriptions()[0];

								if(word.startsWith(prefix)){
									clean = false;
									break;
								}

							}

						}

					}

					if(clean){
						wordLists.get(i).add(word);
						if(wordLists.get(i).size()==parameters.batchSize){
							attempts=0;
							break;
						}
					}
				}

			}
			
			if(wordLists.get(i).size()==0){
				for(int j=wordLists.get(i).size();j<parameters.batchSize;j++)
					wordLists.get(i).add("####");

			}

			for(int j=wordLists.get(i).size();j<parameters.batchSize;j++){

				wordLists.get(i).add(wordLists.get(i).get(0));
					
			}				
			
		}
		

		List<GameElement> result = new ArrayList<GameElement>();

		for(int j=0;j< parameters.batchSize;j++){

			for (int i = 0; i< difficulties.length;i++){

				if(i==0)
					result.add(new GameElement(false, new EnglishWord(wordLists.get(i).get(j)), languageArea, difficulties[i]));
				else
					result.add(new GameElement(true, new EnglishWord(wordLists.get(i).get(j)), languageArea, difficulties[i]));

				LOG.debug(String.format("Create Word!!%s ",wordLists.get(i).get(j)));

			}

		}

		LOG.debug(String.format("Done!"));

		return result;
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
			return false;
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
