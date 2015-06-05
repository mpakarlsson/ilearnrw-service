package com.ilearnrw.api.info.services;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilearnrw.api.info.dao.InfoDao;
import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.model.Problem;

@Service
public class InfoServiceImpl implements InfoService {
	
	InfoDao infoDao;
	
	private List<Problem> englishProbs;
	private List<Problem> greekProbs;
	private List<Application> apps;
	
	private List<int[]> appsProbsCorrespondanceEN;
	private List<int[]> appsProbsCorrespondanceGR;	

	@Autowired
	public InfoServiceImpl(InfoDao infoDao)
	{
		this.infoDao = infoDao;
		apps = infoDao.getApps();
		englishProbs = getProblems("EN");
		greekProbs = getProblems("GR");
		
		appsProbsCorrespondanceEN = getAppsProblemCorrespondence("EN");
		appsProbsCorrespondanceGR = getAppsProblemCorrespondence("GR");

	}

	@Override
	public Application getApplication(int id) {
		
		for (Application app: apps)
			if (app.getId() == id)
				return app;
		return null;

	}
	
	@Override
	public Application getApplicationByAppId(String appId) {
		for (Application app: apps)
			if (app.getAppId().compareToIgnoreCase(appId) == 0)
				return app;
		return null;
	}


	@Override
	public List<Application> getApps() {
		return apps;
	}

	@Override
	public List<Problem> getProblems(String language) {
		return infoDao.getProblems(language);
	}

	@Override
	public List<int[]> getAppsProblemCorrespondence(
			String language) {
		return infoDao.getAppsProblemCorrespondence(language);
	}
	

	public int getAppID(String appName) {
		for (Application app: apps)
			if (app.getName().equalsIgnoreCase(appName))
				return app.getId();
		return -1;
	}

	public Problem getProblem(String probName, LanguageCode lan) {
		if (lan == LanguageCode.EN) {
			for (Problem p: englishProbs)
				if (p.getTitle().equalsIgnoreCase(probName))
					return p;
		} else {
			for (Problem p: greekProbs)
				if (p.getTitle().equalsIgnoreCase(probName))
					return p;
		}
		return null;
	}

	public List<Integer> getAppRelatedProblems(int appId,
			LanguageCode lan) {
		List<Integer> res = new ArrayList<Integer>();
		if (lan == LanguageCode.EN) {
			for(int[] pair: appsProbsCorrespondanceEN) {
				if (pair[0] == appId)
					res.add(pair[1]);
			}
		} else {
			for(int[] pair: appsProbsCorrespondanceGR) {
				if (pair[0] == appId)
					res.add(pair[1]);
			}
		}
		return res;
	}

	public List<Integer> getAppRelatedProblems(String appName,
			LanguageCode lan) {
		int id = getAppID(appName);
		if (id==-1)
			return null;
		return getProblemRelatedApps(id, lan);
	}

	public List<Integer> getProblemRelatedApps(int probId,
			LanguageCode lan) {
		List<Integer> res = new ArrayList<Integer>();
		if (lan == LanguageCode.EN) {
			for(int[] pair: appsProbsCorrespondanceEN) {
				if (pair[1] == probId)
					res.add(pair[0]);
			}
		} else {
			for(int[] pair: appsProbsCorrespondanceGR) {
				if (pair[1] == probId)
					res.add(pair[0]);
			}
		}
		return null;
	}

	public List<Integer> getProblemRelatedApps(String prob,
			LanguageCode lan) {
		int id = getProblem(prob, lan).getId();
		return getProblemRelatedApps(id, lan);
	}
		
	public boolean allowsLetters(int appId){
		return getApplication(appId).isLetters();
	}
	public boolean allowsLetters(String appName){
		int id = getAppID(appName);
		if (id==-1)
			return false;
		return allowsLetters(id);
	}
	
	public boolean allowsWords(int appId){
		return getApplication(appId).isWords();
	}
	public boolean allowsWords(String appName){
		int id = getAppID(appName);
		if (id==-1)
			return false;
		return allowsWords(id);
	}
	
	public boolean allowsSentences(int appId){
		return getApplication(appId).isSentences();
	}
	public boolean allowsSentences(String appName){
		int id = getAppID(appName);
		if (id==-1)
			return false;
		return allowsSentences(id);
	}

}
