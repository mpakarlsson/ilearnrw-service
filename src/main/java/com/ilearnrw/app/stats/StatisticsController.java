package com.ilearnrw.app.stats;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
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
