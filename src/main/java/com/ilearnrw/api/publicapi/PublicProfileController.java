package com.ilearnrw.api.publicapi;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.io.IOException;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;

@Controller
class PublicProfileController {

	@RequestMapping(value = "/public/profile", method = RequestMethod.GET)
	public @ResponseBody
	UserProfile getProfile(
			@RequestParam(value = "lc", required = true) String language)
			throws ProfileProviderException, IOException {
		int defaultSeverityValue = 3;
		return ProfileGenerator.createProfile(LanguageCode.fromString(language), defaultSeverityValue);
	}
	
}