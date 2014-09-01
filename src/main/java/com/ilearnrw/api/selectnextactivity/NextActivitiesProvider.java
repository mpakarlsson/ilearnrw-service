package com.ilearnrw.api.selectnextactivity;

import ilearnrw.user.profile.UserProfile;

import java.util.List;

public interface NextActivitiesProvider {
	public List<NextActivities> getNextProblems(UserProfile profile);
}
