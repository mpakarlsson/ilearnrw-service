package com.ilearnrw.services.rest;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ilearnrw.languagetools.LanguageAnalyzerAPI;
import ilearnrw.languagetools.english.EnglishLanguageAnalyzer;
import ilearnrw.languagetools.greek.GreekLanguageAnalyzer;
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
	
    private static final Logger log = LoggerFactory.getLogger(TextClassificationResults.class);

	@RequestMapping(headers = {"Accept=application/json"},
					value = "/text/classify/{userId}", 
					method = RequestMethod.POST,
					produces = "text/plain;charset=UTF-8")
	public @ResponseBody TextClassificationResults classify(HttpServletRequest request,
														   @PathVariable String userId,
														   @Valid @RequestBody String analyzeText) throws ProfileProviderException, FileNotFoundException
	{
		UserProfile profile = profileProvider.getProfile(userId);
		LanguageAnalyzerAPI languageAnalyzer = null;
		InputStream greekDictionary = request.getSession().getServletContext().getResourceAsStream("/data/greek_dictionary.txt");
		InputStream greekSoundDictionary = request.getSession().getServletContext().getResourceAsStream("/data/greek_sound_similarity.txt");
		if (greekDictionary == null)
			throw new FileNotFoundException("/data/greek_sound_similarity.txt");
		if (greekSoundDictionary == null)
			throw new FileNotFoundException("/data/greek_sound_similarity.txt");
		if( profile.getLanguage() == LanguageCode.EN)
			languageAnalyzer = new EnglishLanguageAnalyzer();
		else
			languageAnalyzer = new GreekLanguageAnalyzer(greekDictionary, greekSoundDictionary);

		Text text = new Text(analyzeText, profile.getLanguage());
		Classifier cls = new Classifier(profile, text, languageAnalyzer);
		cls.calculateProblematicWords(false);
		
		return cls.getUserProblemsToText().getTextClassificationResults();
	}
}
