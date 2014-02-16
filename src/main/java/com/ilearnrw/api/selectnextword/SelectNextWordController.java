package com.ilearnrw.api.selectnextword;

import ilearnrw.languagetools.greek.GreekDictionary;
import ilearnrw.languagetools.greek.GreekDictionaryLoader;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.greek.GreekWord;

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

@Controller
public class SelectNextWordController {
	
	private static Logger LOG = Logger
			.getLogger(SelectNextWordController.class);

	@RequestMapping(value = "/activity/next_words", method = RequestMethod.GET)
	public @ResponseBody
	List<Word> selectNextWords(
			@RequestParam(value = "activity", required = true) String activity,
			@RequestParam(value = "count", required = true) int count,
			@RequestParam(value = "percent_new_words", required = false) Integer percentNewWords,
			@RequestParam(value = "only_tricky_words", required = false) Boolean onlyTrickyWords) {
		//GreekDictionaryLoader loader = new GreekDictionaryLoader();
		GreekDictionary dictionary = new GreekDictionary();
		dictionary.loadWords();
		List<Word> subList = dictionary.getWords().subList(0, count);
		List<Word> result = new ArrayList<Word>();
		for (Word w : subList) {
			result.add(w);
		}
		return result;
	}
}