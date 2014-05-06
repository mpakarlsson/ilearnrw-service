package com.ilearnrw.app.stats;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.app.stats.model.RequestTest;
import com.ilearnrw.app.stats.model.ResponseTest;

@Controller
public class StatisticsController {
	@RequestMapping(value = "/stats")
	public String stats() {
		return "stats";
	}
	
	@RequestMapping(produces = {"application/json"}, value = "/stats/test2")
	public @ResponseBody String test2()
	{
		return "{ \"testA\":\"5\", \"jsonB\":\"3\", \"nowC\": \"8\", \"sometimeD\": \"8\"}";
	}
	
	@RequestMapping(produces = {"application/json"}, value = "/stats/test1")
	public @ResponseBody ResponseTest test1()
	{
		ResponseTest responseTest = new ResponseTest();
		responseTest.qwe = 3;
		responseTest.asd = 5;
		return responseTest;
	}
	
	@RequestMapping(produces = {"application/json"}, value = "/stats/test2")
	public @ResponseBody ResponseTest test2(@RequestBody RequestTest requestTest)
	{
		ResponseTest responseTest = new ResponseTest();
		return responseTest;
	}
	
	@RequestMapping(produces = {"application/json"}, value = "/stats/test3")
	public @ResponseBody Map<String, Integer> test3(@RequestBody RequestTest requestTest)
	{
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("hi", 5);
		map.put("there", 2);
		return map;
	}
}
