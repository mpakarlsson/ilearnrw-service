package com.ilearnrw.api.selectnextactivity.decisiontree;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.clusters.ProblemDescriptionCoordinates;
import ilearnrw.user.profile.clusters.ProfileClusters;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ilearnrw.api.datalogger.model.LogEntry;
import com.ilearnrw.api.selectnextactivity.NextActivities;
import com.ilearnrw.api.selectnextactivity.decisiontree.Recommendation.IgBasicComparison;
import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;
import com.ilearnrw.api.selectnextword.LevelParameters;

public class GeneralisedLog {

	//Absolute
	public GSeverity severity;
	public int accuracy;//{0-3}
	public int speed;//{0-3}
	//public int gamePreferredOrder; //how much student chooses this game on her own {0 to 9}
		
	
	//Relative
	public List<GDifficulty> difficulty;
	public List<BasicComparison> accuracyRel;
	public List<BasicComparison> speedRel;
	
	public List<BasicComparison> challenge;

	public List<BasicComparison> amountDistractors;
	public List<BasicComparison> amountTrickyWords;
	public List<BasicComparison> wordDifficulty;
	public List<BasicComparison> numberWords;
	public List<GGame> gameType;
	
	
	
	
	public GeneralisedLog(){
		difficulty = new ArrayList<GDifficulty>();
		gameType = new ArrayList<GGame>();
		accuracyRel = new ArrayList<BasicComparison>();
		speedRel = new ArrayList<BasicComparison>();
		challenge = new ArrayList<BasicComparison>();
		amountDistractors = new ArrayList<BasicComparison>();
		amountTrickyWords = new ArrayList<BasicComparison>();
		wordDifficulty = new ArrayList<BasicComparison>();
		numberWords  = new ArrayList<BasicComparison>();
	}
	
