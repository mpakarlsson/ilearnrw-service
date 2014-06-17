package com.ilearnrw.api.info;

import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.services.InfoService;

@Controller
public class InfoController {

	private static Logger LOG = Logger.getLogger(InfoController.class);

	InfoService infoService;
	

	@RequestMapping(value = "/info/app", method = RequestMethod.GET)
	public @ResponseBody
	Application getApplication(
			@RequestParam(value = "id", required = true) int id) {
		return infoService.getApplication(id);
	}
	
	@RequestMapping(value = "/info/version", method = RequestMethod.GET)
	public @ResponseBody
	String getVersion() {
		return "5";
	}
	@RequestMapping(value = "/info/app/{id}/problems", method = RequestMethod.GET)
	public @ResponseBody
	List<Integer> getApplicationProblems(
			@PathVariable("id") int appId,
			@RequestParam(value = "lang", required = false) String language) {
		return infoService.getAppRelatedProblems(appId, LanguageCode.fromString(language));
	}

	@Autowired
	public InfoController(InfoService infoService) {
		this.infoService = infoService;
	}

}
