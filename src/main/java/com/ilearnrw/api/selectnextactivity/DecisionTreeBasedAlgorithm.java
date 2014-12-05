package com.ilearnrw.api.selectnextactivity;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.selectnextactivity.decisiontree.Recommendation;
import com.ilearnrw.api.selectnextactivity.decisiontree.Tree;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class DecisionTreeBasedAlgorithm {

	
	Random rand = new Random();
	
	
	public List<NextActivities> getNextProblems(UserProfile profile,List<LogEntry> lastLogs) {
	
		
		Tree tree = new Tree("trees/sample.tree");
		
    	List<Recommendation> recommendations = tree.getRecommendations(profile,lastLogs);

		return Recommendation.GeneralisedLogsToRecommendation(profile, lastLogs, recommendations);
		
		/*ClusterBasedAlgorithm tmp = new ClusterBasedAlgorithm();
		
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
		return nextProbs;

		*
		*
		*/
		
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
		
		List<String> activityLevels = level.getAllLevels(languageArea, difficulty);

		
		if(index<activityLevels.size())
			return activityLevels.get(index);
		else
			return activityLevels.get(0);	
		
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
