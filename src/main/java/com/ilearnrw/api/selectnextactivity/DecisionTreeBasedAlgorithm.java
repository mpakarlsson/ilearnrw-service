package com.ilearnrw.api.selectnextactivity;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.selectnextactivity.decisiontree.Recommendation;
import com.ilearnrw.api.selectnextactivity.decisiontree.Tree;
import com.ilearnrw.api.selectnextactivity.decisiontree.GeneralisedLog.GSeverity;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.api.selectnextword.TypeAmount;
import com.ilearnrw.api.selectnextword.TypeBasic;
import com.ilearnrw.api.selectnextword.TypeFiller;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;

public class DecisionTreeBasedAlgorithm {

	
	Random rand = new Random();
	
	
	public List<NextActivities> getNextProblems(UserProfile profile,List<LogEntry> lastLogs) {
	
		
		Tree tree = new Tree("trees/sample.tree");
		
    	List<Recommendation> recommendations = tree.getRecommendations(profile,lastLogs);

		return Recommendation.GeneralisedLogsToRecommendation(profile, lastLogs, recommendations,5);
		
		
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
	
	
	String getALevel(UserProfile profile, int languageArea, int difficulty,String appName){
		
		 GameLevel levelInfo = LevelFactory.createLevel(profile.getLanguage(), languageArea, appName);
		
		 int inverseSeverity = 3-profile.getUserProblems().getUserSeverities().getSeverity(languageArea, difficulty);
		 
		 
		 LevelParameters level = new LevelParameters("");
		 
		 level.wordLevel = (int)(10.0*inverseSeverity/GSeverity.values().length);
		 level.fillerType = levelInfo.fillerTypes(languageArea, difficulty)[0];
		 if(levelInfo.batchSizes(languageArea, difficulty).length>inverseSeverity)
			 level.batchSize = levelInfo.batchSizes(languageArea, difficulty)[inverseSeverity];
		 else
			 level.batchSize = levelInfo.batchSizes(languageArea, difficulty)[levelInfo.batchSizes(languageArea, difficulty).length-1];

		 if(levelInfo.speedLevels(languageArea, difficulty).length>inverseSeverity)
			 level.speed = levelInfo.speedLevels(languageArea, difficulty)[inverseSeverity];
		 else
			 level.speed = levelInfo.speedLevels(languageArea, difficulty)[levelInfo.speedLevels(languageArea, difficulty).length-1];
		 
		 level.mode = levelInfo.modeLevels(languageArea, difficulty)[0];

		 if(levelInfo.accuracyLevels(languageArea, difficulty).length>inverseSeverity)
			 level.accuracy = levelInfo.accuracyLevels(languageArea, difficulty)[inverseSeverity];
		 else
			 level.accuracy = levelInfo.accuracyLevels(languageArea, difficulty)[levelInfo.accuracyLevels(languageArea, difficulty).length-1];
		 
		 if(levelInfo.amountDistractors(languageArea, difficulty).length>inverseSeverity)
			 level.amountDistractors = levelInfo.amountDistractors(languageArea, difficulty)[inverseSeverity];
		 else
			 level.amountDistractors = levelInfo.amountDistractors(languageArea, difficulty)[levelInfo.amountDistractors(languageArea, difficulty).length-1];
		 
		 if(levelInfo.amountTricky(languageArea, difficulty).length>inverseSeverity)
			 level.amountTricky = levelInfo.amountTricky(languageArea, difficulty)[inverseSeverity];
		 else
			 level.amountTricky = levelInfo.amountTricky(languageArea, difficulty)[levelInfo.amountTricky(languageArea, difficulty).length-1];
		 		 
		 level.ttsType = levelInfo.TTSLevels(languageArea, difficulty)[0];

		 return level.toString();
		
	}


	public List<NextActivities> getNextProblems(UserProfile profile,List<LogEntry> lastLogs, int languageArea, int difficulty,String game) {

		Tree tree = new Tree("trees/sample.tree");
		
    	List<Recommendation> recommendations = tree.getRecommendations(profile,lastLogs);

    	List<NextActivities> basicRecommendation = Recommendation.GeneralisedLogsToRecommendation(profile, lastLogs, recommendations,-1);
		
		List<NextActivities> nextProbs = new ArrayList<NextActivities>();

		
		for(NextActivities activity : basicRecommendation){
			if((activity.getCategory()==languageArea)&&(activity.getIndex()==difficulty)){
				if(game!=null){
					for(int i= 0;i<activity.getActivity().size();i++)
						if(activity.getActivity().get(i).equals(game)){
							List<String> selectedGame = new ArrayList<String>(Arrays.asList(game));
							List<String> selectedLevel = new ArrayList<String>(Arrays.asList(activity.getLevel().get(i)));
							
							return new ArrayList<NextActivities>(  Arrays.asList(new NextActivities(selectedGame,  languageArea, difficulty, selectedLevel)));
						}
				}else{
					return new ArrayList<NextActivities>(  Arrays.asList(activity));
				}
			}
			
		}
		//Did not find a matching recommendation
		
		NextActivities next = new NextActivities(languageArea, difficulty);
		List<String> games = availableGames( profile.getLanguage(),languageArea,difficulty);
		if(game!=null)
			games = new ArrayList<String>(  Arrays.asList(game));
		
		
		next.setActivity(games);
		
		List<String> levels = new ArrayList<String>();
		
		for(String g : games){
				levels.add(getALevel(profile, languageArea, difficulty,g));
			
		}
		
		next.setLevel(levels);
		nextProbs.add(next);
		
		return nextProbs;
	}
	

	public List<NextActivities> getNextProblems(UserProfile profile,List<LogEntry> lastLogs, String character,String game) {

		System.err.println(character);
		Tree tree = new Tree("trees/sample.tree");
		
    	List<Recommendation> recommendations = tree.getRecommendations(profile,lastLogs);

    	List<NextActivities> basicRecommendation = Recommendation.GeneralisedLogsToRecommendation(profile, lastLogs, recommendations,-1);
		
		List<NextActivities> nextProbs = new ArrayList<NextActivities>();

		
		for(NextActivities activity : basicRecommendation){
			String activityCharacter = profile.getUserProblems().getProblemDescription(activity.getCategory(), activity.getIndex()).getCharacter();
			
			if(activityCharacter.equals(character)){
				if(game!=null){
					for(int i= 0;i<activity.getActivity().size();i++)
						if(activity.getActivity().get(i).equals(game)){
							List<String> selectedGame = new ArrayList<String>(Arrays.asList(game));
							List<String> selectedLevel = new ArrayList<String>(Arrays.asList(activity.getLevel().get(i)));
							
							return new ArrayList<NextActivities>(  Arrays.asList(new NextActivities(selectedGame,  activity.getCategory(), activity.getIndex(), selectedLevel)));
						}
				}else{
					return new ArrayList<NextActivities>(  Arrays.asList(activity));
				}
			}
			
		}
		//Did not find a matching recommendation
		List<int[]> characterDifficultiesBeforeCluster = new ArrayList<int[]>();
		List<int[]> characterDifficultiesAfterCluster = new ArrayList<int[]>();
		boolean done = false;
		int systemCluster = profile.getUserProblems().getSystemCluster();
		
		for(int lA=0;lA<profile.getUserProblems().getNumerOfRows();lA++){
			for(int diff=0;diff<profile.getUserProblems().getRowLength(lA);diff++){
				if(profile.getUserProblems().getProblemDescription(lA,diff).getCharacter().equals(character)){
					if(profile.getUserProblems().getProblemDescription(lA,diff).getCluster()==systemCluster){
						characterDifficultiesBeforeCluster.clear();
						characterDifficultiesBeforeCluster.add(new int[]{lA,diff});
						done = true;
						break;
					}else if(profile.getUserProblems().getProblemDescription(lA,diff).getCluster()<systemCluster){
						characterDifficultiesBeforeCluster.add(new int[]{lA,diff});

					}else{
						characterDifficultiesAfterCluster.add(new int[]{lA,diff});

					}	
				}	
			}
			if(done)
				break;
		}
		
		
		if(characterDifficultiesBeforeCluster.size()==0)
			characterDifficultiesBeforeCluster = characterDifficultiesAfterCluster;
		
		NextActivities next = new NextActivities(characterDifficultiesBeforeCluster.get(0)[0], characterDifficultiesBeforeCluster.get(0)[1]);
		List<String> games = availableGames( profile.getLanguage(),characterDifficultiesBeforeCluster.get(0)[0], characterDifficultiesBeforeCluster.get(0)[1]);
		if(game!=null)
			games = new ArrayList<String>(  Arrays.asList(game));
		
		next.setActivity(games);
		
		List<String> levels = new ArrayList<String>();
		
		for(String g : games){
				levels.add(getALevel(profile, characterDifficultiesBeforeCluster.get(0)[0], characterDifficultiesBeforeCluster.get(0)[1],g));
			
		}
		
		next.setLevel(levels);
		nextProbs.add(next);
		
		return nextProbs;
	}
	
	
}
