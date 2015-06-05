package com.ilearnrw.api.info.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;
import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.model.Problem;

public interface InfoDao {

	public List<Application> getApps();

	public List<Problem> getProblems(String language);

	public List<int[]> getAppsProblemCorrespondence(
			String language);

	public Application getApplication(int id);
	
}
