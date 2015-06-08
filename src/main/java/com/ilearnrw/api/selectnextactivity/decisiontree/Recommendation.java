package com.ilearnrw.api.selectnextactivity.decisiontree;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.clusters.ProblemDescriptionCoordinates;
import ilearnrw.user.profile.clusters.ProfileClusters;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.selectnextactivity.NextActivities;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;
import com.ilearnrw.api.selectnextword.LevelParameters;
import com.ilearnrw.api.selectnextword.TtsType;
import com.ilearnrw.app.games.mapping.GamesInformation;

public class Recommendation {

	//Absolute
	public IgGSeverity severity;



	//Relative
	public List<IgGDifficulty> difficulty;
	public List<IgBasicComparison> accuracyRel;
	public List<IgBasicComparison> speedRel;

	public List<IgBasicComparison> challenge;

	public List<IgBasicComparison> amountDistractors;
	public List<IgBasicComparison> amountTrickyWords;
	public List<IgBasicComparison> wordDifficulty;
	public List<IgGGame> gameType;

	public List<IgBasicComparison> numberWords;



	public Recommendation(){
		difficulty = new ArrayList<IgGDifficulty>();
		gameType = new ArrayList<IgGGame>();
		accuracyRel = new ArrayList<IgBasicComparison>();
		speedRel = new ArrayList<IgBasicComparison>();
		challenge = new ArrayList<IgBasicComparison>();
		amountDistractors = new ArrayList<IgBasicComparison>();
		amountTrickyWords = new ArrayList<IgBasicComparison>();
		wordDifficulty = new ArrayList<IgBasicComparison>();

		numberWords = new ArrayList<IgBasicComparison>();
	}

	public Recommendation(
			IgGSeverity severity,
			//int gamePreferredOrder, //how much student chooses this game on her own {0 to 9}
			List<IgGDifficulty> difficulty,
			List<IgGGame> gameType,
			List<IgBasicComparison> accuracyRel,
			List<IgBasicComparison> speedRel,
			List<IgBasicComparison> challenge,
			List<IgBasicComparison> amountDistractors,
			List<IgBasicComparison> amountTrickyWords,
			List<IgBasicComparison> wordDifficulty,
			List<IgBasicComparison> numberWords){

		this.severity = severity;


		//this.gamePreferredOrder = gamePreferredOrder;
		this.gameType = gameType;
		this.difficulty = difficulty;

		this.accuracyRel = accuracyRel;
		this.speedRel = speedRel;

		this.challenge = challenge;

		this.amountDistractors = amountDistractors;
		this.amountTrickyWords = amountTrickyWords;
		this.wordDifficulty = wordDifficulty;
		this.numberWords = numberWords;

	}


