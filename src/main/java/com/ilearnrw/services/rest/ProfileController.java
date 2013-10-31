package com.ilearnrw.services.rest;

import ilearnrw.user.User;
import ilearnrw.user.UserSeverities;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController {

	private static Logger LOG = Logger.getLogger(ProfileController.class);

	@RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
	public @ResponseBody
	User getProfile(@PathVariable int id) {

		return new User(id);
	}

}