package com.ilearnrw.app.usermanager;

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
	public String viewUserUpdateForm(@PathVariable int userId, 
			@RequestParam(value="category", required = false) Integer category, 
			@RequestParam(value="index", required = false) Integer index, ModelMap model) 
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		User current = null;
		if (category == null || index == null){
			category = -1;
			index = -1;
		}
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
		model.put("category", category);
		model.put("index", index);
		model.put("profile", profile);
		model.put("problems", profile.getUserProblems().getUserSeverities()
				.getSystemIndices());
		return "users/profile.initialize";
	}
}