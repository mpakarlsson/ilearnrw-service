package com.ilearnrw.app.games.mapping;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import com.ilearnrw.api.selectnextword.GameLevel;
import com.ilearnrw.api.selectnextword.LevelFactory;

public class GamesInformation {
	public static GamesInformation mapping = new GamesInformation();
	/*private static String englishProbs[] = { "Phon-Graph", 
			"Vowel Sounds", "Letter Patterns", "Syllable Division",
			"Suffixing", "Prefixing", "Confusing Letter Shapes", 
			//"Letter Names", "Sight Words",
			};*/
	private static String englishProbs[] = { "Consonants", 
		"Vowels", "Blends and Letter Patterns", "Syllables",
		"Suffixes", "Prefixes", "Confusing letters", 
		//"Letter Names", "Sight Words",
		};	
	
	
	private static String greekProbs[] = { "Syllable Division",
			"Phonemes:Consonants", "Phonemes:Vowels", "Suffixing:Derivational",
			"Suffixing:Inflectional/Grammatical", "Prefixing",
			"Grapheme/Phoneme Correspondence", "Grammar/Function Words",
			"Word Recognition", "Visual Similarity" };

	private static String apps[] = { "Mail Sorter", "Whack a Mole",
			"Endless Runner", "Harvest", "Serenade Hero", "Moving Pathways",
			"Eye Exam", "Train Dispatcher", "Drop Chops", "Game World",
			"Social Network", "Logging Screen", "Profile Setup" };

	public static String appIds[] = { "MAIL_SORTER", "WHAK_A_MOLE",
			"ENDLESS_RUNNER", "HARVEST", "SERENADE_HERO", "MOVING_PATHWAYS",
			"EYE_EXAM", "TRAIN_DISPATCHER", "DROP_CHOPS", "GAME_WORLD",
			"SOCIAL_NETWORK", "LOGGING_SCREEN", "PROFILE_SETUP" };

	private static boolean appsProbsCorrespondanceEN[][] = {
		// Consonants
		{ true, 	true, 	false, true, false, true, false, true, false, false, false, false, false },
		// Vowels
		{ true, 	true, 	false, true, false, true, true, false, false, false, false, false, false },
		// Blends and letter patterns
		{ true, 	true, 	false, true, false, true, false, false, false, false, false, false, false },
		// Syllables
		{ false, 	false, 	true, true, false, true, true, true, true, false, false, false, false },
		// suffixes
		{ true, 	true, 	false, true, true, false, true, true, true, false, false, false, false },
		// Prefixes
		{ true, 	true, 	false, true, true, false, true, true, true, false, false, false, false },
	// lett names
	// { true, true, false, true, false, false, false, false, false, false,
	// false, false, false },
	// sight
	// { true, true, false, false, false, true, false, true, false, false,
	// false, false, false },
		// conf lett
		{ false, 	true, 	false, true, true, true, true, true, false, false, false, false, false }
	};

	private static boolean appsProbsCorrespondanceGR[][] = {
		// phon:cons
		{ true, false, false, false, false, true, true, false, false, false, false, false, false },
		// phon:vow
		{ false, false, false, true, false, true, true, false, false, false, false, false, false },
		// lett vis sim
		{ true, true, false, false, false, true, false, false, false, false, false, false, false }, 
		// gp
		{ true, true, false, true, false, true, false, false, false, false, false, false, false },
		// syl div
		{ false, false, true, true, false, true, true, true, true, false, false, false, false },
		// pref
		{ true, false, true, true, true, false, true, true, true, false, false, false, false },
		// der suf
		{ true, true, true, true, true, false, true, true, true, false, false, false, false },
		// inf suf
		{ true, true, true, true, true, false, true, true, false, false, false, false, false },
		// gram fun words
		{ false, false, false, true, true, true, false, true, false, false, false, false, false }
			// word rec
			// { true, true, false, false, false, true, true, true, false,
			// false, false, false, false },
		};

	// letter, word, sentence
	private static boolean appsInputsCorrespondance[][] = {
			// "Mail Sorter"
			{ false, true, false },
			// "WhackaMole"
			{ false, true, false },
			// "Endless Runner"
			{ false, true, false },
			// "Harvest"
			{ false, true, false },
			// "Serenade Hero"
			{ false, false, true },
			// "Moving Pathways"
			{ true, true, false },
			// "Eye Exam"
			{ false, true, false },
			// "Typing Train Dispatcher"
			{ false, true, false },
			// "Drop Chop"
			{ false, true, false },
			// "Game World"
			{ false, false, false },
			// "Social Network"
			{ false, false, false },
			// "Logging Screen"
			{ false, false, false },
			// "Profile Setup"
			{ false, false, false } };

	private GamesInformation() {
	}

	public static GamesInformation getInstance() {
		return mapping;
	}

	public static int getAppID(String appName) {
		for (int i = 0; i < apps.length; i++)
			if (apps[i].equalsIgnoreCase(appName))
				return i;
		return -1;
	}

	public static int getAppIDfromStringID(String appStringId) {
		for (int i = 0; i < appIds.length; i++)
			if (appIds[i].equalsIgnoreCase(appStringId))
				return i;
		return -1;
	}

