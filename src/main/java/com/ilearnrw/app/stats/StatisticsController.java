package com.ilearnrw.app.stats;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatisticsController {
	@RequestMapping(value = "/stats")
	public String stats() {
		return "stats";
	}
	
	@RequestMapping(produces = {"text/plain", "application/json"}, value = "/stats/test")
	public @ResponseBody String test()
	{
		return "{ \"test\":\"1\", \"json\":\"7\", \"now\": \"3\"}";
	}
}