	public Recommendation(String line){


		difficulty = new ArrayList<IgGDifficulty>();
		gameType = new ArrayList<IgGGame>();
		accuracyRel = new ArrayList<IgBasicComparison>();
		speedRel = new ArrayList<IgBasicComparison>();
		challenge = new ArrayList<IgBasicComparison>();
		amountDistractors = new ArrayList<IgBasicComparison>();
		amountTrickyWords = new ArrayList<IgBasicComparison>();
		wordDifficulty = new ArrayList<IgBasicComparison>();

		numberWords = new ArrayList<IgBasicComparison>();		
		int memoryLength = 1;

		for(String attr : line.split(";")){

			String name = attr.split(":")[0];
			String value = attr.replace(name+":", "");

			if(value.equals("null"))
				continue;
			try {
				//System.out.println(this.getClass().getDeclaredField(name).getType()+" -> "+value);

				if(this.getClass().getDeclaredField(name).getType().isEnum())
					this.getClass().getDeclaredField(name).set(this,  Enum.valueOf((Class<Enum>)this.getClass().getDeclaredField(name).getType(), value));
				else if(this.getClass().getDeclaredField(name).getType().isPrimitive())//assume int
					this.getClass().getDeclaredField(name).set(this,  Integer.valueOf(value));
				else{

					String[] values = value.split(":");

					ParameterizedType pt = (ParameterizedType)this.getClass().getDeclaredField(name).getGenericType();
					Type enumType = pt.getActualTypeArguments()[0];

					List<Enum> aux = new ArrayList<Enum>();

					if(value.equals("")){
						this.getClass().getDeclaredField(name).set(this,  aux);							
					}else{

						for(String v : values){
							aux.add( Enum.valueOf((Class<Enum>)enumType, v));
						}
						if(aux.size()>memoryLength)
							memoryLength = aux.size();
						this.getClass().getDeclaredField(name).set(this,  aux);
					}


				}


			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}


		}


		if(this.severity==null)
			this.severity = IgGSeverity.IGNORE;

		if(this.gameType.size()==0){
			for(int i = 0; i < memoryLength;i++) this.gameType.add(IgGGame.IGNORE);
		}

		if(this.difficulty.size()==0)
			for(int i = 0; i < memoryLength;i++) this.difficulty.add(IgGDifficulty.IGNORE);

		if(this.accuracyRel.size()==0)
			for(int i = 0; i < memoryLength;i++) this.accuracyRel.add(IgBasicComparison.IGNORE);

		if(this.speedRel.size()==0)
			for(int i = 0; i < memoryLength;i++) this.speedRel.add(IgBasicComparison.IGNORE);

		if(this.challenge.size()==0)
			for(int i = 0; i < memoryLength;i++) this.challenge.add(IgBasicComparison.IGNORE);

		if(this.amountDistractors.size()==0)
			for(int i = 0; i < memoryLength;i++) this.amountDistractors.add(IgBasicComparison.IGNORE);

		if(this.amountTrickyWords.size()==0)
			for(int i = 0; i < memoryLength;i++) this.amountTrickyWords.add(IgBasicComparison.IGNORE);

		if(this.wordDifficulty.size()==0)
			for(int i = 0; i < memoryLength;i++) this.wordDifficulty.add(IgBasicComparison.IGNORE);

		if(this.numberWords.size()==0)
			for(int i = 0; i < memoryLength;i++) this.numberWords.add(IgBasicComparison.IGNORE);

	}

