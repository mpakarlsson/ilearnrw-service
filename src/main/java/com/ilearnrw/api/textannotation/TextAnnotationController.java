package com.ilearnrw.api.textannotation;

import ilearnrw.annotation.HtmlGenerator;
import ilearnrw.languagetools.LanguageAnalyzerAPI;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.languagetools.greek.GreekLanguageAnalyzer;
import ilearnrw.textclassification.Classifier;
import ilearnrw.textclassification.Text;
import ilearnrw.textclassification.TextClassificationResults;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.io.FileNotFoundException;

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
public class TextAnnotationController {

	@Autowired
	IProfileProvider profileProvider;

	private static final Logger log = LoggerFactory
			.getLogger(HtmlGenerator.class);

	@RequestMapping(headers = { "Accept=application/json" }, value = "/text/annotate", 
			method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	HtmlGenerator annotate(HttpServletRequest request,
			@RequestParam("userId") String userId, @RequestParam("lc") String language, 
			@Valid @RequestBody String text)
			throws ProfileProviderException, FileNotFoundException {
		UserProfile profile = profileProvider.getProfile(userId);
		LanguageCode lc = LanguageCode.EN;
		if (language.equalsIgnoreCase("GR"))
			lc = LanguageCode.GR;
		HtmlGenerator hg = new HtmlGenerator(text, profile, lc, "html/template.html");

		return hg;
	}
}