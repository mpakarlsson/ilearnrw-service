package com.ilearnrw.api.selectnextword;

import ilearnrw.languagetools.WordDictionary;
import ilearnrw.languagetools.greek.GreekDictionary;
import ilearnrw.structs.sets.SortedTreeSet;
import ilearnrw.textclassification.Word;
import ilearnrw.user.problems.wordlists.ProblemsWordLists;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.datalogger.services.CubeServiceImpl;
import com.ilearnrw.api.profileAccessUpdater.DbProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.app.games.mapping.GamesInformation;

@Controller
public class SelectNextWordController {

	@Autowired
	IProfileProvider profileProvider;

	private static Logger LOG = Logger
			.getLogger(SelectNextWordController.class);

	@RequestMapping(value = "/activity/new_data", method = RequestMethod.GET)
	public @ResponseBody
	List<GameElement> selectNextWords(
			@RequestParam(value = "activity", required = true) String activity,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "probId", required = true) String probId,
			@RequestParam(value = "count", required = true) int count,
			@RequestParam(value = "evaluation_mode", required = true) int evaluationMode,
			@RequestParam(value = "new_words_percentage", required = false) Integer newWordsPercentage,
			@RequestParam(value = "only_tricky_words", required = false) Boolean onlyTrickyWords) {
		String[] parts = probId.split("_");
		int i = Integer.parseInt(parts[0]);
		int j = Integer.parseInt(parts[1]);

		UserProfile user = null;
		try {
			user = profileProvider.getProfile(""+userId);
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

		ProblemsWordLists pwl = new ProblemsWordLists(lc);
		WordDictionary dictionary = pwl.get(i, j);
		CubeService cs = new CubeServiceImpl();
		SortedTreeSet subList = dictionary.getWords();
		List<GameElement> result = new ArrayList<GameElement>();
		ArrayList<Word> words = subList.getRandomElementsNotIn(count, null);
		for (Word w : words) {
			System.err.println(w.toString());
			result.add(new GameElement(false, w, i, j));
		}
		return result;
	}
}