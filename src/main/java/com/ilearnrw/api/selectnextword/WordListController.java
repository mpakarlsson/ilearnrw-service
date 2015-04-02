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
public class WordListController {

	@Autowired
	IProfileProvider profileProvider;

	@RequestMapping(value = "/wordlists/problem_data", method = RequestMethod.GET)
	public @ResponseBody List<GameElement> returnWordList(
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "probId", required = true) String probId) {
		String[] parts = probId.split("_");
		int i = Integer.parseInt(parts[0]);
		int j = Integer.parseInt(parts[1]);

		UserProfile user = null;
		try {
			user = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user == null)
			return null;
		LanguageCode lc = user.getLanguage();

		List<GameElement> result = new ArrayList<GameElement>();
		ProblemWordListLoader pwll = new ProblemWordListLoader(lc, i, j);
		EasyHardList thelist = new EasyHardList(pwll.getItems());
		ArrayList<String> tt = thelist.getAll();
		for (String w : tt) {
			if (lc == LanguageCode.EN)
				result.add(new GameElement(false, new EnglishWord(w), i, j));
			else
				result.add(new GameElement(false, new GreekWord(w), i, j));
		}
		return result;
	}
}