	public String toString(){

		Field[] fields = this.getClass().getDeclaredFields();

		String out = "";

		for(Field field: fields){
			try {

				Type type = field.getGenericType();

				if(type instanceof ParameterizedType){
					List<Enum> aux = (List<Enum>)field.get(this);
					out+=";"+field.getName();

					if((aux==null)||(aux.size()==0))
						out+=":";
					else
						for(Enum e : aux)
							out+= ":"+e;


				}else{
					out += ";"+field.getName()+":"+field.get(this);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		return out.substring(1);//remove first comma

	}

	public enum IgGDifficulty{SAME, SAME_CLUSTER, PREV_CLUSTER, NEXT_CLUSTER,IGNORE }
	public enum IgGSeverity{MASTER, REINFORCE, NEED_WORK, NEW,IGNORE}
	public enum IgBasicComparison {LOWER,SAME,HIGHER,IGNORE}
	public enum IgGGame {SAME,DIFFERENT,IGNORE}
	//public enum GDistractor {NONE, FEW, HALF,MANY}



	public static List<NextActivities> GeneralisedLogsToRecommendation(UserProfile profile,List<LogEntry> lastLogs,List<Recommendation> recommendations,int maxRecommendations){


		List<NextActivities> output = new ArrayList<NextActivities>();
		List<NextActivities> outputBackup = new ArrayList<NextActivities>();
		Random rand = new Random();

		for(int recommendation_i = 0; recommendation_i<recommendations.size();recommendation_i++){
			Recommendation recommendation = recommendations.get(recommendation_i);
			//			for(Recommendation recommendation : recommendations){

			HashMap<Integer,List<Integer>> constrainedCandidates = new HashMap<Integer,List<Integer>>();//Find all candidate difficulties 



			for( int memory_i = 0; memory_i< recommendation.difficulty.size();memory_i++){

				HashMap<Integer,List<Integer>> candidates = new HashMap<Integer,List<Integer>>();//Find all candidate difficulties 
				//for this recommendation & memory

				if(recommendation.difficulty.get(memory_i)==IgGDifficulty.IGNORE)
					continue;

				if(recommendation.difficulty.get(memory_i)==IgGDifficulty.SAME){//from closer log to default

					if((memory_i==recommendation.difficulty.size()-1)||(lastLogs.size()==0)){//Default

						int targetCluster = profile.getUserProblems().getUserSeverities().getSystemCluster();

						ProfileClusters cls = new ProfileClusters(profile.getUserProblems().getProblems());

						for(ProblemDescriptionCoordinates index :cls.getClusterProblems(targetCluster)){

							if(!candidates.containsKey(index.getCategory())){
								List<Integer> aux = new ArrayList<Integer>();
								aux.add(index.getIndex());
								candidates.put(index.getCategory(), aux);
							}else{
								if(!candidates.get(index.getCategory()).contains(index.getIndex())){
									candidates.get(index.getCategory()).add(index.getIndex());
								}	
							}								
						}


					}else{

						int lA = lastLogs.get(memory_i).getProblemCategory();
						int dff = lastLogs.get(memory_i).getProblemIndex();

						if(!candidates.containsKey(lA)){
							List<Integer> aux = new ArrayList<Integer>();
							aux.add(dff);
							candidates.put(lA, aux);
						}else{
							if(!candidates.get(lA).contains(dff)){
								candidates.get(lA).add(dff);
							}
						}
					}

				}else{//not the same difficulty

					ProfileClusters cls = new ProfileClusters(profile.getUserProblems().getProblems());
					List<Integer> targetCluster = new ArrayList<Integer>();

					int cluster = -1;
					if((memory_i==recommendation.difficulty.size()-1)||(lastLogs.size()==0)){//Default

						cluster = profile.getUserProblems().getUserSeverities().getSystemCluster();

					}else{

						int lA = lastLogs.get(memory_i).getProblemCategory();
						int dff = lastLogs.get(memory_i).getProblemIndex();
						cluster = profile.getUserProblems().getProblemDescription(lA, dff).getCluster();

					}

					if(recommendation.difficulty.get(memory_i)==IgGDifficulty.SAME_CLUSTER){
						targetCluster.add(cluster);
					}else if(recommendation.difficulty.get(memory_i)==IgGDifficulty.PREV_CLUSTER){

						for(int i = cls.getClustersNumbers().size()-1;i>=0;i--){
							if(cls.getClustersNumbers().get(i)<cluster)
								targetCluster.add(cls.getClustersNumbers().get(i));
						}
					}else if(recommendation.difficulty.get(memory_i)==IgGDifficulty.NEXT_CLUSTER){

						for(int i : cls.getClustersNumbers()){

							if(i>cluster){
								targetCluster.add(i);		
								if (targetCluster.size()==3)
									break;
							}
						}

					}

					for(int target : targetCluster){

						for(ProblemDescriptionCoordinates index :cls.getClusterProblems(target)){

							if(!candidates.containsKey(index.getCategory())){
								List<Integer> aux = new ArrayList<Integer>();
								aux.add(index.getIndex());
								candidates.put(index.getCategory(), aux);
							}else{
								if(!candidates.get(index.getCategory()).contains(index.getIndex())){
									candidates.get(index.getCategory()).add(index.getIndex());
								}	
							}								
						}

					}					

				}



				if(constrainedCandidates.size()==0){//first candidates

					constrainedCandidates = candidates;

				}else{//intersect constrainedCandidates and candidates

					for(int lA : constrainedCandidates.keySet()){

						if(!candidates.containsKey(lA)){

							constrainedCandidates.put(lA, new ArrayList<Integer>());//delete all

						}else{

							List<Integer> removeIdx = new ArrayList<Integer>();

							for(int i = constrainedCandidates.get(lA).size()-1;i>=0;i--){
								int dff = constrainedCandidates.get(lA).get(i);
								if(!candidates.get(lA).contains(dff)){

									removeIdx.add(i);
								}

							}

							for(int i : removeIdx){

								constrainedCandidates.get(lA).remove(i);

							}
						}

					}
				}	

			}//end for memory




			//Check severity constraints
			if(recommendation.severity!=IgGSeverity.IGNORE){
				for(int lA : constrainedCandidates.keySet()){
					List<Integer> removeIdx = new ArrayList<Integer>();

					for(int i =constrainedCandidates.get(lA).size()-1;i>=0;i--){
						int dff = constrainedCandidates.get(lA).get(i);

						if(profile.getUserProblems().getUserSeverity(lA, dff)!=recommendation.severity.ordinal()){
							removeIdx.add(i);
						}

					}

					for(int i : removeIdx)
						constrainedCandidates.get(lA).remove(i);

				}

			}

			boolean constraintIgnored = false;

			if(constrainedCandidates.size()==0){//No matches, check the next recommendation
				//continue;
				if(recommendation_i<(recommendations.size()-1)){//if there are more recommendations, continue with the next
					continue;
				}else{//Otherwise get the first difficulty of the current cluster
					//continue;
					System.err.println("Ignore difficulty constraints");

					ProfileClusters cls = new ProfileClusters(profile.getUserProblems().getProblems());

					int cluster = profile.getUserProblems().getUserSeverities().getSystemCluster();

					ProblemDescriptionCoordinates index = cls.getClusterProblems(cluster).get(0);
					List<Integer> aux = new ArrayList<Integer>();
					aux.add(index.getIndex());
					constrainedCandidates.put(index.getCategory(),aux);
					constraintIgnored = true;
				}
			}


			//Choose game and level for each difficulty individually

			for(int lA : constrainedCandidates.keySet()){


				for(int dff : constrainedCandidates.get(lA)){


					ArrayList<Integer> gameCandidates = GamesInformation.getProblemRelatedApps(lA,profile.getLanguage());

					for(int i = 0;i<gameCandidates.size();i++){//Randomise order of games

						int j = rand.nextInt(gameCandidates.size());
						int aux = gameCandidates.get(i);
						gameCandidates.set(i,gameCandidates.get(j));
						gameCandidates.set(j,aux);						
					}



					HashMap<Integer,List<String>> gameLevelmappings = new HashMap<Integer,List<String>>();

					for( int memory_i = 0; memory_i< recommendation.gameType.size();memory_i++){


						if(recommendation.gameType.get(memory_i)==IgGGame.IGNORE)
							continue;

						if(recommendation.gameType.get(memory_i)==IgGGame.DIFFERENT){

							int removeGame = -1;
							if((memory_i==recommendation.gameType.size()-1)||(lastLogs.size()==0)){//default

								//TODO add comparison with preferred game for this difficulty

							}else{
								removeGame = GamesInformation.getAppIDfromStringID(lastLogs.get(memory_i).getApplicationId() );
							}

							int aux = gameCandidates.indexOf(removeGame);

							if(aux>-1)
								gameCandidates.remove(aux);

							if(gameCandidates.size()==1){//if only one game, relax the constraint, and use the same game as the second option
								if(removeGame>0)
								gameCandidates.add(removeGame);
							}

						}else{

							int onlyGame = -1;
							if((memory_i==recommendation.gameType.size()-1)||(lastLogs.size()==0)){//default

								//TODO add comparison with preferred game for this difficulty
								onlyGame = GamesInformation.getProblemRelatedApps(lA, profile.getLanguage()).get(0);
							}else{
								onlyGame = GamesInformation.getAppIDfromStringID(lastLogs.get(memory_i).getApplicationId() );
							}

							//onlyGame is the preferred (but not the only one)

							if(gameCandidates.contains(onlyGame)){//put onlyGame on first position
								int idx = gameCandidates.indexOf(onlyGame);
								gameCandidates.set(idx, gameCandidates.get(0));
								gameCandidates.set(0,onlyGame);


								//gameCandidates = new ArrayList<Integer>();
								//gameCandidates.add(onlyGame);
							}else{
								System.err.println("Ignore a SAME GAME constraint");
								constraintIgnored = true;

								//gameCandidates = new ArrayList<Integer>();//if the same is not contained, empty the list								
								//break;
							}

						}
					}

					if(gameCandidates.size()==0){
						//continue;

						if(recommendation_i<(recommendations.size()-1)){//if there are more recommendations, continue with the next
							continue;
						}else{
							System.err.println("Ignore game constraint");
							gameCandidates.add(GamesInformation.getProblemRelatedApps(lA, profile.getLanguage()).get(0));
							constraintIgnored = true;
						}
					}

					for(int x = 0; x<gameCandidates.size();x++){//find levels for each game
						int game = gameCandidates.get(x);
						/*ArrayList<Integer> candidateChallenge = new ArrayList<Integer>();
						ArrayList<Integer> candidateAccuracy = new ArrayList<Integer>();
						ArrayList<Integer> candidateSpeed = new ArrayList<Integer>();
						ArrayList<Integer> candidateDistractors = new ArrayList<Integer>();
						ArrayList<Integer> candidateTricky = new ArrayList<Integer>();
						ArrayList<Integer> candidateWordLevel = new ArrayList<Integer>();
						ArrayList<Integer> candidateNumberWords = new ArrayList<Integer>();*/

						GameLevel gameLevel = LevelFactory.createLevel(profile.getLanguage(), lA, game);
						List<String> aux = gameLevel.getAllLevels(lA, dff);

						List<LevelParameters> candidateLevels = new ArrayList<LevelParameters>();

						for(String level : aux)
							candidateLevels.add(new LevelParameters(level));


						for( int memory_i = 0; memory_i< recommendation.challenge.size();memory_i++){

							LevelParameters level_i;

							if((memory_i==recommendation.challenge.size()-1)||(lastLogs.size()==0)){

								int inverseSeverity = 3-profile.getUserProblems().getUserSeverity(lA, dff);

								String defaultLevel = generateDefaultLevel(inverseSeverity,lA,dff,gameLevel);




								level_i = new LevelParameters(defaultLevel);

							}else{
								level_i = new LevelParameters(lastLogs.get(memory_i).getLevel());
							}

							float challenge_i = gameLevel.challengeApproximation(lA, dff, level_i);


							List<Integer> removeIdx = new ArrayList<Integer>();

							for(int i = candidateLevels.size()-1;i>=0;i--){


//LevelParameters candidateLevel = candidateLevels.get(i);

								//Accuracy
								if(recommendation.accuracyRel.get(memory_i)==IgBasicComparison.HIGHER){

									if(level_i.accuracy==gameLevel.accuracyLevels(lA, dff)[gameLevel.accuracyLevels(lA, dff).length-1]){//comparing to maximum
										if(level_i.accuracy>candidateLevels.get(i).accuracy){
											removeIdx.add(i);
											continue;
										}

									}else if(candidateLevels.get(i).accuracy<gameLevel.accuracyLevels(lA, dff)[gameLevel.accuracyLevels(lA, dff).length-1])//Not max already
										if(level_i.accuracy>=candidateLevels.get(i).accuracy){
											removeIdx.add(i);
											continue;
										}

								}else if(recommendation.accuracyRel.get(memory_i)==IgBasicComparison.LOWER){

									if(level_i.accuracy==gameLevel.accuracyLevels(lA, dff)[0]){//comparing to minimum
										if(level_i.accuracy<candidateLevels.get(i).accuracy){
											removeIdx.add(i);
											continue;
										}				

									}else if(candidateLevels.get(i).accuracy>gameLevel.accuracyLevels(lA, dff)[0])//Not min already

										if(level_i.accuracy<=candidateLevels.get(i).accuracy){
											removeIdx.add(i);
											continue;
										}								

								}else if(recommendation.accuracyRel.get(memory_i)==IgBasicComparison.SAME){
									//TODO add support for games with dissimilar levels of accuracy, i.e recommendation says A3 but game has only A0
									//Same applies to others
									if(level_i.accuracy!=candidateLevels.get(i).accuracy){
										removeIdx.add(i);
										continue;
									}
								}

								//Speed				
								if(recommendation.speedRel.get(memory_i)==IgBasicComparison.HIGHER){

									if(level_i.speed.ordinal()==  gameLevel.speedLevels(lA, dff)[gameLevel.speedLevels(lA, dff).length-1].ordinal()){//Comparing to max

										if(level_i.speed.ordinal()>candidateLevels.get(i).speed.ordinal()){
											removeIdx.add(i);
											continue;
										}
									}else if(candidateLevels.get(i).speed.ordinal()<  gameLevel.speedLevels(lA, dff)[gameLevel.speedLevels(lA, dff).length-1].ordinal())//Not max already

										if(level_i.speed.ordinal()>=candidateLevels.get(i).speed.ordinal()){
											removeIdx.add(i);
											continue;
										}

								}else if(recommendation.speedRel.get(memory_i)==IgBasicComparison.LOWER){

									if(level_i.speed.ordinal()==  gameLevel.speedLevels(lA, dff)[0].ordinal()){//Comparing to min
										if(level_i.speed.ordinal()<candidateLevels.get(i).speed.ordinal()){
											removeIdx.add(i);
											continue;
										}										
									}else if(candidateLevels.get(i).speed.ordinal()>  gameLevel.speedLevels(lA, dff)[0].ordinal())//Not min already

										if(level_i.speed.ordinal()<=candidateLevels.get(i).speed.ordinal()){
											removeIdx.add(i);
											continue;
										}								

								}else if(recommendation.speedRel.get(memory_i)==IgBasicComparison.SAME){

									if(level_i.speed.ordinal()!=candidateLevels.get(i).speed.ordinal()){
										removeIdx.add(i);
										continue;
									}
								}

								//Tricky words					
								if(recommendation.amountTrickyWords.get(memory_i)==IgBasicComparison.HIGHER){

									if(level_i.amountTricky.ordinal()==  gameLevel.amountTricky(lA, dff)[gameLevel.amountTricky(lA, dff).length-1].ordinal()){//Comparing to max
										if(level_i.amountTricky.ordinal()>candidateLevels.get(i).amountTricky.ordinal()){
											removeIdx.add(i);
											continue;
										}

									}else if(candidateLevels.get(i).amountTricky.ordinal()<  gameLevel.amountTricky(lA, dff)[gameLevel.amountTricky(lA, dff).length-1].ordinal())//Not max already

										if(level_i.amountTricky.ordinal()>=candidateLevels.get(i).amountTricky.ordinal()){
											removeIdx.add(i);
											continue;
										}

								}else if(recommendation.amountTrickyWords.get(memory_i)==IgBasicComparison.LOWER){
									if(level_i.amountTricky.ordinal()==  gameLevel.amountTricky(lA, dff)[0].ordinal()){//comparing to minimum

										if(level_i.amountTricky.ordinal()<candidateLevels.get(i).amountTricky.ordinal()){
											removeIdx.add(i);
											continue;
										}		

									}else if(candidateLevels.get(i).amountTricky.ordinal()>  gameLevel.amountTricky(lA, dff)[0].ordinal())//Not min already

										if(level_i.amountTricky.ordinal()<=candidateLevels.get(i).amountTricky.ordinal()){
											removeIdx.add(i);
											continue;
										}								

								}else if(recommendation.amountTrickyWords.get(memory_i)==IgBasicComparison.SAME){

									if(level_i.amountTricky.ordinal()!=candidateLevels.get(i).amountTricky.ordinal()){
										removeIdx.add(i);
										continue;
									}
								}					


								//Amount distractors					
								if(recommendation.amountDistractors.get(memory_i)==IgBasicComparison.HIGHER){

									if(level_i.amountDistractors.ordinal()==  gameLevel.amountDistractors(lA, dff)[gameLevel.amountDistractors(lA, dff).length-1].ordinal()){//Comparing to max
										if(level_i.amountDistractors.ordinal()>candidateLevels.get(i).amountDistractors.ordinal()){
											removeIdx.add(i);
											continue;
										}

									}else if(candidateLevels.get(i).amountDistractors.ordinal()<  gameLevel.amountDistractors(lA, dff)[gameLevel.amountDistractors(lA, dff).length-1].ordinal())//Not max already


										if(level_i.amountDistractors.ordinal()>=candidateLevels.get(i).amountDistractors.ordinal()){
											removeIdx.add(i);
											continue;
										}

								}else if(recommendation.amountDistractors.get(memory_i)==IgBasicComparison.LOWER){

									if(level_i.amountDistractors.ordinal()==  gameLevel.amountDistractors(lA, dff)[0].ordinal()){//Compare to min
										if(level_i.amountDistractors.ordinal()<candidateLevels.get(i).amountDistractors.ordinal()){
											removeIdx.add(i);
											continue;
										}	

									}else if(candidateLevels.get(i).amountDistractors.ordinal()>  gameLevel.amountDistractors(lA, dff)[0].ordinal())//Not min already


										if(level_i.amountDistractors.ordinal()<=candidateLevels.get(i).amountDistractors.ordinal()){
											removeIdx.add(i);
											continue;
										}								

								}else if(recommendation.amountDistractors.get(memory_i)==IgBasicComparison.SAME){

									if(level_i.amountDistractors.ordinal()!=candidateLevels.get(i).amountDistractors.ordinal()){
										removeIdx.add(i);
										continue;
									}
								}					


								//Amount words			
								if(recommendation.numberWords.get(memory_i)==IgBasicComparison.HIGHER){

									if(level_i.batchSize==  gameLevel.batchSizes(lA, dff)[gameLevel.batchSizes(lA, dff).length-1]){//Compare to max

										if(level_i.batchSize>candidateLevels.get(i).batchSize){
											removeIdx.add(i);
											continue;
										}									
									}else if(candidateLevels.get(i).batchSize<  gameLevel.batchSizes(lA, dff)[gameLevel.batchSizes(lA, dff).length-1])//Not max already


										if(level_i.batchSize>=candidateLevels.get(i).batchSize){
											removeIdx.add(i);
											continue;
										}

								}else if(recommendation.numberWords.get(memory_i)==IgBasicComparison.LOWER){

									if(level_i.batchSize==  gameLevel.batchSizes(lA, dff)[0]){//Compare to min
										if(level_i.batchSize<=candidateLevels.get(i).batchSize){
											removeIdx.add(i);
											continue;
										}										
									}else	if(candidateLevels.get(i).batchSize>  gameLevel.batchSizes(lA, dff)[0])//Not min already


										if(level_i.batchSize<=candidateLevels.get(i).batchSize){
											removeIdx.add(i);
											continue;
										}								

								}else if(recommendation.numberWords.get(memory_i)==IgBasicComparison.SAME){

									if(level_i.batchSize!=candidateLevels.get(i).batchSize){
										removeIdx.add(i);
										continue;
									}
								}


								//Challenge

								float challenge_candidate_i = gameLevel.challengeApproximation(lA, dff,candidateLevels.get(i));

								if(recommendation.challenge.get(memory_i)==IgBasicComparison.HIGHER){

									if(challenge_i==1.0){//Compare with max
										if(challenge_i>challenge_candidate_i){
											removeIdx.add(i);
											continue;
										}										

									}else if(challenge_candidate_i<1.0)//not the max
										if(challenge_i>=challenge_candidate_i){
											removeIdx.add(i);
											continue;
										}

								}else if(recommendation.challenge.get(memory_i)==IgBasicComparison.LOWER){
									if(challenge_i==0.0){//Compare to min
										if(challenge_i<challenge_candidate_i){
											removeIdx.add(i);
											continue;
										}										
									}else if(challenge_candidate_i>0.0)//not the min
										if(challenge_i<=challenge_candidate_i){
											removeIdx.add(i);
											continue;
										}								

								}else if(recommendation.challenge.get(memory_i)==IgBasicComparison.SAME){

									if(challenge_i!=challenge_candidate_i){
										removeIdx.add(i);
										continue;
									}
								}


							}


							for(int i : removeIdx)
								candidateLevels.remove(i);

						}


						if(candidateLevels.size()==0){

							if(recommendation_i<(recommendations.size()-1)){//if there are more recommendations, continue with the next
								continue;
							}else{

								if(gameCandidates.size()==(x+1)){//last game
									boolean needsDefault = true;
									for(int key : gameLevelmappings.keySet()){
										if(gameLevelmappings.get(key).size()>0){
											needsDefault = false;
											break;
										}
											
									}
									if(needsDefault){
										System.err.println("Ignore level constraint for the last game candidate");
										constraintIgnored = true;

										int inverseSeverity = 3-profile.getUserProblems().getUserSeverity(lA, dff);

										String defaultLevel = generateDefaultLevel(inverseSeverity,lA,dff,gameLevel);


										candidateLevels.add(new LevelParameters(defaultLevel));
									}else
										continue;

								}else{
									continue;
								}
							}
						}

						//Remaining levels

						if(!gameLevelmappings.containsKey(game)){
							gameLevelmappings.put(game, new ArrayList<String>());
						}

						for(LevelParameters level : candidateLevels){
							gameLevelmappings.get(game).add(level.toString());
						}


					}//All possible games


					if(gameLevelmappings.size()==0)
						continue;
					//Create the activities with available games and levels

					List<String> gameNames = new ArrayList<String>();
					List<String> levelNames = new ArrayList<String>();


					Integer[] gameOrder = gameLevelmappings.keySet().toArray(new Integer[gameLevelmappings.keySet().size()]);

					for(int game : gameOrder){

						gameNames.add(GamesInformation.appIds[game]);
						levelNames.add( gameLevelmappings.get(game).get(0));						
					}


					if(constraintIgnored)
						outputBackup.add(new NextActivities(gameNames,lA,dff,levelNames));
					else
						output.add(new NextActivities(gameNames,lA,dff,levelNames));

				}//end for all candidate difficulties

			}//end for all candidate language areas

		}

		if(maxRecommendations<0){
			if (output.size()>0)
				return output;
			else
				return outputBackup;

		}else{

			if(output.size()>maxRecommendations){
				List<NextActivities> randomSelection = new ArrayList<NextActivities>();
				while(randomSelection.size()<maxRecommendations){
					int j = rand.nextInt(output.size());
					randomSelection.add(output.get(j));
					output.remove(j);
				}			
				return randomSelection;
			}else if(output.size()==0){
				if(outputBackup.size()>maxRecommendations){

					List<NextActivities> randomSelection = new ArrayList<NextActivities>();
					while(randomSelection.size()<maxRecommendations){
						int j = rand.nextInt(outputBackup.size());
						randomSelection.add(outputBackup.get(j));
						outputBackup.remove(j);
					}			
					return randomSelection;			

				}else if(outputBackup.size()==1){//triplicate recommendation; game will create recommendations for the same difficulty with different games
					outputBackup.add(outputBackup.get(0));
					outputBackup.add(outputBackup.get(0));
					return outputBackup;

				}else
					return outputBackup;
			}else if(output.size()==1){//triplicate recommendation; game will create recommendations for the same difficulty with different games
				output.add(output.get(0));
				output.add(output.get(0));
				return output;
			}else{


				return output;
			}
		}
	}

	private static String generateDefaultLevel(int inverseSeverity, int lA,
			int dff, GameLevel gameLevel) {


		String defaultLevel = "";


		if(inverseSeverity<gameLevel.wordLevels(lA, dff).length)
			defaultLevel +="W"+gameLevel.wordLevels(lA, dff)[inverseSeverity];
		else
			defaultLevel +="W"+gameLevel.wordLevels(lA, dff)[gameLevel.wordLevels(lA, dff).length-1];



		if(inverseSeverity<gameLevel.batchSizes(lA, dff).length)
			defaultLevel +="-B"+gameLevel.batchSizes(lA, dff)[inverseSeverity];
		else
			defaultLevel +="-B"+gameLevel.batchSizes(lA, dff)[gameLevel.batchSizes(lA, dff).length-1];

		if(inverseSeverity<gameLevel.speedLevels(lA, dff).length)
			defaultLevel +="-S"+gameLevel.speedLevels(lA, dff)[inverseSeverity].ordinal();
		else
			defaultLevel +="-S"+gameLevel.speedLevels(lA, dff)[gameLevel.speedLevels(lA, dff).length-1].ordinal();

		if(inverseSeverity<gameLevel.accuracyLevels(lA, dff).length)
			defaultLevel +="-A"+gameLevel.accuracyLevels(lA, dff)[inverseSeverity];
		else
			defaultLevel +="-A"+gameLevel.accuracyLevels(lA, dff)[gameLevel.accuracyLevels(lA, dff).length-1];

		if(inverseSeverity<gameLevel.amountDistractors(lA, dff).length)
			defaultLevel +="-D"+gameLevel.amountDistractors(lA, dff)[inverseSeverity].ordinal();
		else
			defaultLevel +="-D"+gameLevel.amountDistractors(lA, dff)[gameLevel.amountDistractors(lA, dff).length-1].ordinal();

		if(inverseSeverity<gameLevel.amountTricky(lA, dff).length)
			defaultLevel +="-X"+gameLevel.amountTricky(lA, dff)[inverseSeverity].ordinal();
		else
			defaultLevel +="-X"+gameLevel.amountTricky(lA, dff)[gameLevel.amountTricky(lA, dff).length-1].ordinal();

		if(inverseSeverity<gameLevel.fillerTypes(lA, dff).length)
			defaultLevel +="-F"+gameLevel.fillerTypes(lA, dff)[inverseSeverity].ordinal();
		else
			defaultLevel +="-F"+gameLevel.fillerTypes(lA, dff)[gameLevel.fillerTypes(lA, dff).length-1].ordinal();


		boolean done = false;
		for(TtsType ttstype : gameLevel.TTSLevels(lA, dff)){

			if(done)
				break;
			for(int mode : gameLevel.modeLevels(lA, dff)){

				inverseSeverity--;
				if(inverseSeverity<0){
					defaultLevel+= "-T"+ttstype.ordinal();
					defaultLevel+= "-M"+mode;
					done = true;
					break;
				}

			}
		}

		if(!done){
			defaultLevel+= "-T"+gameLevel.TTSLevels(lA, dff)[0].ordinal();
			defaultLevel+= "-M"+gameLevel.modeLevels(lA, dff)[0];								

		}


		return defaultLevel;
	}


}
