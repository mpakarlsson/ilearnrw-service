package com.ilearnrw.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {

	private static Logger LOG = Logger.getLogger(CacheController.class);
	@Autowired(required = false)
	CacheManager cacheManager;

	@RequestMapping(value = "/cache", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, List<String>> clearCache(
			@RequestParam(value = "clear", required = false) boolean clear) {

		if (cacheManager == null) {
			return null;
		}

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		for (String cacheName : cacheManager.getCacheNames()) {
			ConcurrentMapCache cache = (ConcurrentMapCache) cacheManager
					.getCache(cacheName);
			if (cache == null) {
				continue;
			}

			List<String> list = new ArrayList<String>();
			map.put(cacheName, list);
			if (clear) {
				LOG.debug(String.format("Cache %s cleared", cacheName));
				cache.clear();
			} else {

				Set set = cache.getNativeCache().entrySet();
				for (Object o : set) {
					list.add(o.toString());
				}
			}
		}
		return map;
	}
}