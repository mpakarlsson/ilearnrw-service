package com.ilearnrw.api.selectnextactivity;

import ilearnrw.user.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkingIndexBasedAlgorithm implements NextActivitiesProvider {

	// calculate average severity, avg(i), for each profile category (row)
	// calculate the balance, bal(i) of each working index. 
	//   bal(i) = (sum of severities on next cells of row i)/(sum of severities on previous cells of row i)
	// big avg(i) means that the child need to word more on the row
	// small bal(i) means that the next problems of the row are at the same difficulty as these that the student has left behind
	// big bal(i) means that the child has to improve many things compared to what is behind
	// the idea is to select the working index of the row with big avg(i) and also big bal(i)
	// so we calculate the weight for each working index weight(i) = avg(i)*50+bal(i)*20
	//the bigger the weight(i) is the bigger the possibility to select the i-th problem is
	@Override
	public List<NextActivities> getNextProblems(UserProfile profile) {
		List<NextActivities> nextProbs = new ArrayList<NextActivities>();
		int severities[][] = profile.getUserProblems().getUserSeverities().getSeverities();
		double avg[] = new double[severities.length];
		double bal[] = new double[severities.length];
		int weights[] = new int[severities.length];
		for (int i=0; i<avg.length; i++){
			avg[i] = 0;
			bal[i] = 0;
		}
		for (int i=0; i<severities.length; i++){
			double before = 0.00001, after = 0;
			for (int j=0; j<severities[i].length; j++){
				avg[i] += severities[i][j];
				if (i<profile.getUserProblems().getSystemIndex(i))
					before += severities[i][j];
				else if (i>profile.getUserProblems().getSystemIndex(i))
					after += severities[i][j];
			}
			bal[i] = after/before;
			weights[i] = (int)(avg[i]*50+bal[i]*20) + ((i>0)?weights[i-1]:0);
			System.err.print(weights[i]+"  -- ");
		}
		Random rand = new Random();
		int pick = rand.nextInt(weights[weights.length-1]);
		System.err.println(pick+" out of "+weights[weights.length-1]);
		for (int i=0; i<weights.length; i++){
			if (weights[i]>pick){
				nextProbs.add(new NextActivities(i, profile.getUserProblems().getSystemIndex(i)));
				break;
			}
		}
		return nextProbs;
	}

}
