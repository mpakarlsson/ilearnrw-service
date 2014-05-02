package com.ilearnrw.api.info.services;

import ilearnrw.utils.LanguageCode;

import java.util.List;

import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.model.Problem;

public interface InfoService {
	public Application getApplication(int id);

	public List<Application> getApps();

	public List<Problem> getProblems(String language);

	public List<int[]> getAppsProblemCorrespondence(
			String language);

	public List<Integer> getAppRelatedProblems(int appId,
			LanguageCode fromString);

}
