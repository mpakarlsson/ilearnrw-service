package com.ilearnrw.api.info.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.model.Problem;

public interface InfoService {
	public Application getApplication(int id);
	public Application getApplicationByAppId(String appId);

	public List<Application> getApps();

	public List<Problem> getProblems(String language);

	public List<int[]> getAppsProblemCorrespondence(
			String language);

	public List<Integer> getAppRelatedProblems(int appId,
			LanguageCode fromString);

}
