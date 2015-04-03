package com.ilearnrw.api.publicapi;

import java.io.IOException;
import ilearnrw.textclassification.Classifier;
import ilearnrw.textclassification.Text;
import ilearnrw.textclassification.TextClassificationResults;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

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
public class PublicTextClassificationController {

	@Autowired
	IProfileProvider profileProvider;

	private static final Logger log = LoggerFactory
			.getLogger(TextClassificationResults.class);

	@RequestMapping(headers = { "Accept=application/json" }, value = "/public/text/classify", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody
	TextClassificationResults classify(HttpServletRequest request,
			@RequestParam(value = "lc", required = true) String language, @Valid @RequestBody String analyzeText)
			throws ProfileProviderException, IOException {
		int defaultSeverity = 3;
		UserProfile profile = ProfileGenerator.createProfile(LanguageCode.fromString(language), defaultSeverity);

		Text text = new Text(analyzeText, profile.getLanguage());
		Classifier cls = new Classifier(profile, text);
		cls.calculateProblematicWords(false);

		return cls.getUserProblemsToText().getTextClassificationResults();
	}
}
