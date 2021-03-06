package com.ilearnrw.app.usermanager;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.model.LogEntryResult;
import com.ilearnrw.api.datalogger.model.filters.DateFilter;
import com.ilearnrw.api.datalogger.model.filters.StudentFilter;
import com.ilearnrw.api.datalogger.model.filters.DateFilter.DateFilterType;
import com.ilearnrw.api.datalogger.model.filters.StudentFilter.StudentFilterType;
import com.ilearnrw.api.datalogger.model.result.BreakdownResult;
import com.ilearnrw.api.datalogger.model.result.GamesComparisonResult;
import com.ilearnrw.api.datalogger.model.result.OverviewBreakdownResult;
import com.ilearnrw.api.datalogger.model.result.ReaderComparisonResult;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.info.model.Application;
import com.ilearnrw.api.info.services.InfoService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.app.usermanager.form.ExpertTeacherForm;
import com.ilearnrw.app.usermanager.form.RolePermissionsForm;
import com.ilearnrw.app.usermanager.form.TeacherStudentForm;
import com.ilearnrw.app.usermanager.form.UserNewForm;
import com.ilearnrw.app.usermanager.jquery.model.ApplicationBreakdownRequest;
import com.ilearnrw.app.usermanager.jquery.model.ApplicationDataPoint;
import com.ilearnrw.app.usermanager.jquery.model.BreakdownFilter;
import com.ilearnrw.app.usermanager.jquery.model.SkillDataPoint;
import com.ilearnrw.app.usermanager.jquery.model.SkillBreakdownRequest;
import com.ilearnrw.common.AuthenticatedRestClient;
import com.ilearnrw.common.security.Tokens;
import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.ExpertTeacherService;
import com.ilearnrw.common.security.users.services.PermissionService;
import com.ilearnrw.common.security.users.services.RoleService;
import com.ilearnrw.common.security.users.services.StudentDetailsService;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);

	@Autowired
	@Qualifier("usersDataSource")
	private DataSource dSource;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private TeacherStudentService teacherStudentService;

	@Autowired
	private ExpertTeacherService expertTeacherService;

	@Autowired
	private StudentDetailsService studentDetailsService;

	@Autowired
	private CubeService cubeService;

	@Autowired
	private InfoService infoService;

	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	private AuthenticatedRestClient restClient;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public String panel(ModelMap modelMap) {
		return "main/panel";
	}

	@RequestMapping(value = "users/{id}/stats", method = RequestMethod.GET)
	public String stats(ModelMap modelMap) {
		return "statistics/stats";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();
		return "redirect:/apps/login";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "main/login";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "main/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("pass") String pass, ModelMap model,
			HttpServletRequest request) {

		try {
			Tokens tokens = restClient.getTokens(username, pass);

			List<String> roles = restClient.getRoles(tokens.getAuthToken());

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			Authentication userAuthentication = new UsernamePasswordAuthenticationToken(
					username, tokens.getAuthToken(), authorities);

			SecurityContextHolder.getContext().setAuthentication(
					userAuthentication);

			request.getSession().setAttribute("username", username);
			User user = userService.getUserByUsername(username);
			request.getSession().setAttribute("userid", user.getId());
			request.getSession().setAttribute("students",
					teacherStudentService.getStudentList(user));

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "main/login";
		}
		return "redirect:/apps/panel";
	}

	/* Users logs */

	@RequestMapping(value = "users/manage")
	public String manageUsers(ModelMap modelMap, HttpServletRequest request,
			ModelMap model) {
		if (request.isUserInRole("PERMISSION_ADMIN"))
			modelMap.addAttribute("users", getUsersManagedByAdmin());
		else if (request.isUserInRole("PERMISSION_EXPERT"))
			modelMap.addAttribute("users", getUsersManagedByExpert());
		else if (request.isUserInRole("PERMISSION_TEACHER"))
			modelMap.addAttribute("users", getUsersManagedByTeacher());
		model.put("teachersList", roleService.getUsersWithRole("ROLE_TEACHER"));
		return "users/manage";
	}

	@RequestMapping(value = "users/students")
	public String manageStudents(ModelMap modelMap, HttpServletRequest request,
			ModelMap model) {
		if (request.isUserInRole("PERMISSION_TEACHER")) {
			modelMap.addAttribute("students", getUsersManagedByTeacher());
		}
		return "users/students";
	}

	private List<UserNewForm> getUsersManagedByAdmin() {
		List<User> users = userService.getUserList();
		List<UserNewForm> userRoles = new ArrayList<UserNewForm>();
		for (User user : users) {
			List<Role> roles = roleService.getRoleList(user);
			StudentDetails sd = studentDetailsService.getStudentDetails(user);
			if (roles.size() == 1)
				userRoles
						.add(new UserNewForm(user, roles.get(0).getName(), sd));
			else {
				userRoles.add(new UserNewForm(user, "none", sd));
			}
		}
		return userRoles;
	}

	private List<UserNewForm> getUsersManagedByExpert() {
		User current = userService.getUserByUsername(SecurityContextHolder
				.getContext().getAuthentication().getName());
		List<User> teachers = expertTeacherService.getTeacherList(current);
		List<User> users = new ArrayList<User>();
		for (User teacher : teachers) {
			users.add(teacher);
			users.addAll(teacherStudentService.getStudentList(teacher));
		}
		users.addAll(teacherStudentService.getUnassignedStudentsList());
		List<UserNewForm> userRoles = new ArrayList<UserNewForm>();
		for (User user : users) {
			List<Role> roles = roleService.getRoleList(user);
			StudentDetails sd = studentDetailsService.getStudentDetails(user);
			if (roles.size() == 1)
				userRoles
						.add(new UserNewForm(user, roles.get(0).getName(), sd));
			else {
				userRoles.add(new UserNewForm(user, "none", sd));
			}
		}
		return userRoles;
	}

	private List<UserNewForm> getUsersManagedByTeacher() {
		User current = userService.getUserByUsername(SecurityContextHolder
				.getContext().getAuthentication().getName());
		List<User> users = teacherStudentService.getStudentList(current);
		List<UserNewForm> userRoles = new ArrayList<UserNewForm>();
		for (User user : users) {
			List<Role> roles = roleService.getRoleList(user);
			StudentDetails sd = studentDetailsService.getStudentDetails(user);
			if (roles.size() == 1)
				userRoles
						.add(new UserNewForm(user, roles.get(0).getName(), sd));
			else {
				userRoles.add(new UserNewForm(user, "none", sd));
			}
		}
		return userRoles;
	}

	@RequestMapping(value = "users/{id}/logs/page/{page}", method = RequestMethod.GET)
	public String viewLogs(@PathVariable int id, @PathVariable String page,
			ModelMap model, HttpServletRequest request) {

		Map<String, String> args = new HashMap<String, String>();
		User user = userService.getUser(id);
		args.put("username", user.getUsername());
		args.put("page", page);
		for (String param : Arrays.asList("timestart", "timeend", "tags",
				"applicationId"))
			if (request.getSession().getAttribute(param) != null)
				args.put(param,
						(String) request.getSession().getAttribute(param));
		LogEntryResult result = restClient.getLogs(args);
		model.addAttribute("logEntryResult", result);
		return "users/logs";
	}

	@RequestMapping(value = "users/{id}/logs/page/{page}", method = RequestMethod.POST)
	public String viewLogsFiltered(
			@PathVariable int id,
			@PathVariable String page,
			@RequestParam(value = "timestart", required = false) String timestart,
			@RequestParam(value = "timeend", required = false) String timeend,
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "applicationId", required = false) String applicationId,
			ModelMap model, HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("timestart", timestart);
		map.put("timeend", timeend);
		map.put("tags", tags);
		map.put("applicationId", applicationId);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isEmpty())
				request.getSession().setAttribute(entry.getKey(),
						entry.getValue());
			else
				request.getSession().removeAttribute(entry.getKey());
		}

		return viewLogs(id, page, model, request);
	}

	/* Users profile */

	@RequestMapping(value = "users/{id}/profile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfile(@PathVariable int id, ModelMap model)
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		try {
			profile = profileProvider.getProfile(id);
		} catch (ProfileProviderException e) {
			LOG.error(e);
		}

		if (profile == null) {
			throw new Exception("No profile available.");
		}

		User user = new User();
		user.setId(id);
		//TODO: remove this when severities are implemented properly in the games
		profile.getUserProblems().getUserSeverities().setIndicesFromCount(profile.getUserProblems().getProblems());
		model.put("userId", id);
		model.put("profile", profile);
		model.put("student", userService.getUser(id));
		model.put("studentDetails",
				studentDetailsService.getStudentDetails(user));
		return "users/profile";
	}

	@RequestMapping(value = "users/{id}/profile/set", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@ResponseBody
	public String updateProfileEntry(@RequestParam("level") Integer level,
			@RequestParam("type") String type,
			@RequestParam("category") Integer category,
			@RequestParam("index") Integer index, @PathVariable int id)
			throws ProfileProviderException {
		profileProvider.updateProfileEntry(id, category, index, level);
		// profileProvider.updateProfile(id, profile);
		return "OK";
	}

	@RequestMapping(value = "users/{id}/profile", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public String updateProfile(@ModelAttribute("profile") UserProfile profile,
			@PathVariable int id, HttpServletRequest request)
			throws ProfileProviderException {
		UserProfile oldProfile = profileProvider.getProfile(id);
		oldProfile.getUserProblems().setTrickyWords(
				profile.getUserProblems().getTrickyWords());
		oldProfile.getPreferences().setFontSize(
				profile.getPreferences().getFontSize());
		profileProvider.updateProfile(id, oldProfile);
		return "redirect:/apps/users/" + id + "/profile";
	}

	@RequestMapping(value = "users", method = RequestMethod.POST)
	@Transactional
	public String insert(@ModelAttribute("user") User user,
			BindingResult result, ModelMap model) {
		if (result.hasErrors())
			return "users/form.update";

		userService.insertData(user);

		model.put("message", "inserted");
		return "redirect:/apps/panel";
	}

	/* Users */

	@RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUserUpdateForm(@PathVariable int id, ModelMap model, HttpServletRequest request) {
		UserNewForm userForm = new UserNewForm();
		User user = userService.getUser(id);
		user.setPassword("");

		List<Role> selectedRoles = roleService.getRoleList(user);
		userForm.setUser(user);
		userForm.setRole(selectedRoles.get(0).getName());
		userForm.setStudentDetails(studentDetailsService
				.getStudentDetails(user));

		model.put("userform", userForm);
		model.put("schools", studentDetailsService.getSchools());
		// TODO: this has to go as ajax
		// model.put("classRooms", studentDetailsService.getClassRooms());
		model.put("teachersList", getTeachersAccessibleByCurrentUser(request));
		return "users/form.update";
	}

	@RequestMapping(value = "users/{id}/changepassword", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	String changePassword(@PathVariable int id,
			@RequestParam("password") String password,
			HttpServletRequest request) {

		try {
			userService.setPassword(id, password);
		} catch (Exception e) {
			return "FAILURE";
		}
		return "OK";
	}
	
	List<User> getTeachersAccessibleByCurrentUser(HttpServletRequest request)
	{
		if (request.isUserInRole("PERMISSION_ADMIN"))
			return teacherStudentService.getTeacherList();
		else if (request.isUserInRole("PERMISSION_EXPERT")) {
			User current = userService.getUserByUsername(SecurityContextHolder
					.getContext().getAuthentication().getName());
			return expertTeacherService.getTeacherList(current);
		}
		return null;
	}

	@RequestMapping(value = "users/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updateUser(@PathVariable int id,
			@Valid @ModelAttribute("userform") UserNewForm userForm,
			BindingResult result, ModelMap model, HttpServletRequest request) {
		User user = userForm.getUser();
		user.setId(id);
		userForm.setUser(user);
		if (user.getBirthdate().after(new Date()))
			result.rejectValue("user.birthdate", "birthdate.invalid");
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -100);
			if (user.getBirthdate().before(calendar.getTime()))
				result.rejectValue("user.birthdate", "birthdate.invalid");
		}
		if (result.hasErrors()) {
			model.put("schools", studentDetailsService.getSchools());
			// TODO: also, this
			// model.put("classRooms", studentDetailsService.getClassRooms());
			model.put("teachersList", getTeachersAccessibleByCurrentUser(request));
			return "users/form.update";
		}

		userService.updateData(userForm.getUser());
		if (userForm.getRole().compareTo("ROLE_STUDENT") == 0) {
			StudentDetails sd = userForm.getStudentDetails();
			sd.setStudentId(user.getId());
			studentDetailsService.updateData(userForm.getStudentDetails());
		}
		return "redirect:/apps/users/manage";
	}

	@RequestMapping(value = "users/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteUser(@PathVariable int id) {
		userService.deleteData(id);
		return "redirect:/apps/users/manage";
	}

	@RequestMapping(value = "users/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUserInsertForm(
			@ModelAttribute("userform") UserNewForm form, ModelMap model, HttpServletRequest request) {
		model.put("schools", studentDetailsService.getSchools());
		// TODO: and this
		// model.put("classRooms", studentDetailsService.getClassRooms());
		if (request.isUserInRole("PERMISSION_ADMIN"))
			model.put("teachersList", teacherStudentService.getTeacherList());
		else if (request.isUserInRole("PERMISSION_EXPERT")) {
			User current = userService.getUserByUsername(SecurityContextHolder
					.getContext().getAuthentication().getName());
			model.put("teachersList", expertTeacherService.getTeacherList(current));
		}
		return "users/form.insert";
	}

	@RequestMapping(value = "users/new", method = RequestMethod.POST)
	@Transactional
	public String insertUser(
			@Valid @ModelAttribute("userform") UserNewForm form,
			BindingResult result, HttpServletRequest request, ModelMap model)
			throws ProfileProviderException {

		User current = userService.getUserByUsername(SecurityContextHolder
				.getContext().getAuthentication().getName());
		User user = form.getUser();
		if (user.getBirthdate().after(new Date()))
			result.rejectValue("user.birthdate", "birthdate.invalid");
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -100);
			if (user.getBirthdate().before(calendar.getTime()))
				result.rejectValue("user.birthdate", "birthdate.invalid");
		}
		if (userService.getUserByUsername(user.getUsername()) != null)
			result.rejectValue("user.username", "user.username.exists");
		StudentDetails sd = form.getStudentDetails();
		if (form.getRole().equals("student")) {
			if (sd.getTeacherId() == null) {
				result.rejectValue("studentDetails.teacherId",
						"studentDetails.teacherId.enter");
			}
		}
		if (result.hasErrors()) {
			model.put("schools", studentDetailsService.getSchools());
			// TODO: and this
			// model.put("classRooms", studentDetailsService.getClassRooms());
			model.put("teachersList", teacherStudentService.getTeacherList());
			return "users/form.insert";
		}
		int userId = userService.insertData(user);
		if (form.getRole().equals("admin")) {
			roleService.setRoleList(user,
					Arrays.asList(roleService.getRole("ROLE_ADMIN")));
		} else if (form.getRole().equals("expert")) {
			roleService.setRoleList(user,
					Arrays.asList(roleService.getRole("ROLE_EXPERT")));
		} else if (form.getRole().equals("teacher")) {
			roleService.setRoleList(user,
					Arrays.asList(roleService.getRole("ROLE_TEACHER")));
			if (request.isUserInRole("PERMISSION_EXPERT"))
				expertTeacherService.assignTeacherToExpert(current, user);
		} else if (form.getRole().equals("student")) {
			roleService.setRoleList(user,
					Arrays.asList(roleService.getRole("ROLE_STUDENT")));
			sd.setStudentId(user.getId());
			studentDetailsService.insertData(sd);
			User teacher = userService.getUser(sd.getTeacherId());
			teacherStudentService.assignStudentToTeacher(teacher, user);
		}
		profileProvider.createProfile(userId,
				LanguageCode.fromString(user.getLanguage()));

		return "redirect:/apps/users/manage";
	}

	/* Roles */

	@RequestMapping(value = "roles/manage")
	public String manageRoles(ModelMap modelMap) {
		modelMap.addAttribute("roles", roleService.getRoleList());
		return "roles/manage";
	}

	@RequestMapping(value = "/roles/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewRoleUpdateForm(@PathVariable int id, ModelMap model) {
		RolePermissionsForm roleForm = new RolePermissionsForm();
		Role role = roleService.getRole(id);
		List<Permission> allPermissions = permissionService.getPermissionList();
		List<Permission> selectedPermissions = permissionService
				.getPermissionList(role);
		roleForm.setRole(role);
		roleForm.setAllPermissions(allPermissions);
		roleForm.setSelectedPermissions(selectedPermissions);
		model.put("roleform", roleForm);
		return "roles/form.update";
	}

	@RequestMapping(value = "roles/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updateRole(@PathVariable int id,
			@Valid @ModelAttribute("roleform") RolePermissionsForm roleForm,
			BindingResult result, ModelMap model) {
		Role role = roleForm.getRole();
		role.setId(id);
		roleForm.setRole(role);

		if (result.hasErrors()) {
			List<Permission> allPermissions = permissionService
					.getPermissionList();
			roleForm.setAllPermissions(allPermissions);
			return "roles/form.update";
		}

		roleService.updateData(roleForm.getRole());
		permissionService.setPermissionList(roleForm.getRole(),
				roleForm.getSelectedPermissions());
		return "redirect:/apps/roles/manage";
	}

	@RequestMapping(value = "roles/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteRole(@PathVariable int id) {
		roleService.deleteData(id);
		return "redirect:/apps/roles/manage";
	}

	@RequestMapping(value = "roles/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewRoleInsertForm(@ModelAttribute("role") Role role) {
		return "roles/form.insert";
	}

	@RequestMapping(value = "roles/new", method = RequestMethod.POST)
	@Transactional
	public String insertRole(@Valid @ModelAttribute("role") Role role,
			BindingResult result, ModelMap model) {

		if (result.hasErrors())
			return "roles/form.insert";
		roleService.insertData(role);

		model.put("message", "inserted");
		return "redirect:/apps/roles/manage";
	}

	/* Permissions */

	@RequestMapping(value = "permissions/manage")
	public String managePermissions(ModelMap modelMap) {
		modelMap.addAttribute("permissions",
				permissionService.getPermissionList());
		return "permissions/manage";
	}

	@RequestMapping(value = "/permissions/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewPermissionUpdateForm(@PathVariable int id, ModelMap model) {
		Permission permission = permissionService.getPermission(id);
		model.put("permission", permission);
		return "permissions/form.update";
	}

	@RequestMapping(value = "permissions/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updatePermission(@PathVariable int id,
			@Valid @ModelAttribute("permission") Permission permission,
			BindingResult result, ModelMap model) {

		if (result.hasErrors())
			return "permissions/form.update";
		permissionService.updateData(permission);
		return "redirect:/apps/permissions/manage";
	}

	@RequestMapping(value = "permissions/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deletePermission(@PathVariable int id) {
		permissionService.deleteData(id);
		return "redirect:/apps/permissions/manage";
	}

	@RequestMapping(value = "permissions/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewPermissionInsertForm(
			@ModelAttribute("permission") Permission permission) {
		return "permissions/form.insert";
	}

	@RequestMapping(value = "permissions/new", method = RequestMethod.POST)
	@Transactional
	public String insertPermission(
			@Valid @ModelAttribute("permission") Permission permission,
			BindingResult result, ModelMap model) {

		if (result.hasErrors())
			return "permissions/form.insert";
		permissionService.insertData(permission);

		model.put("message", "inserted");
		return "redirect:/apps/permissions/manage";
	}

	/* Teachers */

	@RequestMapping(value = "teachers/{id}/assign", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewTeachersAssignForm(@PathVariable int id, ModelMap model) {
		User teacher = userService.getUser(id);
		TeacherStudentForm teacherStudentForm = new TeacherStudentForm();
		teacherStudentForm.setTeacher(teacher);
		List<User> unassignedStudentsList = teacherStudentService
				.getUnassignedStudentsList();
		unassignedStudentsList.addAll(teacherStudentService
				.getStudentList(teacher));
		teacherStudentForm.setAllStudents(unassignedStudentsList);
		teacherStudentForm.setSelectedStudents(teacherStudentService
				.getStudentList(teacher));
		model.put("teacherStudentForm", teacherStudentForm);
		return "teachers/assign";
	}

	@RequestMapping(value = "teachers/{id}/assign", method = RequestMethod.POST)
	@Transactional
	public String viewTeachersAssignForm(
			@PathVariable int id,
			@ModelAttribute("teacherStudentForm") TeacherStudentForm teacherStudentForm,
			BindingResult result, ModelMap model, HttpServletRequest request) {
		User teacher = userService.getUser(id);
		teacherStudentService.setStudentList(teacher,
				teacherStudentForm.getSelectedStudents());
		request.getSession().setAttribute("students",
				teacherStudentService.getStudentList(teacher));
		return "redirect:/apps/users/manage";
	}

	/* Experts */

	@RequestMapping(value = "experts/{id}/assign", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewExpertsAssignForm(@PathVariable int id, ModelMap model) {
		User expert = userService.getUser(id);
		ExpertTeacherForm expertTeacherForm = new ExpertTeacherForm();
		expertTeacherForm.setExpert(expert);
		List<User> unassignedTeachersList = expertTeacherService
				//.getUnassignedTeachersList();
				.getAllTeachersList();
//		unassignedTeachersList.addAll(expertTeacherService
//				.getTeacherList(expert));
		expertTeacherForm.setAllTeachers(unassignedTeachersList);
		expertTeacherForm.setSelectedTeachers(expertTeacherService
				.getTeacherList(expert));
		model.put("expertTeacherForm", expertTeacherForm);
		return "experts/assign";
	}

	@RequestMapping(value = "experts/{id}/assign", method = RequestMethod.POST)
	@Transactional
	public String viewExpertsAssignForm(
			@PathVariable int id,
			@ModelAttribute("expertTeacherForm") ExpertTeacherForm expertTeacherForm,
			BindingResult result, ModelMap model, HttpServletRequest request) {
		User expert = userService.getUser(id);
		expertTeacherService.setTeacherList(expert,
				expertTeacherForm.getSelectedTeachers());
		request.getSession().setAttribute("teachers",
				expertTeacherService.getTeacherList(expert));
		return "redirect:/apps/users/manage";
	}

	@RequestMapping(value = "reports/overview", method = RequestMethod.GET)
	@Transactional
	public String reportsOverview(ModelMap model) {
		model.put("title", "Overview");
		model.put("js", "overview.js");
		return "reports/reports";
	}

	@RequestMapping(value = "reports/skill", method = RequestMethod.GET)
	@Transactional
	public String reportsSkills(ModelMap model) {
		model.put("title", "Skill breakdown");
		model.put("count", "No. of activity rounds");
		model.put("type", "skill");
		model.put("js", "skill.js");
		return "reports/reports";
	}

	@RequestMapping(value = "reports/activity", method = RequestMethod.GET)
	@Transactional
	public String reportsActivities(ModelMap model) {
		model.put("title", "Activity breakdown");
		model.put("count", "No. of activity rounds");
		model.put("type", "activity");
		model.put("js", "activity.js");
		return "reports/reports";
	}
	
	@RequestMapping(value = "reports/games-comparison", method = RequestMethod.GET)
	@Transactional
	public String reportsGamesComparison(ModelMap model) {
		model.put("title", "Student games comparison");
		model.put("js", "games-comparison.js");
		return "reports/tables";
	}
	
	@RequestMapping(value = "reports/reader-comparison", method = RequestMethod.GET)
	@Transactional
	public String reportsReaderComparison(ModelMap model) {
		model.put("title", "Student reader comparison");
		model.put("js", "reader-comparison.js");
		return "reports/tables";
	}

	@RequestMapping(value = "jquery/admin/schools", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	List<School> getSchools() {
		List<School> schools = studentDetailsService.getSchools();
		School all = new School();
		all.setId(-1);
		all.setName("All schools");
		schools.add(0, all);
		return schools;
	}

	@RequestMapping(value = "jquery/admin/classrooms", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	List<Classroom> getClassRooms(@RequestBody School school) {
		List<Classroom> classrooms = new ArrayList<Classroom>();
		Classroom all = new Classroom();
		all.setId(-1);
		all.setName("All classrooms");
		classrooms.add(all);
		if (school.getId() != -1)
			classrooms.addAll(studentDetailsService.getClassRooms(school));
		return classrooms;
	}

	@RequestMapping(value = "jquery/admin/students", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	List<User> getStudents(@RequestBody Classroom classroom) {
		List<User> students = new ArrayList<User>();
		User all = new User();
		all.setId(-1);
		all.setUsername("All students");
		students.add(0, all);
		if (classroom.getId() != -1)
			students.addAll(studentDetailsService
					.getStudentsFromClassRoom(classroom));
		else {
			students.addAll(teacherStudentService.getAllStudentsList());
		}
		return students;
	}

	private DateFilter getDateFilterFromBreakdownFilter(
			BreakdownFilter breakdownFilter) {
		DateFilter dateFilter = new DateFilter();
		switch (breakdownFilter.getDateType()) {
		case 0:
			dateFilter.setType(DateFilterType.ALL);
			break;
		case 1:
			dateFilter.setType(DateFilterType.TODAY);
			break;
		case 2:
			dateFilter.setType(DateFilterType.WEEK);
			break;
		case 3:
			dateFilter.setType(DateFilterType.MONTH);
			break;
		case 4:
			dateFilter.setType(DateFilterType.CUSTOM);
			dateFilter.setStartDate(breakdownFilter.getStartDate());
			dateFilter.setEndDate(breakdownFilter.getEndDate());
			break;
		default:
			break;
		}
		return dateFilter;
	}

	private StudentFilter getStudentFilterFromBreakdownFilter(
			BreakdownFilter breakdownFilter) {
		StudentFilter studentFilter = new StudentFilter();

		if (breakdownFilter.getStudent().getId() != -1) {
			studentFilter.setType(StudentFilterType.STUDENT);
			studentFilter.setName(breakdownFilter.getStudent().getUsername());
		} else if (breakdownFilter.getClassroom().getId() != -1) {
			studentFilter.setType(StudentFilterType.CLASSROOM);
			studentFilter.setName(breakdownFilter.getClassroom().getName());
		} else if (breakdownFilter.getSchool().getId() != -1) {
			studentFilter.setType(StudentFilterType.SCHOOL);
			studentFilter.setName(breakdownFilter.getSchool().getName());
		} else {
			studentFilter.setType(StudentFilterType.ALL);
			studentFilter.setName(null);
		}
		return studentFilter;
	}

	@RequestMapping(value = "jquery/admin/plot/skill/breakdown", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	List<SkillDataPoint> getSkillBreakdown(
			@RequestBody BreakdownFilter breakdownFilter) {
		User current = userService.getUserByUsername(SecurityContextHolder
				.getContext().getAuthentication().getName());
		List<com.ilearnrw.api.info.model.Problem> problems = infoService
				.getProblems(current.getLanguage());
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(breakdownFilter);
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(breakdownFilter);
		List<SkillDataPoint> skillBreakdown = new ArrayList<SkillDataPoint>();
		SkillDataPoint dataPoint;
		int problemCategoryCounter = 1;
		int languageCode = LanguageCode.fromString(current.getLanguage())
				.getCode();
		for (com.ilearnrw.api.info.model.Problem problem : problems) {
			dataPoint = new SkillDataPoint();
			dataPoint.setLabel(problem.getTitle());
			dataPoint.setId(problemCategoryCounter);
			dataPoint.setLanguage(languageCode);
			BreakdownResult breakdownResult = cubeService
					.getSkillBreakdownResult(dateFilter, studentFilter,
							problemCategoryCounter, languageCode);
			dataPoint.setData(breakdownResult.getTotalAnswers());
			if (dataPoint.getData() > 0)
				skillBreakdown.add(dataPoint);
			problemCategoryCounter++;
		}
		return skillBreakdown;
	}

	@RequestMapping(value = "jquery/admin/plot/skill/details", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	BreakdownResult getSkillDetails(
			@RequestBody SkillBreakdownRequest skillBreakdownRequest) {
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(skillBreakdownRequest
				.getFilter());
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(skillBreakdownRequest
				.getFilter());
		BreakdownResult breakdownResult = cubeService.getSkillBreakdownResult(
				dateFilter, studentFilter, skillBreakdownRequest.getDataPoint()
						.getId(), skillBreakdownRequest.getDataPoint()
						.getLanguage());
		return breakdownResult;
	}

	@RequestMapping(value = "jquery/admin/plot/activity/breakdown", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	List<ApplicationDataPoint> getActivityBreakdown(
			@RequestBody BreakdownFilter breakdownFilter) {
		List<Application> applications = infoService.getApps();
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(breakdownFilter);
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(breakdownFilter);
		List<ApplicationDataPoint> applicationBreakdown = new ArrayList<ApplicationDataPoint>();
		ApplicationDataPoint dataPoint;
		for (Application application : applications) {
			dataPoint = new ApplicationDataPoint();
			dataPoint.setLabel(application.getName());
			BreakdownResult breakdownResult = cubeService
					.getActivityBreakdownResult(dateFilter, studentFilter,
							application.getName());
			dataPoint.setData(breakdownResult.getTotalAnswers());
			if (dataPoint.getData() > 0)
				applicationBreakdown.add(dataPoint);
		}
		return applicationBreakdown;
	}

	@RequestMapping(value = "jquery/admin/plot/activity/details", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	BreakdownResult getApplicationDetails(
			@RequestBody ApplicationBreakdownRequest applicationBreakdownRequest) {
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(applicationBreakdownRequest
				.getFilter());
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(applicationBreakdownRequest
				.getFilter());
		BreakdownResult breakdownResult = cubeService
				.getActivityBreakdownResult(dateFilter, studentFilter,
						applicationBreakdownRequest.getDataPoint().getLabel());
		return breakdownResult;
	}

	@RequestMapping(value = "jquery/admin/plot/overview", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	OverviewBreakdownResult getOverviewBreakdown(
			@RequestBody BreakdownFilter breakdownFilter) {
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(breakdownFilter);
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(breakdownFilter);
		OverviewBreakdownResult overviewBreakdownResult = cubeService
				.getOverviewBreakdownResult(dateFilter, studentFilter);
		List<String> skills = new ArrayList<String>();
		User current = userService.getUserByUsername(SecurityContextHolder
				.getContext().getAuthentication().getName());
		List<com.ilearnrw.api.info.model.Problem> getProblems = infoService
				.getProblems(current.getLanguage());
		for (String id : overviewBreakdownResult.getSkillsWorkedOn()) {
			skills.add(getProblems.get(Integer.parseInt(id)).getTitle());
		}
		overviewBreakdownResult.setSkillsWorkedOn(skills);
		return overviewBreakdownResult;
	}
	
	@RequestMapping(value = "jquery/admin/tables/games-comparison", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	List<GamesComparisonResult> getGamesComparison(
			@RequestBody BreakdownFilter breakdownFilter) {
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(breakdownFilter);
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(breakdownFilter);
		List<GamesComparisonResult> gamesComparisonResult = cubeService
				.getGamesComparisonResult(dateFilter, studentFilter);
		return gamesComparisonResult;
	}
	
	@RequestMapping(value = "jquery/admin/tables/reader-comparison", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	List<ReaderComparisonResult> getReaderComparison(
			@RequestBody BreakdownFilter breakdownFilter) {
		DateFilter dateFilter = getDateFilterFromBreakdownFilter(breakdownFilter);
		StudentFilter studentFilter = getStudentFilterFromBreakdownFilter(breakdownFilter);
		List<ReaderComparisonResult> readerComparisonResult = cubeService
				.getReaderComparisonResult(dateFilter, studentFilter);
		return readerComparisonResult;
	}
}
