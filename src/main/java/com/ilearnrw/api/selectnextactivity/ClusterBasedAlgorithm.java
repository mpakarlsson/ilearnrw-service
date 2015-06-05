package com.ilearnrw.api.selectnextactivity;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.user.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClusterBasedAlgorithm implements NextActivitiesProvider {

	// calculate previous covered clusters percentage, pccp(i), for each row:
	// pccp(i) = (clusters of previous rows that the user has already play games
	// for) / (all clusters of the previous rows)
	// define pccp(0) = 1

	// calculate covered percentage, cp(i), for each row:
	// cp(i) = (working index) / (length of row)

	// find active rows of the profile:
	// -- a row is active if pccp(i)>=0.25
	// -- but, a row is NOT active if cp(i) = 1 and there is at least one row,
	// say j, with cp(j)<1

	// calculate the next problem to play games for (select the row using the
	// normal distribution):
	// we put the most upper profile rows near center and the most lower to the
	// edges of the distribution
	// if we have x active rows then we use normal distribution with x/2
	// standard deviations
	// (so, with a possibility of 95% we select one of the active rows)
	// if we fail to hit an active row (that is, we are in the other 5%)
	// we repeat and choose randomly one row and one index of the profile.
	@Override
	public List<NextActivities> getNextProblems(UserProfile profile) {
		List<NextActivities> nextProbs = new ArrayList<NextActivities>();
		double pccp[] = getPccp(profile);
		double cp[] = getCp(profile);
		int activeArray[] = getActiveArray(pccp, cp);
		Random rand = new Random();
		int selection = (int) Math.round(rand.nextGaussian()*(activeArray.length/2)) + activeArray.length/2;
		if (0 <= selection && selection < activeArray.length){
			nextProbs.add(new NextActivities(activeArray[selection], profile.getUserProblems().getSystemIndex(activeArray[selection])));
		}
		else {
			int i = rand.nextInt(profile.getUserProblems().getNumerOfRows());
			int j = rand.nextInt(profile.getUserProblems().getSystemIndex(i)+1);
			nextProbs.add(new NextActivities(i, j));
		}
		return nextProbs;
	}

	private double[] getPccp(UserProfile profile) {
		double pccp[] = new double[profile.getUserProblems().getNumerOfRows()];
		pccp[0] = 1;
		int alreadyPlayed = 0;
		for (int i = 0; i < pccp.length - 1; i++) {
			int cl = -1;
			for (int j = 0; j < profile.getUserProblems().getSystemIndex(i); j++) {
				if (profile.getUserProblems().getProblems()
						.getProblemDescription(i, j).getCluster() != cl) {
					alreadyPlayed++;
					cl = profile.getUserProblems().getProblems()
							.getProblemDescription(i, j).getCluster();
				}
			}
			pccp[i + 1] = (double) alreadyPlayed
					/ profile.getUserProblems().getProblems()
							.getProblemDescription(i + 1, 0).getCluster();
		}
		return pccp;
	}

	private double[] getCp(UserProfile profile) {
		double cp[] = new double[profile.getUserProblems().getNumerOfRows()];
		for (int i = 0; i < cp.length - 1; i++) {
			cp[i] = (double) profile.getUserProblems().getSystemIndex(i)
					/ profile.getUserProblems().getRowLength(i);
		}
		return cp;
	}

	private int[] getActiveArray(double pccp[], double cp[]) {
		boolean active[] = new boolean[cp.length];
		boolean completedFlag = true;
		for (int i = 0; i < cp.length - 1; i++) {
			if (cp[i] < 1) {
				completedFlag = false;
				break;
			}
		}
		
		int allActive = 0;
		for (int i = 0; i < cp.length - 1; i++) {
			if (cp[i] == 1 && !completedFlag)
				active[i] = false;
			else if (pccp[i] >= 0.25){
				active[i] = true;
				allActive++;
			}
			else
				active[i] = false;
		}
		int activeArray[] = new int[2*allActive];
		
		int j = 0;
		for (int i = cp.length - 1; i>=0; i--) {
			if (active[i]) {
				activeArray[j] = i;
				activeArray[activeArray.length - 1 - j] = i;
				j++;
			}
		}
		return activeArray;
	}
}
