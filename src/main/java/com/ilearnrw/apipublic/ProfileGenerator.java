package com.ilearnrw.apipublic;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import ilearnrw.resource.ResourceLoader;
import ilearnrw.textclassification.Word;
import ilearnrw.user.UserPreferences;
import ilearnrw.user.problems.ProblemDefinitionIndex;
import ilearnrw.user.profile.UserProblems;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.user.profile.UserSeverities;
import ilearnrw.utils.LanguageCode;

public class ProfileGenerator {
	public static UserProfile createProfile(LanguageCode language, int value) throws IOException {
		UserProfile up = new UserProfile(language, createProblems(
				createDefinitionIndex(language), value),
				new UserPreferences(14));
		return up;
	}

	private static ProblemDefinitionIndex createDefinitionIndex(
			LanguageCode language) throws IOException {
		ProblemDefinitionIndex res;
		if (language == LanguageCode.EN) {
			String json = ResourceLoader.getInstance().readAllLinesAsStringUTF8(ResourceLoader.Type.DATA, "problem_definitions_en.json");
			res = (ProblemDefinitionIndex) new Gson().fromJson(json,
					ProblemDefinitionIndex.class);
		} else {
			String json = ResourceLoader.getInstance().readAllLinesAsStringUTF8(ResourceLoader.Type.DATA, "problem_definitions_greece.json");
			res = (ProblemDefinitionIndex) new Gson().fromJson(json,
					ProblemDefinitionIndex.class);
		}
		return res;
	}

	private static UserSeverities createSeverities(ProblemDefinitionIndex pdi,
			int value) {
		UserSeverities us = new UserSeverities(pdi.getProblems().length);
		for (int i = 0; i < pdi.getProblems().length; i++) {
			us.constructRow(i, pdi.getProblems()[i].length);
		}
		for (int i = 0; i < us.getLength(); i++) {
			us.getSystemIndices()[i] = 0;
			us.getTeacherIndices()[i] = 0;
			int curValue = value;
			if (pdi.getProblemDefinition(i).getSeverityType().equals("binary"))
				curValue = value < 2 ? value : 1;
			for (int j = 0; j < us.getSeverities()[i].length; j++) {
				us.setSeverity(i, j, curValue);
			}
		}
		return us;
	}

	private static UserProblems createProblems(ProblemDefinitionIndex pdi,
			int value) {
		UserProblems up = new UserProblems(pdi);
		up.setTrickyWords(new ArrayList<Word>());
		up.setUserSeverities(createSeverities(pdi, value));
		return up;
	}
}
