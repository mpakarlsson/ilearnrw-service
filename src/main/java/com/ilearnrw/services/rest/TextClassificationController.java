package com.ilearnrw.services.rest;

import ilearnrw.languagetools.LanguageAnalyzerAPI;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.languagetools.greek.GreekLanguageAnalyzer;
import ilearnrw.textclassification.Classifier;
import ilearnrw.textclassification.Text;
import ilearnrw.textclassification.TextClassificationResults;
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.services.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.services.profileAccessUpdater.IProfileProvider.ProfileProviderException;

@Controller
public class TextClassificationController {
	
	@Autowired
	IProfileProvider profileProvider;

	@RequestMapping(headers = {"Accept=application/json"},
					value = "/text/classify/{userId}", 
					method = RequestMethod.POST,
					produces = "text/plain;charset=UTF-8")
	public @ResponseBody TextClassificationResults classify(@PathVariable String userId,
														   @Valid @RequestBody String analyzeText) throws ProfileProviderException
	{

		UserProfile profile = profileProvider.getProfile(userId);
		LanguageAnalyzerAPI languageAnalyzer = null;
		if( profile.getLanguage() == LanguageCode.EN)
			languageAnalyzer = new EnglishLanguageAnalyzer();
		else
			languageAnalyzer = new GreekLanguageAnalyzer();

		Text text = new Text(analyzeText, profile.getLanguage());
		Classifier cls = new Classifier(profile, text, languageAnalyzer);
		cls.calculateProblematicWords(false);
		
		return cls.getUserProblemsToText().getTextClassificationResults();
	}
}
