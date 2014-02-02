package com.ilearnrw.api.selectnextword;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SelectNextWordController {

	private static Logger LOG = Logger.getLogger(SelectNextWordController.class);

	@RequestMapping(value = "/activity/words", method = RequestMethod.GET)
	public @ResponseBody
	void selectNextWords() {
		return;
	}
}