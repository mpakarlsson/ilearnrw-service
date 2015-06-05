package com.ilearnrw.api.selectnextword;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;



@Controller
public class UoMSelectNextWordController {

	@Autowired
	IProfileProvider profileProvider;
	
	private static Logger LOG = Logger
			.getLogger(UoMSelectNextWordController.class);
	
	@RequestMapping(value = "/activity/new_data_UoM", method = RequestMethod.GET)
	public @ResponseBody
	List<GameElement> selectNextWordsUoM(
			@RequestParam(value = "activity", required = true) String activity,
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "probId", required = true) String probId,
			@RequestParam(value = "count", required = true) int count,
			@RequestParam(value = "difficultyLevel", required = true) String difficultyLevel,
			@RequestParam(value = "evaluation_mode", required = true) int evaluationMode,
			@RequestParam(value = "new_words_percentage", required = false) Integer newWordsPercentage,
			@RequestParam(value = "only_tricky_words", required = false) Boolean onlyTrickyWords) {
		
		String[] parts = probId.split("_");
		int languageArea = Integer.parseInt(parts[0]);
		int difficulty = Integer.parseInt(parts[1]);
		
		
		UserProfile user = null;
		try {
			user = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if (user == null)
			return null;
		
		
		LanguageCode lc = user.getLanguage();

		GameLevel level = LevelFactory.createLevel(lc, languageArea, activity);	
		
		LOG.debug(String.format("Gathering words"));

		
		if (level!=null){
			
			return level.getWords(new LevelParameters(difficultyLevel), languageArea, difficulty);
			
		}else
			return null;
		
		
		
	/*	if (appId == 4){
			List<GameElement> result = new ArrayList<GameElement>();
			ProblemWordListLoader pwll = new ProblemWordListLoader();
			pwll.loadSentences(lc);
			EasyHardList thelist = new EasyHardList(pwll.getSentenceList());
			for (String w : thelist.getRandom(count, difficultyLevel)){
				result.add(new GameElement(SentenceLoaderControler.fromString(w)));
			}
			return result;
		}*/
	}
}
