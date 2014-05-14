package com.ilearnrw.app.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
public class EndpointController {
	private final RequestMappingHandlerMapping handlerMapping;

	@Autowired
	public EndpointController(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	@RequestMapping(value = "/endpoints", method = RequestMethod.GET)
	public String endpoints(Model model) {
		model.addAttribute("handlerMethods",
				this.handlerMapping.getHandlerMethods());
		model.addAttribute("title", "Web application endpoints");
		return "endpoints";
	}
}
