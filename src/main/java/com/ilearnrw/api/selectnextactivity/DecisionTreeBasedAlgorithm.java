package com.ilearnrw.api.selectnextactivity;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.selectnextword.FillerType;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class DecisionTreeBasedAlgorithm implements NextActivitiesProvider {

	
	Random rand = new Random();
	@Override
	public List<NextActivities> getNextProblems(UserProfile profile) {
		// TODO Auto-generated method stub
		
		ClusterBasedAlgorithm tmp = new ClusterBasedAlgorithm();
		
		List<NextActivities> nextProbs = tmp.getNextProblems(profile);
		nextProbs.add(tmp.getNextProblems(profile).get(0));
		nextProbs.add(tmp.getNextProblems(profile).get(0));
		
		for(int i = 0;i<nextProbs.size();i++){
			
			List<String> games = availableGames( profile.getLanguage(),nextProbs.get(i).getCategory(),nextProbs.get(i).getIndex());
			nextProbs.get(i).setActivity(games);
			
			List<String> levels = new ArrayList<String>();
			
			for(String game : games){
				levels.add(getALevel(profile, profile.getLanguage(), nextProbs.get(i).getCategory(),nextProbs.get(i).getIndex(),game, 0));
			}
			nextProbs.get(i).setLevel(levels);
			
		}
		
//		List<NextActivities> nextProbs = new ArrayList<NextActivities>();



		//One random
		/*int i = rand.nextInt(profile.getUserProblems().getNumerOfRows());
		int j = rand.nextInt(profile.getUserProblems().getSystemIndex(i)+1);
		
		NextActivities next = new NextActivities(i, j);
		List<String> games = availableGames( profile.getLanguage(),i,j);
		
		next.setActivity(games);
		
		List<String> levels = new ArrayList<String>();
		
		for(String game : games){
			levels.add(getALevel(profile.getLanguage(), i, j,game, 0));
			
		}
		
		next.setLevel(levels);
		nextProbs.add(next);

		
		//One random
		i = rand.nextInt(profile.getUserProblems().getNumerOfRows());
		j = rand.nextInt(profile.getUserProblems().getSystemIndex(i)+1);
		next = new NextActivities(i, j);
		games = availableGames( profile.getLanguage(),i,j);
		next.setActivity(games);
		
		for(String game : games){
			levels.add(getALevel(profile.getLanguage(), i, j,game, 0));
			
		}
		
		next.setLevel(levels);
		nextProbs.add(next);
		
		//One random
		i = rand.nextInt(profile.getUserProblems().getNumerOfRows());
		j = rand.nextInt(profile.getUserProblems().getSystemIndex(i)+1);
		next = new NextActivities(i, j);
		games = availableGames( profile.getLanguage(),i,j);
		next.setActivity(games);
		
		for(String game : games){
			levels.add(getALevel(profile.getLanguage(), i, j,game, 0));
			
		}
		
		next.setLevel(levels);
		nextProbs.add(next);*/
		
		
		return nextProbs;
	}
	
	
	
	String[] appIDs = new String[]{ "MAIL_SORTER", "WHAK_A_MOLE",
		"ENDLESS_RUNNER", "HARVEST", "SERENADE_HERO", "MOVING_PATHWAYS",
		"EYE_EXAM", "TRAIN_DISPATCHER", "DROP_CHOPS"};
	
	String[] appNames = new String[]{ "Mail Sorter", "Whack a Mole",
			"Endless Runner", "Harvest", "Serenade Hero", "Moving Pathways",
			"Eye Exam", "Train Dispatcher", "Drop Chops" };
	
	List<String> availableGames(LanguageCode lc, int languageArea, int difficulty){
		
		List<String> result = new ArrayList<String>();
		
		for(int i = 0; i<appNames.length; i++){
			String appName = appNames[i];
			
			GameLevel gl = LevelFactory.createLevel(lc, languageArea, appName);
			
			if(gl.allowedDifficulty(languageArea, difficulty))
				result.add(appIDs[i]);
			
		}
		
		if (result.size()==0){
			
			System.err.println(languageArea+" "+difficulty);
			return result;
		}

		for(int k = 0; k<result.size();k++){//randomize order
			int index = rand.nextInt(result.size());
			String aux = result.get(index);
			result.remove(index);
			result.add(aux);
			
		}

		return result;
		
	}
	
	
	String getALevel(UserProfile profile, LanguageCode lc, int languageArea, int difficulty,String appName, int index){
		
		GameLevel level = LevelFactory.createLevel(lc, languageArea, appName);
		
		 ArrayList<String> words = new ProblemWordListLoader(lc, languageArea, difficulty).getItems();
		 int numberWords = words.size()-1;
		 if(numberWords<1)
			 numberWords = 1;
		 
		 int proficiency = 3-profile.getUserProblems().getUserSeverity(languageArea, difficulty);
		 int randomOffset = new Random().nextInt( (proficiency+1)*3 );
		 
		 int wordLevels = randomOffset+ (int)java.lang.Math.floor( (proficiency/3.0)*(numberWords));

		String activityLevel = "";
		int i = 0;
		if(level.allowedDifficulty(languageArea, difficulty)){
			for(int mode : level.modeLevels(languageArea, difficulty)){

				for(int accuracy : level.accuracyLevels(languageArea, difficulty)){

					for(int speed : level.speedLevels(languageArea, difficulty)){

						for(int batchSize : level.batchSizes(languageArea, difficulty)){

							//for(int wordLevels : level.wordLevels(languageArea, difficulty)){

								for (FillerType fillerTypes : level.fillerTypes(languageArea, difficulty)){

									for(TtsType ttstype : level.TTSLevels(languageArea, difficulty)){


										 
										 if(wordLevels<0)
											 wordLevels = 0;
										 activityLevel = 	"M"+mode+"-"
												+ 	"A"+accuracy+"-"
												+ 	"S"+speed+"-"
												+ 	"B"+batchSize+"-"
												+ 	"W"+wordLevels+"-"
												+ 	"F"+fillerTypes.ordinal()+"-"
												+	"T"+ttstype.ordinal();


										if (i==index){

											return activityLevel;
										}else{
											i++;
										}


									}



								}
							//}
						}


					}
				}

			}	
		
	}
		
		return activityLevel;
	}


	public List<NextActivities> getNextProblems(UserProfile user, int languageArea, int difficulty) {

		List<NextActivities> nextProbs = new ArrayList<NextActivities>();

		NextActivities next = new NextActivities(languageArea, difficulty);
		List<String> games = availableGames( user.getLanguage(),languageArea,difficulty);
		
		next.setActivity(games);
		
		List<String> levels = new ArrayList<String>();
		
		for(String game : games){
			levels.add(getALevel(user,user.getLanguage(), languageArea, difficulty,game, 0));
			
		}
		
		next.setLevel(levels);
		nextProbs.add(next);
		
		return nextProbs;
		
	}
	
	
	public List<NextActivities> getNextProblems(UserProfile user, int languageArea, int difficulty,String game) {

		List<NextActivities> nextProbs = new ArrayList<NextActivities>();

		NextActivities next = new NextActivities(languageArea, difficulty);
		List<String> games = new ArrayList<String>();
		games.add(game);
		
		next.setActivity(games);
		
		List<String> levels = new ArrayList<String>();
		
		levels.add(getALevel(user,user.getLanguage(), languageArea, difficulty,game, 0));
			
		
		next.setLevel(levels);
		nextProbs.add(next);
		
		return nextProbs;
		
	}
	
}
