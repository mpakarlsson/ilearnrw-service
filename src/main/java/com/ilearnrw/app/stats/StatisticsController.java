package com.ilearnrw.app.stats;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatisticsController {
	@RequestMapping(value = "/stats")
	public String login() {
		return "stats";
	}
}
