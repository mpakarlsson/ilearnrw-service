package com.ilearnrw.api.publicapi;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.annotation.AnnotatedPack;
import ilearnrw.annotation.HtmlGenerator;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;

@Controller
public class PublicTextAnnotationController {

	@Autowired
	IProfileProvider profileProvider;

	private static final Logger log = LoggerFactory
			.getLogger(HtmlGenerator.class);

	@RequestMapping(headers = { "Accept=application/json" }, value = "/public/text/annotate", 
			method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	AnnotatedPack annotate(HttpServletRequest request, @RequestParam(value = "lc", required = true) String language, 
			@Valid @RequestBody String text)
			throws ProfileProviderException, IOException {
		int defaultSeverity = 3;
		UserProfile profile = ProfileGenerator.createProfile(LanguageCode.fromString(language), defaultSeverity);
		LanguageCode lc = LanguageCode.EN;
		if (language.equalsIgnoreCase("GR"))
			lc = LanguageCode.GR;
		HtmlGenerator hg = new HtmlGenerator(text, profile, lc, "html/template.html");
		AnnotatedPack ap = new AnnotatedPack();
		ap.setHtml(hg.getHtml());
		ap.setTrickyWordList(hg.getTrickyWordsList());
		ap.setWordSet(hg.getWordSet());
		
		return ap;
	}
}
