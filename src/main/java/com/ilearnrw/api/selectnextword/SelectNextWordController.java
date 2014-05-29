package com.ilearnrw.api.selectnextword;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;
import com.ilearnrw.app.games.mapping.GamesInformation;

@Controller
public class SelectNextWordController {

	@Autowired
	IProfileProvider profileProvider;

	@RequestMapping(value = "/activity/new_data", method = RequestMethod.GET)
	public @ResponseBody
	List<GameElement> selectNextWords(
			@RequestParam(value = "activity", required = true) String activity,
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "probId", required = true) String probId,
			@RequestParam(value = "count", required = true) int count,
			@RequestParam(value = "difficultyLevel", required = false) Integer difficultyLevel,
			@RequestParam(value = "evaluation_mode", required = true) int evaluationMode,
			@RequestParam(value = "new_words_percentage", required = false) Integer newWordsPercentage,
			@RequestParam(value = "only_tricky_words", required = false) Boolean onlyTrickyWords) {
		String[] parts = probId.split("_");
		int i = Integer.parseInt(parts[0]);
		int j = Integer.parseInt(parts[1]);
		if (difficultyLevel == null)
			difficultyLevel = 0;

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
		int appId = GamesInformation.getAppID(activity);
		if (appId==-1)
			return null;
		if (!(GamesInformation.getAppRelatedProblems(appId, lc)).contains(i))
			return null;

		if (appId == 4){
			List<GameElement> result = new ArrayList<GameElement>();
			ProblemWordListLoader pwll = new ProblemWordListLoader();
			pwll.loadSentences(lc);
			EasyHardList thelist = new EasyHardList(pwll.getSentenceList());
			for (String w : thelist.getRandom(count, difficultyLevel)) {
				result.add(new GameElement(w, null));
			}
			return result;
		}
		else{
			List<GameElement> result = new ArrayList<GameElement>();
			ProblemWordListLoader pwll = new ProblemWordListLoader(lc, i, j);
			EasyHardList thelist = new EasyHardList(pwll.getSentenceList());
	
			for (String w : thelist.getRandom(count, difficultyLevel)) {
				if (lc==LanguageCode.EN)
					result.add(new GameElement(false, new EnglishWord(w), i, j));
				else
					result.add(new GameElement(false, new GreekWord(w), i, j));
			}
			return result;
		}
	}
}
