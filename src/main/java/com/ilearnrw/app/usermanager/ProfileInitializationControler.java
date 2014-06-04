package com.ilearnrw.app.usermanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ilearnrw.languagetools.extras.EasyHardList;
import ilearnrw.textclassification.english.EnglishWord;
import ilearnrw.textclassification.greek.GreekWord;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.api.selectnextword.GameElement;
import com.ilearnrw.api.selectnextword.tools.ProblemWordListLoader;
import com.ilearnrw.common.AuthenticatedRestClient;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class ProfileInitializationControler {

	@Autowired
	private UserService userService;
	
	@Autowired
	IProfileProvider profileProvider;
	
	// find css style at src/main/webapp/resources/css/style.css
	
	@RequestMapping(value = "/users/{userId}/initialize", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfileInitializationForm(@PathVariable int userId, 
			@RequestParam(value="category", required = true) Integer category, 
			@RequestParam(value="start", required = true) Integer start, 
			@RequestParam(value="end", required = true) Integer end, ModelMap model) 
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		User current = null;
		if (category == null || start == null || end == null){
			category = -1;
			start = -1;
			end = -1;
		}
		int index = (start+end)/2;
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}

		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		List<GameElement> result = new ArrayList<GameElement>();
		ProblemWordListLoader pwll = new ProblemWordListLoader(LanguageCode.GR, category, index);
		EasyHardList thelist = new EasyHardList(pwll.getSentenceList());

		for (String w : thelist.getRandom(7, 0))
				result.add(new GameElement(false, new EnglishWord(w), category, index));
		
		model.put("userId", userId);
		model.put("username", current.getUsername());
		model.put("category", category);
		model.put("index", index);
		model.put("profile", profile);
		model.put("problems", profile.getUserProblems().getUserSeverities()
				.getSystemIndices());
		model.put("result", result);
				
		return "users/profile.initialize";
	}
	
	@RequestMapping(value = "/users/{userId}/initprofile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfileInitializationPage(@PathVariable int userId, ModelMap model) 
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		User current = null;
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}

		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		model.put("userId", userId);
		model.put("username", current.getUsername());
		model.put("profile", profile.getUserProblems());
		model.put("problems", profile.getUserProblems().getProblems().getProblemsIndex());
				
		return "users/profile.startpage";
	}
	
	@RequestMapping(value = "/users/{userId}/confirmanswers", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewConfirmAnswersPage(@PathVariable int userId, 
			@RequestParam(value="succeed", required = false) String[] succeed, 
			@RequestParam(value="words", required = true) String[] words, ModelMap model) 
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		User current = null;
		ArrayList<String> wordlist = convertArrayToUTF8(words);
		ArrayList<String> succeedlist = convertArrayToUTF8(succeed);
		System.err.println(wordlist.get(0));
		try {
			current = userService.getUser(userId);
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			System.err.println(e.toString());
		}

		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.fromString(current.getLanguage()));
			profile = profileProvider.getProfile(userId);
		}
		
		model.put("userId", userId);
		model.put("username", current.getUsername());
		model.put("succeedlist", succeedlist);
		model.put("wordlist", wordlist);
		model.put("profile", profile.getUserProblems());
		model.put("problems", profile.getUserProblems().getProblems().getProblemsIndex());
				
		return "users/profile.confirmpage";
	}
	
	private ArrayList<String> convertArrayToUTF8(String str[]) throws UnsupportedEncodingException{
		if (str == null)
			return new ArrayList<String>();
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i<str.length; i++){
			byte[] in = str[i].getBytes("iso-8859-1");
			res.add(new String(in,"UTF-8"));
		}
		return res;
	}
}