	public GeneralisedLog(
			GSeverity severity,
			int accuracy,//{0-3}
			int speed,//{0-3}
			//int gamePreferredOrder, //how much student chooses this game on her own {0 to 9}
			List<GDifficulty> difficulty,
			List<GGame> gameType,
			List<BasicComparison> accuracyRel,
			List<BasicComparison> speedRel,
			List<BasicComparison> challenge,
			List<BasicComparison> amountDistractors,
			List<BasicComparison> amountTrickyWords,
			List<BasicComparison> wordDifficulty,
			List<BasicComparison> numberWords){
	
		this.severity = severity;
		this.accuracy = accuracy;
		this.speed = speed;
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
	
	
	public GeneralisedLog(String line){
					
						
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
							this.getClass().getDeclaredField(name).set(this,  aux);
						}
						
					/*	if(name.equals("difficulty")){
							
							List<GDifficulty> aux = new ArrayList<GDifficulty>();
							
							for(String v : values){
								aux.add(GDifficulty.valueOf(v));
							}
							this.getClass().getDeclaredField(name).set(this, aux);	
							
							
						}else{
						
								BasicComparison.valueOf(values[0]);
								
								List<BasicComparison> aux = new ArrayList<BasicComparison>();
								
								for(String v : values){
									aux.add(BasicComparison.valueOf(v));
								}
								this.getClass().getDeclaredField(name).set(this, aux);
																
						}*/
													
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
	
	public enum GDifficulty{ SAME, SAME_CLUSTER, PREV_CLUSTER, NEXT_CLUSTER }
	public enum GSeverity{ MASTER, REINFORCE, NEED_WORK, NEW}
	public enum BasicComparison {LOWER,SAME,HIGHER}
	public enum GGame {SAME,DIFFERENT}
	//public enum GDistractor {NONE, FEW, HALF,MANY}
	
	
	
	
	static private GDifficulty compareDifficulty(UserProfile profile,List<LogEntry> lastLogs,int i,int j){
		
		int languageArea = lastLogs.get(i).getProblemCategory();
		int difficulty = lastLogs.get(i).getProblemIndex();
		int cluster = profile.getUserProblems().getProblems().getProblemDescription(languageArea, languageArea).getCluster();

		int prevLanguageArea = lastLogs.get(j).getProblemCategory();
		int prevDifficulty = lastLogs.get(j).getProblemIndex();
		int prevCluster = profile.getUserProblems().getProblems().getProblemDescription(prevLanguageArea, prevDifficulty).getCluster();

		
		if ((prevLanguageArea==languageArea)&&(prevDifficulty==difficulty))
			return GDifficulty.SAME;
		else if(prevCluster==cluster){
			return GDifficulty.SAME_CLUSTER;
		}else if(prevCluster<cluster){
			return GDifficulty.NEXT_CLUSTER;
		}else{
			return GDifficulty.PREV_CLUSTER;
		}
	}
	
	static private BasicComparison basicComparison(int value1,int value2){
		if(value1>value2)
			return BasicComparison.HIGHER;
		else if(value1==value2)
			return BasicComparison.SAME;
		else
			return BasicComparison.LOWER;
	}
	
	static private GGame compareGame(List<LogEntry> lastLogs,int i,int j){
		
		if(lastLogs.get(i).getTag().equals(lastLogs.get(j))){
			return GGame.SAME;
		}else
			return GGame.DIFFERENT;
		
	}
	
	
	static private BasicComparison compareGameChallenge(UserProfile profile, List<LogEntry> lastLogs,int i,int j){
		
		int languageArea = lastLogs.get(i).getProblemCategory();
		int difficulty = lastLogs.get(i).getProblemIndex();
		LevelParameters level = new LevelParameters( lastLogs.get(i).getLevel());
		GameLevel game = LevelFactory.createLevel(profile.getLanguage(), languageArea, lastLogs.get(i).getApplicationId());	
		float challenge = game.challengeApproximation(languageArea, difficulty, level);

		int prevLanguageArea = lastLogs.get(j).getProblemCategory();
		int prevDifficulty = lastLogs.get(j).getProblemIndex();
		LevelParameters prevLevel = new LevelParameters( lastLogs.get(j).getLevel());
		GameLevel prevGame = LevelFactory.createLevel(profile.getLanguage(), prevLanguageArea, lastLogs.get(j).getApplicationId());	
		float prevChallenge = prevGame.challengeApproximation(prevLanguageArea, prevDifficulty, prevLevel);
		

		if(challenge>prevChallenge)
			return BasicComparison.HIGHER;
		else if(challenge==prevChallenge)
			return BasicComparison.LOWER;
		else
			return BasicComparison.LOWER;
		
	}
	
	
	static private int processAccuracy(String data){
		//TODO
		return 0;
	}
	
	static private int processSpeed(String data){
		//TODO
		return 0;
	}
	
	public static List<GeneralisedLog> LogsToGeneralisedLogs(UserProfile profile,List<LogEntry> lastLogs){
		
		List<GeneralisedLog> constraints = new ArrayList<GeneralisedLog>();
		
		
		ProfileClusters cls = new ProfileClusters(profile.getUserProblems().getProblems());
		int cluster_default = profile.getUserProblems().getUserSeverities().getSystemCluster();
		ArrayList<ProblemDescriptionCoordinates> aux = cls.getClusterProblems(cluster_default);
		
		
		int lA_default = 0;
		int dff_default = 0;
		
		GSeverity defaultSeverity = GSeverity.MASTER;
		
		for(ProblemDescriptionCoordinates coord : aux){
			if(profile.getUserProblems().getUserSeverity(coord.getCategory(),coord.getIndex())<defaultSeverity.ordinal()){
				lA_default  = coord.getCategory();
				dff_default = coord.getIndex();
				
				defaultSeverity = GSeverity.values()[profile.getUserProblems().getUserSeverity(lA_default,dff_default)];
				
			}
		}
		
		for(int i = 0; i < lastLogs.size(); i++){
			
			
			GeneralisedLog newLog = new GeneralisedLog();
			int languageArea = 0;
			int difficulty = 0;
			if(lastLogs.size()==0){//default
				
				newLog.severity = defaultSeverity;
				
				newLog.accuracy = 3-newLog.severity.ordinal();
				newLog.speed = 3-newLog.severity.ordinal();				
				
				
			}else{
				languageArea = lastLogs.get(i).getProblemCategory();
				difficulty = lastLogs.get(i).getProblemIndex();
			
				newLog.severity = GSeverity.values()[profile.getUserProblems().getUserSeverity(languageArea, difficulty)];
				newLog.accuracy = processAccuracy(lastLogs.get(0).getValue());
				newLog.speed = processSpeed(lastLogs.get(0).getValue());
			}
			
			
			LevelParameters level = new LevelParameters( lastLogs.get(i).getLevel());

			for(int j=i+1;j<lastLogs.size();j++){
				
				newLog.gameType.add(compareGame(lastLogs,i,j));
				
				newLog.difficulty.add( compareDifficulty(profile,lastLogs, i, j)  );
				
				newLog.accuracyRel.add( basicComparison( newLog.accuracy,processAccuracy(lastLogs.get(j).getValue()) ) );
				newLog.speedRel.add( basicComparison( newLog.speed,processSpeed(lastLogs.get(j).getValue()) ) );
			
				newLog.challenge.add( compareGameChallenge( profile,lastLogs,i,j  ) );

				LevelParameters prevLevel = new LevelParameters( lastLogs.get(j).getLevel());
				
				newLog.amountDistractors.add(basicComparison(level.amountDistractors.ordinal(),prevLevel.amountDistractors.ordinal()));
				newLog.amountTrickyWords.add(basicComparison(level.amountTricky.ordinal(),prevLevel.amountTricky.ordinal()));
				newLog.wordDifficulty.add(basicComparison(level.wordLevel,prevLevel.wordLevel));
				
				newLog.numberWords.add(basicComparison(level.batchSize,prevLevel.batchSize));
			
			}
			//Compare to default
			
			
			if(lastLogs.size()==0){
				
				//TODO
				
			
			}else{
			
				newLog.gameType.add(GGame.DIFFERENT);//TODO add comparison with preferred game for this difficulty
//			newLog.gameType.add(compareGame(lastLogs,i,j));
			

				int cluster_i = profile.getUserProblems().getProblems().getProblemDescription(languageArea, languageArea).getCluster();

				GDifficulty compared2default;
				
				if ((lA_default==languageArea)&&(dff_default==difficulty))
					compared2default = GDifficulty.SAME;
				else if(cluster_i==cluster_default){
					compared2default = GDifficulty.SAME_CLUSTER;
				}else if(cluster_i<cluster_default){
					compared2default = GDifficulty.NEXT_CLUSTER;
				}else{
					compared2default = GDifficulty.PREV_CLUSTER;
				}
				
				newLog.difficulty.add( compared2default  );
			
				newLog.accuracyRel.add( basicComparison( newLog.accuracy, 3-newLog.severity.ordinal() ) );//TODO accuracy as float?
				newLog.speedRel.add( basicComparison( newLog.speed,3-newLog.severity.ordinal()  ) );//TODO speed as float 
		
				GameLevel game = LevelFactory.createLevel(profile.getLanguage(), languageArea, lastLogs.get(i).getApplicationId());
			
				newLog.challenge.add(basicComparison( (int)(game.challengeApproximation(languageArea, difficulty, level)*3) , 3-newLog.severity.ordinal()));
			
				newLog.amountDistractors.add(basicComparison(level.amountDistractors.ordinal(), 3-newLog.severity.ordinal()  ));//Default: More severity, more distractors
				newLog.amountTrickyWords.add(basicComparison(level.amountTricky.ordinal(),3-newLog.severity.ordinal()));//Default: More severity, more tricky words
				newLog.wordDifficulty.add(basicComparison(level.wordLevel,  (int)(((10.0*(3-newLog.severity.ordinal()))/GSeverity.values().length))));//Default: more severity, more word difficulty: normalised from 0 to 10
			
				newLog.numberWords.add(basicComparison(level.batchSize,3-newLog.severity.ordinal()));

			}
			constraints.add(newLog);
			
	
		}
		
		return constraints;
		
		
	}
	
	
}
