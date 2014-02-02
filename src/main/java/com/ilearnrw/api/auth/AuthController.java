package com.ilearnrw.api.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilearnrw.app.usermanager.model.User;
import com.ilearnrw.common.security.AuthTokenData;
import com.ilearnrw.common.security.RefreshTokenData;
import com.ilearnrw.common.security.RestToken;
import com.ilearnrw.common.security.TokenUtils;
import com.ilearnrw.common.security.Tokens;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class AuthController {

	private static Logger LOG = Logger.getLogger(AuthController.class);

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private TeacherStudentService teacherStudentService;

	@Autowired
	private KeyBasedPersistenceTokenService tokenService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@PreAuthorize("hasPermission('', 'PERM1')")
	public @ResponseBody
	String test() {
		return "test back";
	}

	@RequestMapping(value = "/user/newtokens", method = RequestMethod.GET)
	public @ResponseBody
	Tokens newTokens(
			@RequestParam(value = "refresh", required = true) String refreshToken,
			HttpServletResponse httpResponse) {

		Token receivedToken = tokenService.verifyToken(refreshToken);

		if (TokenUtils.isExpired(receivedToken)) {
			throw new BadCredentialsException("timeout");
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			RefreshTokenData upp = mapper.readValue(
					receivedToken.getExtendedInformation(),
					RefreshTokenData.class);
			return authenticateUser(upp.getUserName(), upp.getPassword(),
					upp.getTeacherUser(), upp.getTeacherPass(), httpResponse);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new BadCredentialsException("invalid refresh token");
	}

	@RequestMapping(value = "/user/auth", method = RequestMethod.GET)
	public @ResponseBody
	Tokens authenticateUser(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "pass", required = true) String pass,
			@RequestParam(value = "teacher", required = false) String teacher,
			@RequestParam(value = "teacher_pass", required = false) String teacherPass,
			HttpServletResponse httpResponse) {

		RefreshTokenData upp = new RefreshTokenData(username, pass, teacher,
				teacherPass);
		AuthTokenData atd = new AuthTokenData(username, teacher);
		ObjectMapper mapper = new ObjectMapper();

		try {

			UserDetails userDetails = userDetailsService
					.loadUserByUsername(username);

			if (userDetails.getPassword().compareTo(pass) != 0) {
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
						"Invalid user credentials");
				LOG.debug(String.format("Invalid credentials for user %s",
						username));
				return null;
			}

			// if we received infos about teacher, then check them, they must be
			// valid!
			if (teacher != null && !teacher.isEmpty() && teacherPass != null
					&& !teacherPass.isEmpty()) {
				UserDetails teacherDetails = userDetailsService
						.loadUserByUsername(teacher);

				if (teacherDetails.getPassword().compareTo(teacherPass) != 0) {
					httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
							"Invalid credentials for teacher");
					LOG.debug(String.format(
							"Invalid credentials for teacher %s", teacher));
					return null;
				}

				if (!teacherStudentService.isUserStudentOfTeacher(username,
						teacher)) {
					httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
							"User is not assigned to teacher!");
					LOG.debug(String.format(
							"User %s is not assigned to teacher %s", username,
							teacher));
					return null;
				}
			}

			String serializedRefreshTokenData = mapper.writeValueAsString(upp);
			String serializedAuthTokenData = mapper.writeValueAsString(atd);

			Token refreshToken = tokenService
					.allocateToken(serializedRefreshTokenData);
			Token authToken = tokenService
					.allocateToken(serializedAuthTokenData);
			return new Tokens(authToken.getKey(), refreshToken.getKey());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/user/supervising_teacher", method = RequestMethod.GET)
	public @ResponseBody
	String getSupervisingTeacher(
			@RequestParam(value = "token", required = true) String token) {
		Token receivedToken = tokenService.verifyToken(token);
		ObjectMapper mapper = new ObjectMapper();
		AuthTokenData data;
		try {
			data = mapper.readValue(receivedToken.getExtendedInformation(),
					AuthTokenData.class);
			return data.getTeacher();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/user/details/{username}", method = RequestMethod.GET)
	public @ResponseBody
	User userDetailsById(@PathVariable String username) {
		User user = userService.getUserByUsername(username);
		return user;
	}

	@RequestMapping(value = "/user/roles", method = RequestMethod.GET)
	public @ResponseBody
	List<String> userRoles() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority authority : auth.getAuthorities()) {
			roles.add(authority.getAuthority());

		}
		return roles;
	}

	@RequestMapping(value = "/user/id", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	public @ResponseBody
	String userId() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		return auth.getPrincipal().toString();
	}

	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public @ResponseBody
	List<User> getUserIdList() {
		return userService.getUserList();
	}

	@RequestMapping(value = "/user/firstUserByLanguage/{language}", method = RequestMethod.GET)
	public @ResponseBody
	User getUserByLanguage(@PathVariable String language) {
		for (User user : userService.getUserList()) {
			if (user.getLanguage().equals(language))
				return user;
		}
		return null;
	}
}