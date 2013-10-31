package com.ilearnrw.usermanager;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilearnrw.services.security.Tokens;

@Controller
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);

	@RequestMapping(value = "/home")
	public ModelAndView home(Principal principal, HttpServletRequest request) {

		LOG.info("Returning home view");
		String serverTime = (new Date()).toString();
		ModelAndView modelView = new ModelAndView("home");
		modelView.addObject("serverTime", serverTime);
		modelView.addObject("username", request.getSession().getAttribute("user"));
		modelView.addObject("principal", principal.getName());

		return modelView;
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();
		return "login";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("pass") String pass, ModelMap model, HttpServletRequest request) {
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders() {
			{
				byte[] encodedAuthorisation = Base64.encode(new String(
						"api:api").getBytes());
				add("Authorization", "Basic "
						+ new String(encodedAuthorisation));
			}
		};

		String authUri = "http://localhost:8080/test/user/auth?username={username}&pass={pass}";
		try {
			ResponseEntity<Tokens> response = template.exchange(authUri,
					HttpMethod.GET, new HttpEntity<Object>(headers),
					Tokens.class, username, pass);
			Tokens tokens = response.getBody();
			request.getSession().setAttribute("tokens", tokens);

		} catch (Exception e) {
			throw new BadCredentialsException("Invalid credentials provided");
		}

		String detailsUri = "http://localhost:8080/test/user/id?token={token}";
		try {
			Tokens tokens = (Tokens) request.getSession()
					.getAttribute("tokens");
			String token = tokens.getAuthToken();
			ResponseEntity<String> detailsResponse = template.exchange(
					detailsUri, HttpMethod.GET,
					new HttpEntity<Object>(headers), String.class, token);
			String user = detailsResponse.getBody();
//			String userJSON = detailsResponse.getBody();
//
//			ObjectMapper mapper = new ObjectMapper();
//			User user = mapper.readValue(userJSON, User.class);
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//			authorities.addAll(user.getAuthorities());
			Authentication userAuthentication = new UsernamePasswordAuthenticationToken(
					user, token, authorities);
			
			SecurityContextHolder.getContext().setAuthentication(
					userAuthentication);

			request.getSession()
					.setAttribute("user", detailsResponse.getBody());

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "login";
		}
		return "redirect:home";
	}
}