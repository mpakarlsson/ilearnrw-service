package com.ilearnrw.app.stats;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatisticsController {
	
	@RequestMapping(value = "/tests/logs")
	public String testLogs() {
		return "tests/logs";
	}
	
	@RequestMapping(value = "/stats")
	public String stats() {
		return "statistics/stats";
	}
}