	public static int getProblemID(String probName, LanguageCode lan) {
		if (lan == LanguageCode.EN) {
			for (int i = 0; i < englishProbs.length; i++)
				if (englishProbs[i].equalsIgnoreCase(probName))
					return i;
		} else {
			for (int i = 0; i < greekProbs.length; i++)
				if (greekProbs[i].equalsIgnoreCase(probName))
					return i;
		}
		return -1;
	}
	
	
	

	public static ArrayList<Integer> getAppRelatedProblems(int appId,
			LanguageCode lan) {
		System.err.println("GamesInformation.getAppRelatedProblems: deprecated. tables outdated");

		ArrayList<Integer> res = new ArrayList<Integer>();
		if (lan == LanguageCode.EN) {
			if (appId > appsProbsCorrespondanceEN[0].length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceEN.length; i++)
				if (appsProbsCorrespondanceEN[i][appId])
					res.add(i);
		} else {
			if (appId > appsProbsCorrespondanceGR[0].length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceGR.length; i++) {
				if (appsProbsCorrespondanceGR[i][appId]) {
					res.add(i);
				}
			}
		}
		return res;
	}

	public static ArrayList<Integer> getAppRelatedProblems(String appStringID,
			LanguageCode lan) {
		System.err.println("GamesInformation.getAppRelatedProblems: deprecated. tables outdated");

		int id = getAppIDfromStringID(appStringID);
		if (id == -1)
			return null;
		else
			return getAppRelatedProblems(id,lan);
		/*ArrayList<Integer> res = new ArrayList<Integer>();
		if (lan == LanguageCode.EN) {
			if (id > appsProbsCorrespondanceEN[0].length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceEN.length; i++)
				if (appsProbsCorrespondanceEN[i][id])
					res.add(i);
		} else {
			if (id > appsProbsCorrespondanceGR[0].length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceGR.length; i++)
				if (appsProbsCorrespondanceGR[i][id])
					res.add(i);
		}
		return res;*/
	}

	public static ArrayList<Integer> getProblemRelatedApps(int languageArea,
			LanguageCode lan) {
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for(int i=0;i<9;i++){

			
			GameLevel gl = LevelFactory.createLevel(lan, languageArea, appIds[i]);
			if(gl!=null)
			if(gl.allowedDifficulty(languageArea, 0))
				result.add(i);
			
		}
		
		return result;
		

		
		/*ArrayList<Integer> res = new ArrayList<Integer>();
		if (lan == LanguageCode.EN) {
			if (probId > appsProbsCorrespondanceEN.length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceEN[probId].length; i++)
				if (appsProbsCorrespondanceEN[probId][i])
					res.add(i);
		} else {
			if (probId > appsProbsCorrespondanceGR.length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceGR[probId].length; i++)
				if (appsProbsCorrespondanceGR[probId][i])
					res.add(i);
		}
		return res;*/
	}

	public static ArrayList<Integer> getProblemRelatedApps(String prob,
			LanguageCode lan) {

		int id = getProblemID(prob, lan);
		return getProblemRelatedApps(id, lan);
	}

	public static ArrayList<String> getProblemRelatedAppsString(int probId,
			LanguageCode lan) {

		ArrayList<Integer> resInteger = getProblemRelatedApps(probId, lan);
		
		ArrayList<String> res = new ArrayList<String>();
		
		for(int i : resInteger)
			res.add(appIds[i]);
		
		return res;
		
		
		/*if (lan == LanguageCode.EN) {
			if (probId > appsProbsCorrespondanceEN.length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceEN[probId].length; i++)
				if (appsProbsCorrespondanceEN[probId][i])
					res.add(appIds[i]);
		} else {
			if (probId > appsProbsCorrespondanceGR.length)
				return null;
			for (int i = 0; i < appsProbsCorrespondanceGR[probId].length; i++)
				if (appsProbsCorrespondanceGR[probId][i])
					res.add(appIds[i]);
		}
		return res;*/
	}

	public static ArrayList<String> getProblemRelatedAppsString(String prob,
			LanguageCode lan) {
		int id = getProblemID(prob, lan);
		return getProblemRelatedAppsString(id, lan);
	}

	public static boolean allowsLetters(int appId) {
		System.err.println("GamesInformation.allowsLetters: deprecated.  tables outdated");
		return appsInputsCorrespondance[appId][0];
	}

	public static boolean allowsLetters(String appName) {
		System.err.println("GamesInformation.allowsLetters: deprecated.  tables outdated");

		int id = getAppID(appName);
		if (id == -1)
			return false;
		return allowsLetters(id);
	}

	public static boolean allowsWors(int appId) {
		System.err.println("GamesInformation.allowsWords: deprecated.  tables outdated");

		return appsInputsCorrespondance[appId][1];
	}

	public static boolean allowsWors(String appName) {
		System.err.println("GamesInformation.allowsWords: deprecated.  tables outdated");

		int id = getAppID(appName);
		if (id == -1)
			return false;
		return allowsWors(id);
	}

	public static boolean allowsSentences(int appId) {
		System.err.println("GamesInformation.allowsWords: deprecated.  tables outdated");

		return appsInputsCorrespondance[appId][2];
	}

	public static boolean allowsSentences(String appName) {
		System.err.println("GamesInformation.allowsWords: deprecated.  tables outdated");

		int id = getAppID(appName);
		if (id == -1)
			return false;
		return allowsSentences(id);
	}
}
