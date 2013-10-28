package com.ilearnrw.usermanager;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);

	@RequestMapping(value = "/test")
	public ModelAndView test() {

		LOG.info("Returning home view");
		String serverTime = (new Date()).toString();
		ModelAndView modelView = new ModelAndView("home");
		modelView.addObject("serverTime", serverTime);
		return modelView;
	}

}