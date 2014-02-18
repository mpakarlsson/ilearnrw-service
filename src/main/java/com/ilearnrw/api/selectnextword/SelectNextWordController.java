package com.ilearnrw.api.selectnextword;

import ilearnrw.languagetools.WordDictionary;
import ilearnrw.languagetools.greek.GreekDictionary;
import ilearnrw.structs.sets.SortedTreeSet;
import ilearnrw.textclassification.Word;
import ilearnrw.user.problems.wordlists.ProblemsWordLists;
import ilearnrw.utils.LanguageCode;

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
import com.ilearnrw.api.datalogger.services.CubeServiceImpl;

@Controller
public class SelectNextWordController {
	
	private static Logger LOG = Logger
			.getLogger(SelectNextWordController.class);

	@RequestMapping(value = "/activity/next_words", method = RequestMethod.GET)
	public @ResponseBody
	List<Word> selectNextWords(
			@RequestParam(value = "activity", required = true) String activity,
			@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "probId", required = true) String probId,
			@RequestParam(value = "count", required = true) int count,
			@RequestParam(value = "percent_new_words", required = false) Integer percentNewWords,
			@RequestParam(value = "only_tricky_words", required = false) Boolean onlyTrickyWords) {
		ProblemsWordLists pwl = new ProblemsWordLists(LanguageCode.GR);
		String[] parts = probId.split("_");
		int i = Integer.parseInt(parts[0]);
		int j = Integer.parseInt(parts[1]);
		WordDictionary dictionary = pwl.get(i, j);
		CubeService cs = new CubeServiceImpl();
		SortedTreeSet subList = dictionary.getWords();
		List<Word> result = new ArrayList<Word>();
		int ii = 0;
		for (Word w : subList) {
			result.add(w);
			if (ii++ >= 10)
				break;
		}
		return result;
	}
}