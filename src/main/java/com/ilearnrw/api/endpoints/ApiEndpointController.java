package com.ilearnrw.api.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
public class ApiEndpointController {
	private final RequestMappingHandlerMapping handlerMapping;

	@Autowired
	public ApiEndpointController(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root(Model model) {
		return "endpoints/endpointsMain";
	}

	@RequestMapping(value = "/endpoints-api", method = RequestMethod.GET)
	public String endpoints(Model model) {
		model.addAttribute("handlerMethods",
				this.handlerMapping.getHandlerMethods());
		model.addAttribute("title", "API endpoints");
		return "endpoints/endpoints";
	}
}
