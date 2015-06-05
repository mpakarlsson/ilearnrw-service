package com.ilearnrw.api.selectnextactivity;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.user.profile.UserProfile;

import java.util.List;

public interface NextActivitiesProvider {
	public List<NextActivities> getNextProblems(UserProfile profile);
}
